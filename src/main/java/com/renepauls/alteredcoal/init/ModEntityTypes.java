package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.entities.*;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
	
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, AlteredCoal.MOD_ID);

	public static final RegistryObject<EntityType<WhiteBoxEntity>> WHITE_BOX_ENTITY = ENTITY_TYPES.register("white_box_entity", () -> 
	EntityType.Builder.<WhiteBoxEntity>create(WhiteBoxEntity::new, 
		EntityClassification.CREATURE).size(1.0f, 1.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "white_box_entity").toString()));

	public static final RegistryObject<EntityType<BetaCarEntity>> BETA_CAR_ENTITY = ENTITY_TYPES.register("beta_car_entity", () -> 
	EntityType.Builder.<BetaCarEntity>create(BetaCarEntity::new, 
		EntityClassification.CREATURE).size(1.0f, 1.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "beta_car_entity").toString()));
	
	public static final RegistryObject<EntityType<SimpleCarEntity>> SIMPLE_CAR_ENTITY = ENTITY_TYPES.register("simple_car_entity", () -> 
	EntityType.Builder.<SimpleCarEntity>create(SimpleCarEntity::new, 
		EntityClassification.CREATURE).size(1.0f, 1.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "beta_car_entity").toString()));
	
	public static final RegistryObject<EntityType<SnowMobileEntity>> SNOW_MOBILE_ENTITY = ENTITY_TYPES.register("snow_mobile_entity", () -> 
	EntityType.Builder.<SnowMobileEntity>create(SnowMobileEntity::new, 
		EntityClassification.CREATURE).size(1.0f, 1.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "snow_mobile_entity").toString()));
	
	public static final RegistryObject<EntityType<SnowMobileEntity>> SNOW_MOBILE_2_ENTITY = ENTITY_TYPES.register("snow_mobile_2_entity", () -> 
	EntityType.Builder.<SnowMobileEntity>create(SnowMobileEntity::new, 
		EntityClassification.CREATURE).size(1.0f, 1.0f).build(new ResourceLocation(AlteredCoal.MOD_ID, "snow_mobile_2_entity").toString()));
	
	
}
