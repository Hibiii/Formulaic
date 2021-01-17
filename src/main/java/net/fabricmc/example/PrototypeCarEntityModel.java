// Made with Blockbench 3.7.5
	// Exported for Minecraft version 1.15
	// Paste this class into your mod and generate all required imports

	package net.fabricmc.example;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

public class PrototypeCarEntityModel extends EntityModel<PrototypeCarEntity> {
private final ModelPart TyreFR;
	private final ModelPart TyreFL;
	private final ModelPart TyreRL;
	private final ModelPart TyreRR;
	private final ModelPart Body;
	private final ModelPart SteeringWheel_r1;
	private final ModelPart WheelBars;
	private final ModelPart WBar3R_r1;
	private final ModelPart WBar3L_r1;
	private final ModelPart FrontWing;
	private final ModelPart RearWing;
	private final ModelPart RwWing_r1;
public PrototypeCarEntityModel() {
		textureWidth = 128;
		textureHeight = 128;
		TyreFR = new ModelPart(this);
		TyreFR.setPivot(-11.5F, 19.5F, -23.5F);
		TyreFR.setTextureOffset(0, 110).addCuboid(-2.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, 0.0F, false);

		TyreFL = new ModelPart(this);
		TyreFL.setPivot(11.5F, 19.5F, -23.5F);
		TyreFL.setTextureOffset(0, 110).addCuboid(-2.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, 0.0F, true);

		TyreRL = new ModelPart(this);
		TyreRL.setPivot(11.5F, 19.5F, 30.5F);
		TyreRL.setTextureOffset(0, 110).addCuboid(-2.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, 0.0F, true);

		TyreRR = new ModelPart(this);
		TyreRR.setPivot(-11.5F, 19.5F, 30.5F);
		TyreRR.setTextureOffset(0, 110).addCuboid(-2.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, 0.0F, false);

		Body = new ModelPart(this);
		Body.setPivot(0.0F, 18.0F, 0.0F);
		Body.setTextureOffset(86, 44).addCuboid(-3.0F, -1.0F, -30.0F, 6.0F, 6.0F, 15.0F, 0.0F, false);
		Body.setTextureOffset(0, 25).addCuboid(-11.0F, 3.0F, -15.0F, 22.0F, 2.0F, 40.0F, 0.0F, false);
		Body.setTextureOffset(78, 68).addCuboid(-11.0F, -4.0F, -15.0F, 22.0F, 7.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(0, 41).addCuboid(8.0F, -4.0F, -12.0F, 3.0F, 7.0F, 17.0F, 0.0F, true);
		Body.setTextureOffset(0, 41).addCuboid(-11.0F, -4.0F, -12.0F, 3.0F, 7.0F, 17.0F, 0.0F, false);
		Body.setTextureOffset(76, 0).addCuboid(-8.0F, -12.0F, 2.0F, 16.0F, 15.0F, 10.0F, 0.0F, false);
		Body.setTextureOffset(0, 67).addCuboid(-14.0F, -4.0F, 5.0F, 28.0F, 7.0F, 20.0F, 0.0F, false);
		Body.setTextureOffset(88, 27).addCuboid(-4.0F, -9.0F, 12.0F, 8.0F, 5.0F, 12.0F, 0.0F, false);
		Body.setTextureOffset(38, 94).addCuboid(-8.0F, -2.0F, 25.0F, 16.0F, 2.0F, 14.0F, 0.0F, false);
		Body.setTextureOffset(106, 104).addCuboid(-1.0F, 0.0F, 37.0F, 2.0F, 4.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(28, 111).addCuboid(-9.0F, 0.0F, 25.0F, 18.0F, 5.0F, 12.0F, 0.0F, false);

		SteeringWheel_r1 = new ModelPart(this);
		SteeringWheel_r1.setPivot(-0.5F, -4.0F, -10.0F);
		Body.addChild(SteeringWheel_r1);
		setRotationAngle(SteeringWheel_r1, 0.6981F, 0.0F, 0.0F);
		SteeringWheel_r1.setTextureOffset(0, 101).addCuboid(-2.5F, -1.5F, -0.5F, 6.0F, 3.0F, 1.0F, 0.0F, false);

		WheelBars = new ModelPart(this);
		WheelBars.setPivot(2.0F, 3.0F, -24.0F);
		Body.addChild(WheelBars);
		WheelBars.setTextureOffset(0, 108).addCuboid(-11.0F, -1.0F, 0.0F, 18.0F, 1.0F, 1.0F, 0.0F, false);
		WheelBars.setTextureOffset(0, 108).addCuboid(-11.0F, -3.0F, -1.0F, 18.0F, 1.0F, 1.0F, 0.0F, false);

		WBar3R_r1 = new ModelPart(this);
		WBar3R_r1.setPivot(-8.5F, -2.5F, 2.5F);
		WheelBars.addChild(WBar3R_r1);
		setRotationAngle(WBar3R_r1, 0.0F, -0.3491F, -0.3054F);
		WBar3R_r1.setTextureOffset(0, 106).addCuboid(-3.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, 0.0F, true);

		WBar3L_r1 = new ModelPart(this);
		WBar3L_r1.setPivot(3.5F, -2.5F, 2.5F);
		WheelBars.addChild(WBar3L_r1);
		setRotationAngle(WBar3L_r1, 0.0F, 0.3491F, 0.3054F);
		WBar3L_r1.setTextureOffset(0, 106).addCuboid(-3.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, 0.0F, false);

		FrontWing = new ModelPart(this);
		FrontWing.setPivot(0.0F, 19.0F, -32.0F);
		FrontWing.setTextureOffset(96, 78).addCuboid(11.0F, -2.0F, -5.0F, 2.0F, 6.0F, 8.0F, 0.0F, true);
		FrontWing.setTextureOffset(96, 78).addCuboid(-13.0F, -2.0F, -5.0F, 2.0F, 6.0F, 8.0F, 0.0F, false);
		FrontWing.setTextureOffset(0, 0).addCuboid(-11.0F, 3.0F, -5.0F, 22.0F, 1.0F, 5.0F, 0.0F, false);
		FrontWing.setTextureOffset(84, 94).addCuboid(2.0F, -2.0F, -5.0F, 1.0F, 5.0F, 4.0F, 0.0F, true);
		FrontWing.setTextureOffset(84, 94).addCuboid(-3.0F, -2.0F, -5.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
		FrontWing.setTextureOffset(0, 14).addCuboid(-3.0F, -5.0F, -7.0F, 6.0F, 3.0F, 24.0F, 0.0F, false);

		RearWing = new ModelPart(this);
		RearWing.setPivot(0.0F, 16.0F, 36.0F);
		RearWing.setTextureOffset(76, 110).addCuboid(8.0F, -4.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, true);
		RearWing.setTextureOffset(106, 101).addCuboid(8.0F, -9.0F, -6.0F, 1.0F, 5.0F, 10.0F, 0.0F, true);
		RearWing.setTextureOffset(76, 110).addCuboid(-9.0F, -4.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
		RearWing.setTextureOffset(106, 101).addCuboid(-9.0F, -9.0F, -6.0F, 1.0F, 5.0F, 10.0F, 0.0F, false);

		RwWing_r1 = new ModelPart(this);
		RwWing_r1.setPivot(0.0F, -6.5F, 0.0F);
		RearWing.addChild(RwWing_r1);
		setRotationAngle(RwWing_r1, 0.3927F, 0.0F, 0.0F);
		RwWing_r1.setTextureOffset(84, 116).addCuboid(-8.0F, -0.5F, -3.0F, 16.0F, 1.0F, 6.0F, 0.0F, false);
}
@Override
public void setAngles(PrototypeCarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
}
@Override
public void render(MatrixStack matrixStack, VertexConsumer	buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		matrixStack.push();
		matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
		matrixStack.translate(0.0, -1.5, 0.0);
		TyreFR.render(matrixStack, buffer, packedLight, packedOverlay);
		TyreFL.render(matrixStack, buffer, packedLight, packedOverlay);
		TyreRL.render(matrixStack, buffer, packedLight, packedOverlay);
		TyreRR.render(matrixStack, buffer, packedLight, packedOverlay);
		Body.render(matrixStack, buffer, packedLight, packedOverlay);
		FrontWing.render(matrixStack, buffer, packedLight, packedOverlay);
		RearWing.render(matrixStack, buffer, packedLight, packedOverlay);
		matrixStack.pop();
}
public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
}

	}