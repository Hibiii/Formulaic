package net.fabricmc.example;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import java.util.UUID;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

public class PrototypeCarEntity extends Entity {

	public PrototypeCarEntity(EntityType<?> type, World world) {
		super(type, world);
		// TODO Auto-generated constructor stub
	}
	
	@Environment(EnvType.CLIENT)
	public PrototypeCarEntity(World world, double x, double y, double z, int id, UUID uuid) {
		super(ExampleMod.PROTOTYPE_CAR, world);
		updatePosition(x, y, z);
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
