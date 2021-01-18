package hibiii.formulaic;

import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class FormulaicClient implements ClientModInitializer {

	private static MinecraftClient client = MinecraftClient.getInstance();
	
	@Override
	public void onInitializeClient() {
		
		// Yes, entities without renderers do exist
		EntityRendererRegistry.INSTANCE.register(Formulaic.CAR_GEN1, (dispatcher, context) -> {
			return new Gen1CarEntityRenderer(dispatcher);
		});
		
		// See Formulaic.java for packet format info
		ClientSidePacketRegistry.INSTANCE.register(Formulaic.CAR_SPAWN_PACKET,(context, packet) -> {
			double x = packet.readDouble();
			double y = packet.readDouble();
			double z = packet.readDouble();
			int entityId = packet.readInt();
			UUID entityUuid = packet.readUuid();
			// Move car spawning off the network thread
			context.getTaskQueue().execute(() -> {
				Gen1CarEntity car = new Gen1CarEntity(client.world, x, y, z, entityId, entityUuid);
				client.world.addEntity(entityId, car);
			});
		});
	}
}