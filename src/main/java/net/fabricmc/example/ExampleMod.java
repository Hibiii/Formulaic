package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer {

	public static final Identifier CAR_SPAWN_PACKET = new Identifier("formulaic");
	
	public static final EntityType<PrototypeCarEntity> PROTOTYPE_CAR = Registry.register(
			Registry.ENTITY_TYPE, 
			new Identifier("formulaic", "prototype_car"),
			FabricEntityTypeBuilder.<PrototypeCarEntity>create(SpawnGroup.MISC, PrototypeCarEntity::new).dimensions(EntityDimensions.fixed(1.7f, 1.3f)).build());
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
	}
}
