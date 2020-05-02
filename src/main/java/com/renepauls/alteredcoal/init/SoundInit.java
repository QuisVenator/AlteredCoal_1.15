package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.entities.*;
import com.renepauls.alteredcoal.entities.vehicle.BaseTruckEntity;
import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundInit {
	
	public static DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, AlteredCoal.MOD_ID);
	
	public static final RegistryObject<SoundEvent> DRIVINIG_CAR_SOUND = SOUNDS.register("car_driving", () -> new SoundEvent(new ResourceLocation(AlteredCoal.MOD_ID, "car_driving")));
}
