package com.renepauls.alteredcoal.util;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.client.render.*;
import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;
import com.renepauls.alteredcoal.init.GuiInit;
import com.renepauls.alteredcoal.init.ModEntityTypes;
import com.renepauls.alteredcoal.objects.gui.screens.*;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

@Mod.EventBusSubscriber(modid = AlteredCoal.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNOW_MOBILE_2_ENTITY.get(), SnowMobile2EntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BASE_TRUCK_ENTITY.get(), BaseTruckEntityRenderer::new);
		ScreenManager.registerFactory(GuiInit.BASE_TRUCK_CONTAINER.get(), BaseTruckScreen::new);
		ScreenManager.registerFactory(GuiInit.SNOW_MOBILE_CONTAINER.get(), SnowMobileScreen::new);
	}
	
}