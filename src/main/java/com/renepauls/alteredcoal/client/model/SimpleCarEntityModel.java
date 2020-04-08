package com.renepauls.alteredcoal.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.entities.SimpleCarEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SimpleCarEntityModel<T extends SimpleCarEntity> extends EntityModel<T> {
	private final ModelRenderer Body;
	private final ModelRenderer Wheels;
	private final ModelRenderer axis;
	private final ModelRenderer anhanger;
	private final ModelRenderer walls;
	private final ModelRenderer backwall;

	public SimpleCarEntityModel() {
		textureWidth = 64;
		textureHeight = 64;

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 24.0F, 0.0F);

		Wheels = new ModelRenderer(this);
		Wheels.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.addChild(Wheels);
		Wheels.setTextureOffset(0, 0).addBox( 9.0F, -3.0F, -7.0F, 1, 3, 3, 0.0F, false);

		Wheels.setTextureOffset(0, 0).addBox( 9.0F, -3.0F, 4.0F, 1, 3, 3, 0.0F, false);

		Wheels.setTextureOffset(0, 0).addBox( -10.0F, -3.0F, -7.0F, 1, 3, 3, 0.0F, false);

		Wheels.setTextureOffset(0, 0).addBox( -10.0F, -3.0F, 4.0F, 1, 3, 3, 0.0F, false);


		axis = new ModelRenderer(this);
		axis.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.addChild(axis);
		axis.setTextureOffset(0, 0).addBox( -9.0F, -2.0F, -6.0F, 18, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -9.0F, -2.0F, 5.0F, 18, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( 5.0F, -4.0F, 4.0F, 2, 2, 3, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -7.0F, -4.0F, 4.0F, 2, 2, 3, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -7.0F, -4.0F, -7.0F, 2, 2, 3, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( 5.0F, -4.0F, -7.0F, 2, 2, 3, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -7.0F, -2.0F, -7.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -7.0F, -2.0F, -5.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -7.0F, -2.0F, 4.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( -7.0F, -2.0F, 6.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( 5.0F, -2.0F, -7.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( 5.0F, -2.0F, -5.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( 5.0F, -2.0F, 4.0F, 2, 1, 1, 0.0F, false);

		axis.setTextureOffset(0, 0).addBox( 5.0F, -2.0F, 6.0F, 2, 1, 1, 0.0F, false);


		anhanger = new ModelRenderer(this);
		anhanger.setRotationPoint(0.0F, 0.0F, 0.0F);
		axis.addChild(anhanger);
		anhanger.setTextureOffset(0, 0).addBox( -2.0F, -4.0F, -15.0F, 1, 1, 5, 0.0F, false);

		anhanger.setTextureOffset(0, 0).addBox( 0.0F, -4.0F, -15.0F, 1, 1, 5, 0.0F, false);

		anhanger.setTextureOffset(0, 0).addBox( -1.0F, -4.0F, -13.0F, 1, 1, 3, 0.0F, false);

		anhanger.setTextureOffset(0, 0).addBox( -1.0F, -4.0F, -15.0F, 1, 1, 1, 0.0F, false);


		walls = new ModelRenderer(this);
		walls.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.addChild(walls);
		walls.setTextureOffset(0, 0).addBox( -10.0F, -5.0F, -12.0F, 20, 1, 25, 0.0F, false);

		walls.setTextureOffset(0, 0).addBox( -9.0F, -11.0F, -12.0F, 18, 6, 1, 0.0F, false);

		walls.setTextureOffset(0, 0).addBox( -10.0F, -11.0F, -12.0F, 1, 6, 25, 0.0F, false);

		walls.setTextureOffset(0, 0).addBox( 9.0F, -11.0F, -12.0F, 1, 6, 25, 0.0F, false);


		backwall = new ModelRenderer(this);
		backwall.setRotationPoint(0.0F, -6.0F, -1.0F);
		setRotationAngle(backwall, -0.4363F, 0.0F, 0.0F);
		walls.addChild(backwall);
		backwall.setTextureOffset(0, 0).addBox( -9.0F, -11.0F, 12.0F, 18, 6, 1, 0.0F, false);

	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		Body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
	}
}