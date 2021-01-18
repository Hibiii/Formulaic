package hibiii.formulaic;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Formulaic implements ModInitializer {

	// Vanilla does not handle entity spawn packets for non-vanilla entities
	public static final Identifier CAR_SPAWN_PACKET = new Identifier("formulaic");
	// double x
	// double y
	// double z
	// int entityId
	// UUID spawnedEntityUuid
	
	// Register the car using FAPI
	public static final EntityType<Gen1CarEntity> CAR_GEN1 = Registry.register(
			Registry.ENTITY_TYPE, 
			new Identifier("formulaic", "gen1_car"),
			FabricEntityTypeBuilder
				.<Gen1CarEntity>create(
					SpawnGroup.MISC, Gen1CarEntity::new)
				.dimensions(EntityDimensions.fixed(1.9f, 1.0f))  // Currently a little too small
				.trackRangeBlocks(8)
				.build()
			);
	
	@Override
	public void onInitialize() {
	}
}
