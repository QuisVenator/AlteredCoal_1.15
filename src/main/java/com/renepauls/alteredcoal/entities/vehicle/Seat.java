package com.renepauls.alteredcoal.entities.vehicle;

import com.renepauls.alteredcoal.entities.SnowMobileEntity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Seat {
	private Entity seatHolder = null;
	private float front, left, up;
	public Seat(float front, float left, float up) {
		this.front = front;
		this.left = left;
		this.up = up;
	}
	
	public float xOffset(float rotation) {
		return (float)Math.sin(rotation / 180 * Math.PI) * -front
				+ (float)Math.cos(rotation / 180 * Math.PI) * left;
	}
	public float yOffset() {
		return up;
	}
	public float zOffset(float rotation) {
		return (float)Math.cos(rotation / 180 * Math.PI) * front
				+ (float)Math.sin(rotation / 180 * Math.PI) * left;
	}
	
	public boolean takeSeat(Entity entityIn, boolean simulation) {
		if(!ocupied() && accepts(entityIn)) {
			if(!simulation) seatHolder = entityIn;
			return true;
		}
		return false;
	}
	
	public boolean accepts(Entity entityIn) {
		return true;
	}
	
	public void kickPassenger() {
		seatHolder = null;
	}
	
	public boolean ocupied() {
		return seatHolder != null;
	}
	
	public float distance(SnowMobileEntity vehicleIn, Vec3d pos) {
		return (float) pos.distanceTo(vehicleIn.getPositionVec().add(xOffset(vehicleIn.vehicleRotation), yOffset(), zOffset(vehicleIn.vehicleRotation)));
	}
}
