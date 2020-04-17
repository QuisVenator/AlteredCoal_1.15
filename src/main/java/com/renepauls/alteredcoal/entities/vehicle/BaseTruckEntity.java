package com.renepauls.alteredcoal.entities.vehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class BaseTruckEntity extends LandVehicleEntity {

	public BaseTruckEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
		stepHeight = 0.6f;
		steerDegree = 3f;
		maxSteer = 10f;

		seatManager.addDriverSeat(2.1f, 1.0f, 1.7f);;
		seatManager.addSeat(2.1f, -1.0f, 1.7f);;
	}
	
	@Override
	public float getRenderScale() {
		return 1.8F;
	}

	@Override
	public double getMountedYOffset() {
		return 2f;
	}
}
