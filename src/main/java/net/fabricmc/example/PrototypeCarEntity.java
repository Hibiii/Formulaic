package net.fabricmc.example;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import java.util.UUID;

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
import net.minecraft.world.World;

public class PrototypeCarEntity extends Entity {

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
		this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
		this.move(MovementType.SELF, this.getVelocity());
		this.checkBlockCollision();
	}
	
	// code taken directly from minecart code is :concern:ing
	// !!! XXX TODO !!! CONSIDER REDOING THIS !!! XXX !!!
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
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        return true;
    }

}
