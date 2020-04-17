package com.renepauls.alteredcoal.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.model.SnowMobile2EntityModel;
import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;


public class SnowMobile2EntityRenderer extends EntityRenderer<SnowMobileEntity> {

	protected SnowMobile2EntityModel<SnowMobileEntity> entityModel;
	
	protected static final ResourceLocation TEXTURE = new ResourceLocation(AlteredCoal.MOD_ID,
			"textures/entity/snow_mobile_2_entity.png");
	
	public SnowMobile2EntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.6f;
		this.entityModel = new SnowMobile2EntityModel<SnowMobileEntity>();
	}
	
	@Override
	public ResourceLocation getEntityTexture(SnowMobileEntity entity) {
		return TEXTURE;
	}
	
	@Override
	public void render(SnowMobileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		
		float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevVehicleRotation, entityIn.vehicleRotation);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - f));
		
		matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
		//matrixStackIn.translate(0.0D, (double)-1.501F, 0.0D);
	
		RenderType rendertype = entityModel.getRenderType(TEXTURE);
		if (rendertype != null) {
		   IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
		   this.entityModel.renderBody(matrixStackIn, ivertexbuilder, packedLightIn, 0xA0000, 1.0F, 1.0F, 1.0F, 1.0F);
		   
		   //System.out.println("Partial ticks: " + partialTicks);

		   float steer = MathHelper.interpolateAngle(partialTicks, entityIn.getPrevSteerRotation(), entityIn.getCurSteerRotation());
		   matrixStackIn.rotate(Vector3f.YP.rotationDegrees(steer));
		   this.entityModel.renderSteering(matrixStackIn, ivertexbuilder, packedLightIn, 0xA0000, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	
		matrixStackIn.pop();
	}
}
