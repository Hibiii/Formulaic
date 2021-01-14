package net.fabricmc.example;

import java.util.Arrays;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class PrototypeCarEntityModel extends CompositeEntityModel<PrototypeCarEntity> {
    
    private final ModelPart[] parts;
 
    public PrototypeCarEntityModel() {
    	this.parts = new ModelPart[1];
    	this.parts[0] = new ModelPart(this, 0, 0);
		this.parts[0].addCuboid(-6, -6, -6, 12, 12, 12);
    }

	@Override
	public void setAngles(PrototypeCarEntity entity, float limbAngle, float limbDistance, float animationProgress,
			float headYaw, float headPitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<ModelPart> getParts() {
		// TODO Auto-generated method stub
		return Arrays.<ModelPart>asList(this.parts);
	}
 
}