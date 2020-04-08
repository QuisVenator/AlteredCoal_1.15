package com.renepauls.alteredcoal.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.entities.SnowMobileEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SnowMobileEntityModel<T extends SnowMobileEntity> extends EntityModel<T> {
	private final ModelRenderer vehicle;
	private final ModelRenderer steuerrechts;
	private final ModelRenderer steuerlinks;
	private final ModelRenderer steueransatz;
	private final ModelRenderer skinachoben;
	private final ModelRenderer stelzen2;
	private final ModelRenderer front;
	private final ModelRenderer stelzen1;
	private final ModelRenderer back;

	public SnowMobileEntityModel() {
		textureWidth = 128;
		textureHeight = 128;

		vehicle = new ModelRenderer(this);
		vehicle.setRotationPoint(0.0F, 24.0F, 0.0F);
		vehicle.setTextureOffset(0, 10).addBox( -2.625F, -9.0F, -5.5F, 5, 1, 1, 0.0F, false);

		vehicle.setTextureOffset(19, 33).addBox( 3.2F, -1.0F, -14.9F, 1, 1, 9, 0.0F, false);

		vehicle.setTextureOffset(30, 34).addBox( -4.7F, -1.0F, -14.9F, 1, 1, 9, 0.0F, false);

		vehicle.setTextureOffset(0, 25).addBox( -2.55F, -4.2F, -2.3F, 5, 2, 9, 0.0F, false);

		vehicle.setTextureOffset(0, 15).addBox( -4.0F, -3.0F, -14.4F, 8, 1, 9, 0.0F, false);

		vehicle.setTextureOffset(19, 25).addBox( -4.0F, -4.9479F, -14.6382F, 8, 2, 6, 0.0F, false);

		vehicle.setTextureOffset(0, 36).addBox( -4.0F, -6.0F, -8.9F, 8, 3, 3, 0.0F, false);

		vehicle.setTextureOffset(0, 0).addBox( -2.0F, -5.4F, -6.0F, 4, 3, 12, 0.0F, false);

		vehicle.setTextureOffset(25, 11).addBox( -2.0F, -3.0F, -2.0F, 4, 3, 9, 0.0F, false);


		steuerrechts = new ModelRenderer(this);
		steuerrechts.setRotationPoint(2.5F, -9.0F, -4.1F);
		setRotationAngle(steuerrechts, 0.0F, 2.3911F, 0.0F);
		vehicle.addChild(steuerrechts);
		steuerrechts.setTextureOffset(0, 15).addBox( -2.3792F, 0.0F, 0.3819F, 2, 1, 1, 0.0F, false);


		steuerlinks = new ModelRenderer(this);
		steuerlinks.setRotationPoint(-3.0F, -9.0F, -3.3F);
		setRotationAngle(steuerlinks, 0.0F, 0.7854F, 0.0F);
		vehicle.addChild(steuerlinks);
		steuerlinks.setTextureOffset(0, 17).addBox( -1.1292F, 0.0F, -1.2905F, 2, 1, 1, 0.0F, false);


		steueransatz = new ModelRenderer(this);
		steueransatz.setRotationPoint(0.1F, -6.6F, -6.2F);
		setRotationAngle(steueransatz, 0.4363F, 0.0F, 0.0F);
		vehicle.addChild(steueransatz);
		steueransatz.setTextureOffset(0, 19).addBox( -1.4F, -1.5F, 0.4F, 2, 1, 1, 0.0F, false);


		skinachoben = new ModelRenderer(this);
		skinachoben.setRotationPoint(-3.0F, 0.0F, -15.0F);
		setRotationAngle(skinachoben, -0.4363F, 0.0F, 0.0F);
		vehicle.addChild(skinachoben);
		skinachoben.setTextureOffset(6, 0).addBox( -1.7F, -1.0F, -2.1362F, 1, 1, 2, 0.0F, false);

		skinachoben.setTextureOffset(6, 6).addBox( 6.2F, -1.0F, -2.1362F, 1, 1, 2, 0.0F, false);


		stelzen2 = new ModelRenderer(this);
		stelzen2.setRotationPoint(-4.0F, -2.3F, -11.0F);
		setRotationAngle(stelzen2, 1.3963F, 0.0F, 0.1745F);
		vehicle.addChild(stelzen2);
		stelzen2.setTextureOffset(0, 0).addBox( 0.0F, -1.0F, -2.0F, 1, 1, 4, 0.0F, false);


		front = new ModelRenderer(this);
		front.setRotationPoint(0.0F, -3.0F, -13.0F);
		setRotationAngle(front, 0.3491F, 0.0F, 0.0F);
		vehicle.addChild(front);
		front.setTextureOffset(20, 0).addBox( -3.75F, -1.8907F, -0.8731F, 7, 2, 9, 0.0F, false);


		stelzen1 = new ModelRenderer(this);
		stelzen1.setRotationPoint(4.0F, -2.3F, -11.0F);
		setRotationAngle(stelzen1, 1.3963F, 0.0F, -0.1745F);
		vehicle.addChild(stelzen1);
		stelzen1.setTextureOffset(0, 5).addBox( -1.0F, -1.0F, -2.0F, 1, 1, 4, 0.0F, false);


		back = new ModelRenderer(this);
		back.setRotationPoint(0.0F, -3.6F, 6.2F);
		setRotationAngle(back, 0.5236F, 0.0F, 0.0F);
		vehicle.addChild(back);
		back.setTextureOffset(41, 23).addBox( -1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F, false);

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		vehicle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		
	}
}