package net.fabricmc.example;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.client.network.ClientPlayerEntity;
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

public class PrototypeCarEntity extends Entity {

	private double x, y, z;
	private boolean w, a, s, d;
	private float velocityDecay;
	private int posInterpolationSteps;
	private float carYaw;
	private float yawVelocity;
	
	public PrototypeCarEntity(EntityType<?> type, World world) {
		super(type, world);
		this.inanimate = true;
		// TODO Auto-generated constructor stub
	}
	
	@Environment(EnvType.CLIENT)
	public PrototypeCarEntity(World world, double x, double y, double z, int id, UUID uuid) {
		super(ExampleMod.PROTOTYPE_CAR, world);
		updatePosition(x, y, z);
		updateTrackedPosition(x, y, z);
		setEntityId(id);
		setUuid(uuid);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		packet.writeDouble(getX());
		packet.writeDouble(getY());
		packet.writeDouble(getZ());
		packet.writeInt(getEntityId());
		packet.writeUuid(getUuid());
		return ServerSidePacketRegistry.INSTANCE.toPacket(ExampleMod.CAR_SPAWN_PACKET, packet);
	}

	@Override
	public void tick() {
		super.tick();
        this.posInterpolation();
            this.updateVelocity();
                this.updateMovement();
            this.move(MovementType.SELF, this.getVelocity());
        this.checkBlockCollision();
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
		return true;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		// TODO Auto-generated method stub
		
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
	
	private void updateVelocity() {
        double double2 = -0.03999999910593033;
        double double4 = this.hasNoGravity() ? 0.0 : -0.03999999910593033;
        double double6 = 0.0;
        this.velocityDecay = 0.05f;
                double6 = 0.009999999776482582;
                this.velocityDecay = 0.45f;
            Vec3d vec3d8 = this.getVelocity();
            this.setVelocity(vec3d8.x * this.velocityDecay, vec3d8.y + double4, vec3d8.z * this.velocityDecay);
            this.yawVelocity *= this.velocityDecay;
            if (double6 > 0.0) {
                Vec3d vec3d9 = this.getVelocity();
                this.setVelocity(vec3d9.x, (vec3d9.y + double6 * 0.06153846016296973) * 0.75, vec3d9.z);
            }
}
	
	 private void updateMovement() {
	        if (!this.hasPassengers()) {
	            return;
	        }
	        float float2 = 0.0f;
	        if (this.a) {
	            --this.yawVelocity;
	        }
	        if (this.d) {
	            ++this.yawVelocity;
	        }
	        if (this.d != this.a && !this.w && !this.s) {
	            float2 += 0.005f;
	        }
	        this.yaw += this.yawVelocity;
	        if (this.w) {
	            float2 += 0.04f;
	        }
	        if (this.s) {
	            float2 -= 0.005f;
	        }
	        this.setVelocity(this.getVelocity().add(MathHelper.sin(-this.yaw * 0.017453292f) * float2, 0.0, MathHelper.cos(this.yaw * 0.017453292f) * float2));
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
