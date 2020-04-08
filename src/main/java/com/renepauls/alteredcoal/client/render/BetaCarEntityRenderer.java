package com.renepauls.alteredcoal.client.render;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.model.BetaCarEntityModel;
import com.renepauls.alteredcoal.entities.BetaCarEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;


public class BetaCarEntityRenderer extends MobRenderer<BetaCarEntity, BetaCarEntityModel<BetaCarEntity>> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(AlteredCoal.MOD_ID,
			"textures/entity/beta_car_entity.png");
	
	public BetaCarEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new BetaCarEntityModel<BetaCarEntity>(), 0.5f);
	}
	
	@Override
	public ResourceLocation getEntityTexture(BetaCarEntity entity) {
		return TEXTURE;
	}
}
