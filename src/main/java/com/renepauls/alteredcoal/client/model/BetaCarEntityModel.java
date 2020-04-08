package com.renepauls.alteredcoal.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.entities.BetaCarEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BetaCarEntityModel <T extends BetaCarEntity> extends EntityModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer wheels;
	private final ModelRenderer group;
	public BetaCarEntityModel() {
		textureWidth = 16;
		textureHeight = 16;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.setTextureOffset(0, 14).addBox(-6.0F, 6.0F, -6.0F, 12, 1, 14, 0.0F, false);

		wheels = new ModelRenderer(this);
		wheels.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(wheels);
		wheels.setTextureOffset(0, 2).addBox(6.0F, 6.0F, -5.0F, 1, 2, 2, 0.0F, false);
		wheels.setTextureOffset(0, 2).addBox(6.0F, 6.0F, 5.0F, 1, 2, 2, 0.0F, false);
		wheels.setTextureOffset(0, 2).addBox(-7.0F, 6.0F, -5.0F, 1, 2, 2, 0.0F, false);
		wheels.setTextureOffset(0, 2).addBox(-7.0F, 6.0F, 5.0F, 1, 2, 2, 0.0F, false);

		group = new ModelRenderer(this);
		group.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(group);
		wheels.setTextureOffset(0, 1).addBox(3.0F, 6.0F, -7.0F, 1, 1, 1, 0.0F, false);
		wheels.setTextureOffset(0, 1).addBox(-4.0F, 6.0F, -7.0F, 1, 1, 1, 0.0F, false);
		wheels.setTextureOffset(0, 1).addBox(-5.0F, 6.0F, -8.0F, 10, 1, 1, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
