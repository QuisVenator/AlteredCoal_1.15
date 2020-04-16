package com.renepauls.alteredcoal.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BaseTruckEntity extends SnowMobileEntity {

	public BaseTruckEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
		stepHeight = 0.6f;
		steerDegree = 3f;
		maxSteer = 10f;
	}
	
	@Override
	public float getRenderScale() {
		return 1.8F;
	}

	@Override
	public float seatZOffset() {
		return (float)Math.cos(vehicleRotation / 180 * Math.PI) * 3f 
				+ (float)Math.sin(vehicleRotation / 180 * Math.PI) * 1f;
	}
	@Override
	public float seatXOffset() {
		return (float)Math.sin(vehicleRotation / 180 * Math.PI) * -3f
				+ (float)Math.cos(vehicleRotation / 180 * Math.PI) * 1f;
	}

	@Override
	public double getMountedYOffset() {
		return 2f;
	}
}
