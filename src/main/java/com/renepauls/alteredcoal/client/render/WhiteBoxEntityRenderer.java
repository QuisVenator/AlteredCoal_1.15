package com.renepauls.alteredcoal.client.render;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.model.WhiteBoxEntityModel;
import com.renepauls.alteredcoal.entities.WhiteBoxEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;


public class WhiteBoxEntityRenderer extends MobRenderer<WhiteBoxEntity, WhiteBoxEntityModel<WhiteBoxEntity>> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(AlteredCoal.MOD_ID,
			"textures/entity/example_entity.png");
	
	public WhiteBoxEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new WhiteBoxEntityModel<WhiteBoxEntity>(), 0.5f);
	}
	
	@Override
	public ResourceLocation getEntityTexture(WhiteBoxEntity entity) {
		return TEXTURE;
	}
}
