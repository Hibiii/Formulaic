package net.fabricmc.example;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PrototypeCarEntityRenderer extends EntityRenderer<PrototypeCarEntity> {
	protected final PrototypeCarEntityModel model;
    public PrototypeCarEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.model = new PrototypeCarEntityModel();
    }
 
    public PrototypeCarEntityModel getModel() {
    	return this.model;
    }
    @Override
    public Identifier getTexture(PrototypeCarEntity entity) {
        return new Identifier("minecraft", "textures/block/stone.png");
    }
    public static void register() {
        EntityRendererRegistry.INSTANCE.register(ExampleMod.PROTOTYPE_CAR,(entityRenderDispatcher,context)->new PrototypeCarEntityRenderer(entityRenderDispatcher));
    }
    public void render(PrototypeCarEntity prototypeCarEntity, float yaw, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
    	matrixStack.push();
    	VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(model.getLayer(new Identifier("minecraft", "textures/block/stone.png")));
    	model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    	matrixStack.pop();
    	super.render(prototypeCarEntity, yaw, delta, matrixStack, vertexConsumerProvider, i);
    }
    @Override
    public boolean shouldRender(PrototypeCarEntity car, Frustum frustum, double x, double y, double z) {
        return car.shouldRender(x, y, z);
    }
}
