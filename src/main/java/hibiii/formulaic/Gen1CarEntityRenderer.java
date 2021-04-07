package hibiii.formulaic;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

public class Gen1CarEntityRenderer extends EntityRenderer<Gen1CarEntity> {

	protected final Gen1CarEntityModel model;

	public Gen1CarEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
		this.model = new Gen1CarEntityModel();
	}

	public Gen1CarEntityModel getModel() {
		return this.model;
	}

	@Override
	public Identifier getTexture(Gen1CarEntity entity) {
		return new Identifier("formulaic", "textures/entity/prototype_car.png");
	}

	public static void register() {
		EntityRendererRegistry.INSTANCE.register(Formulaic.CAR_GEN1,(entityRenderDispatcher,context)->new Gen1CarEntityRenderer(entityRenderDispatcher));
	}

	public void render(Gen1CarEntity prototypeCarEntity, float yaw, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0f - yaw));
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(model.getLayer(new Identifier("formulaic", "textures/entity/prototype_car.png")));
		model.setAngles(prototypeCarEntity, -prototypeCarEntity.getFrontWheelYaw(), 0, 0, yaw, 0);
		model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
		matrixStack.pop();
		super.render(prototypeCarEntity, yaw, delta, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public boolean shouldRender(Gen1CarEntity car, Frustum frustum, double x, double y, double z) {
		return car.shouldRender(x, y, z);
	}
}
