package com.renepauls.alteredcoal.util;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.render.*;
import com.renepauls.alteredcoal.entities.SnowMobileEntity;
import com.renepauls.alteredcoal.init.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

@Mod.EventBusSubscriber(modid = AlteredCoal.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WHITE_BOX_ENTITY.get(), WhiteBoxEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BETA_CAR_ENTITY.get(), BetaCarEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SIMPLE_CAR_ENTITY.get(), SimpleCarEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNOW_MOBILE_ENTITY.get(), SnowMobileEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNOW_MOBILE_2_ENTITY.get(), SnowMobile2EntityRenderer::new);
	}
	
}