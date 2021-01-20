package hibiii.formulaic;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// So you have chosen :concern:
public class Gen1CarEntity extends Entity {

	private double x, y, z;
	private boolean w, a, s, d;            // Input (Power, Steer Left, Brake, Steer Right)
	private int posInterpolationSteps;
	private float carYaw;
	private float wheelYaw, oldWheelYaw;   // Front wheels' yaw
	private float torque;                  // Rear wheel torque
	private Vec3d speed;                   // Avoid perf loss, don't call this.getVelocity()
	private Vec3d wheelFront, wheelBack;

	private final static float
		carLength = 5f,
		carWidth = 1.7f,
		carMass = 880f,
		wheelbase = 3.4f,
		wheelRadius = 0.4f,
		torqueMaxAccel = 1f,
		torqueMaxBrake = -1.1f,
		momentOfInertiaCar = (carMass * (carWidth * carWidth + carLength * carLength)) / 12,
		momentOfInertiaBack = momentOfInertiaCar + (carMass * wheelbase * wheelbase) / 4,
		jeff = (-1 * carMass) / (2 * carLength); // -Mv²/2L from (21) and (22)

	// Called on summon
	public Gen1CarEntity(EntityType<?> type, World world) {
		super(type, world);
		this.inanimate = true;
	}

	// Called when received a spawn packet, merely a projection of the server on the client
	@Environment(EnvType.CLIENT)
	public Gen1CarEntity(World world, double x, double y, double z, int id, UUID uuid) {
		super(Formulaic.CAR_GEN1, world);
		updatePosition(x, y, z);
		updateTrackedPosition(x, y, z);
		setEntityId(id);
		setUuid(uuid);
	}

	// See Formulaic.java for packet format info
	@Override
	public Packet<?> createSpawnPacket() {
		PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		packet.writeDouble(getX());
		packet.writeDouble(getY());
		packet.writeDouble(getZ());
		packet.writeInt(getEntityId());
		packet.writeUuid(getUuid());
		return ServerSidePacketRegistry.INSTANCE.toPacket(Formulaic.CAR_SPAWN_PACKET, packet);
	}

	//// Tick ////
	@Override
	public void tick() {
		super.tick();
		speed = this.getVelocity();
		this.posInterpolation();
		
		if(this.isLogicalSideForUpdatingMovement()) {
			this.updateVelocity();
			if(this.world.isClient) {
				// This is where the driving happens
				this.updateMovement();
			}
			this.move(MovementType.SELF, speed);
		} else {
			this.setVelocity(Vec3d.ZERO);
		}
		this.checkBlockCollision();
		this.setHeadYaw(this.yaw);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.shouldCancelInteraction()) {
			return ActionResult.PASS;
		}
		if (this.hasPassengers()) {
			return ActionResult.PASS;
		}
		if (!this.world.isClient) {
			return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public boolean canClimb() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public double getMountedHeightOffset() {
		return -0.07;
	}

	@Override
	public boolean collides() {
		return !this.removed;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public Box getVisibilityBoundingBox() {
		return this.getBoundingBox();
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {

	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {

	}

	@Override
	@Nullable
	public Entity getPrimaryPassenger() {
		List<Entity> list = this.getPassengerList();
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.carYaw = yaw;
		this.posInterpolationSteps = 10;
	}

	public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
		w = pressingForward;
		a = pressingLeft;
		s = pressingBack;
		d = pressingRight;
	}

	// XXX I don't know how to physics :(
	private void updateVelocity() {
		this.setVelocity(speed = speed.multiply(0.95f, 1.0f, 0.95f).add(0.0f, -0.4f, 0.0f));
	}

	//// This is where you drivv ////
	private void updateMovement() {
		if (!this.hasPassengers()) {
			return;
		}
		
		//- Handle input -//
		// Accel and decel
		torque = 0;
		if(w)
			torque += torqueMaxAccel;
		if(s)
			torque += torqueMaxBrake;
		
		// Steering
		oldWheelYaw = wheelYaw;
		if(a && !d)
			wheelYaw = 0.5f;
		else if(d && !a)
			wheelYaw = -0.5f;
		else
			wheelYaw = 0;
		
		// (23)
		// XXX THIS CAN CAUSE THE CAR TO MOVE FASTER WHEN FALLING (length OF speed, A Vec3d)
		float omega = (float) (speed.length() / wheelRadius);
		
		Vec3d forceCentripetalFront;
		Vec3d forceCentripetalRear;
		if(wheelYaw != 0) {
			// (21)
			forceCentripetalFront = new Vec3d (
					jeff * speed.lengthSquared() * Math.tan(wheelYaw),
					0,
					jeff * speed.lengthSquared() * Math.pow(Math.tan(wheelYaw), 2));
			// (22)
			forceCentripetalRear = new Vec3d (
					jeff * speed.lengthSquared() * Math.tan(wheelYaw),
					0,
					0);
		} else {
			forceCentripetalFront = Vec3d.ZERO;
			forceCentripetalRear = Vec3d.ZERO;
		}
		// (33)
		float fr = momentOfInertiaBack * (float)Math.tan(wheelYaw) / (wheelbase * wheelbase);
		// (37)
		float acceleration = (float) ((wheelRadius * wheelRadius * (forceCentripetalFront.length() - (fr * speed.length())/Math.pow(Math.cos(wheelYaw), 2)*((wheelYaw - oldWheelYaw)/0.05)))/(2*momentOfInertiaBack + (carMass + fr*Math.tan(wheelYaw)*wheelRadius*wheelRadius)));
		
		
		this.setVelocity(speed);
	}

	private void posInterpolation() {
		if (this.isLogicalSideForUpdatingMovement()) {
			this.posInterpolationSteps = 0;
			this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
		}

		if (this.posInterpolationSteps > 0) {
			double d = this.getX() + (this.x - this.getX()) / (double)this.posInterpolationSteps;
			double e = this.getY() + (this.y - this.getY()) / (double)this.posInterpolationSteps;
			double f = this.getZ() + (this.z - this.getZ()) / (double)this.posInterpolationSteps;
			double g = MathHelper.wrapDegrees(this.carYaw - (double)this.yaw);
			this.yaw = (float)((double)this.yaw + g / (double)this.posInterpolationSteps);
			this.pitch = 0;
			--this.posInterpolationSteps;
			this.updatePosition(d, e, f);
			this.setRotation(this.yaw, this.pitch);
		}
	}

}
