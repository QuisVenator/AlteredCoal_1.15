package com.renepauls.alteredcoal.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.model.SnowMobileEntityModel;
import com.renepauls.alteredcoal.entities.SnowMobileEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;


public class SnowMobileEntityRenderer extends MobRenderer<SnowMobileEntity, SnowMobileEntityModel<SnowMobileEntity>> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(AlteredCoal.MOD_ID,
			"textures/entity/snow_mobile_entity.png");
	
	public SnowMobileEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SnowMobileEntityModel<SnowMobileEntity>(), 0.5f);
	}
	
	@Override
	public ResourceLocation getEntityTexture(SnowMobileEntity entity) {
		return TEXTURE;
	}
	
	@Override
	public void render(SnowMobileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.scale(2.0f, 2.0f, 2.0f);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
}
