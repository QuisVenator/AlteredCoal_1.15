package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.objects.gui.containers.*;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GuiInit {
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, AlteredCoal.MOD_ID);

	public static final RegistryObject<ContainerType<BaseTruckContainer>> BASE_TRUCK_CONTAINER = CONTAINER_TYPES.register("base_truck", () -> IForgeContainerType.create(BaseTruckContainer::new));
	public static final RegistryObject<ContainerType<SnowMobileContainer>> SNOW_MOBILE_CONTAINER = CONTAINER_TYPES.register("snow_mobile", () -> IForgeContainerType.create(SnowMobileContainer::new));
	
	
	
}
