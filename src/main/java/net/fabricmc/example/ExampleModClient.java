package net.fabricmc.example;

import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ExampleModClient implements ClientModInitializer {
 
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(ExampleMod.PROTOTYPE_CAR, (dispatcher, context) -> {
            return new PrototypeCarEntityRenderer(dispatcher);
        });
        
        ClientSidePacketRegistry.INSTANCE.register(ExampleMod.CAR_SPAWN_PACKET,(context, packet) -> {
        	double x = packet.readDouble();
        	double y = packet.readDouble();
        	double z = packet.readDouble();
        	int entityId = packet.readInt();
        	UUID entityUuid = packet.readUuid();
        	context.getTaskQueue().execute(() -> {
        		PrototypeCarEntity car = new PrototypeCarEntity(MinecraftClient.getInstance().world, x, y, z, entityId, entityUuid);
        	});
        });
    }
}