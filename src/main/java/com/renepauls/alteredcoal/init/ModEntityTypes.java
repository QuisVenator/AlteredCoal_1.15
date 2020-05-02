package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.entities.*;
import com.renepauls.alteredcoal.entities.vehicle.BaseTruckEntity;
import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
	
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, AlteredCoal.MOD_ID);

	public static final RegistryObject<EntityType<SnowMobileEntity>> SNOW_MOBILE_2_ENTITY = ENTITY_TYPES.register("snow_mobile_2_entity", () -> 
	EntityType.Builder.<SnowMobileEntity>create(SnowMobileEntity::new, 
		EntityClassification.CREATURE).size(1.0f, 1.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "snow_mobile_2_entity").toString()));
	
	public static final RegistryObject<EntityType<BaseTruckEntity>> BASE_TRUCK_ENTITY = ENTITY_TYPES.register("base_truck_entity", () -> 
	EntityType.Builder.<BaseTruckEntity>create(BaseTruckEntity::new, 
		EntityClassification.CREATURE).size(4.0f, 3.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "base_truck_entity").toString()));
}
