package hibiii.formulaic;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// So you have chosen :concern:
public class Gen1CarEntity extends Entity {

	private static final TrackedData<Float> FRONT_WHEEL_YAW;
	
	private double x, y, z;
	private boolean w, a, s, d;          // Input (Power, Steer Left, Brake, Steer Right)
	private int posInterpolationSteps;
	private float carYaw;
	private float wheelYaw = 0.0f;
	private float velocityDecay = 0.9f;
	private float accel = 0.0f;
	private double speedI;
	private double speedJ;
	private double speedX;
	private double speedZ;
	
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
		this.posInterpolation();
		
		if(this.isLogicalSideForUpdatingMovement()) {
			//this.updateVelocity();
			if(this.world.isClient) {
				// This is where the driving happens
				this.updateMovement();
			}
			this.move(MovementType.SELF, this.getVelocity());
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
		this.dataTracker.<Float>startTracking(Gen1CarEntity.FRONT_WHEEL_YAW, 0.0f);
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

	@Environment(EnvType.CLIENT)
	public float getFrontWheelYaw() {
		return wheelYaw;
	}
	public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
		w = pressingForward;
		a = pressingLeft;
		s = pressingBack;
		d = pressingRight;
	}

	//// This is where you drivv ////
	private void updateMovement() {
		double double4 = this.hasNoGravity() ? 0.0 : -0.04;
		
		// Player Input
		accel = 0.0f;
		if(w)
			accel += 0.04f;
		if(s)
			accel -= 0.005f;
		if(a & ! d)
			wheelYaw += 0.09f;
		else if (d & !a)
			wheelYaw -= 0.09f;
		else {
			if(wheelYaw > 0.06f)
				wheelYaw -=0.04f;
			else if(wheelYaw < -0.06f)
				wheelYaw += 0.04f;
			else
				wheelYaw = 0.0f;
		}
		if(wheelYaw > 0.2f)
			wheelYaw = 0.2f;
		else if (wheelYaw < -0.2f)
			wheelYaw = -0.2f;
		
		// Calculate forces and car-relative speed
		double accelI = accel * Math.cos(wheelYaw);
		speedI += accelI;
		speedJ = speedI * Math.sin(wheelYaw);
		
		// Calculate world-relative speed
		speedX = speedI * Math.sin(yaw) + speedJ * Math.cos(yaw);
		speedZ = speedI * Math.cos(yaw) + speedJ * Math.sin(yaw);

		// Calculate world-relative yaw
		if(Math.abs(speedI) > 0.1) {
			Vec3d oldVelocity = this.getVelocity();
			if(oldVelocity.x != speedX && speedX != 0d) {
				float gah = (float) ((wheelYaw + speedI)/speedI) - 1f;
				System.out.println(gah);
				this.yaw = (this.yaw + gah * 57.29578f)/2f;
			}
		}
		
		// Apply speed world-relative
		this.setVelocity(speedX, this.getVelocity().y + double4, speedZ);
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
	
	static {
		FRONT_WHEEL_YAW = DataTracker.<Float>registerData(Gen1CarEntity.class, TrackedDataHandlerRegistry.FLOAT);
	}

}
