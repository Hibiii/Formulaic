package net.fabricmc.example;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class PrototypeCarEntity extends Entity {

	public PrototypeCarEntity(EntityType<?> type, World world) {
		super(type, world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Packet<?> createSpawnPacket() {
		// TODO Auto-generated method stub
		return new EntitySpawnS2CPacket(this);
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
