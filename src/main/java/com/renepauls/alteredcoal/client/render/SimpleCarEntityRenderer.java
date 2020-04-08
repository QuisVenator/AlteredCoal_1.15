package com.renepauls.alteredcoal.client.render;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.model.SimpleCarEntityModel;
import com.renepauls.alteredcoal.entities.SimpleCarEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;


public class SimpleCarEntityRenderer extends MobRenderer<SimpleCarEntity, SimpleCarEntityModel<SimpleCarEntity>> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(AlteredCoal.MOD_ID,
			"textures/entity/simple_car_entity.png");
	
	public SimpleCarEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SimpleCarEntityModel<SimpleCarEntity>(), 0.5f);
	}
	
	@Override
	public ResourceLocation getEntityTexture(SimpleCarEntity entity) {
		return TEXTURE;
	}
}
