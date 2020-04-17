package com.renepauls.alteredcoal.entities.vehicle;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class SeatManager {
	private LandVehicleEntity vehicleIn;
	private ArrayList<Seat> seats = new ArrayList<>();
	
	public SeatManager(LandVehicleEntity parent) {
		this.vehicleIn = parent;
	}
	
	public void addSeat(Seat seat) {
		seats.add(seat);
	}
	public void addSeat(float front, float left, float up) {
		addSeat(new Seat(front, left, up));
	}
	public void addDriverSeat(Seat seat) {
		seats.add(0, seat);
	}
	public void addDriverSeat(float front, float left, float up) {
		addDriverSeat(new Seat(front, left, up));
	}
	
	public boolean hasSeats() {
		return seats.size() > 0;
	}
	
	public int passengerCount() {
		int i = 0;
		for(Seat e : seats) {
			if(e.ocupied()) i++;
		}
		return i;
	}
	
	public boolean isFull() {
		for(Seat e : seats) {
			if(!e.ocupied()) return false;
		}
		return true;
	}
	
	public boolean takeSeat(Entity entityIn) {
		int closestIndex = -1;
		float closest = Float.MAX_VALUE;
		for(int i = 0; i < seats.size(); i++) {
			if(seats.get(i).takeSeat(entityIn, true)) {
				float distance = seats.get(i).distance(vehicleIn, entityIn.getPositionVec());
				if(distance < closest) {
					closest = distance;
					closestIndex = i;
				}
			}
		}
		if(closestIndex > -1) { 
			return seats.get(closestIndex).takeSeat(entityIn, false);
		}
		return false;
	}
	
	public void removePassenger(Entity passenger) {
		for(Seat s : seats) {
			if(s.getPassenger() != null && s.getPassenger().equals(passenger)) {
				s.freeSeat();
			}
		}
	}
	
	@Nullable
	public Entity getControllingPassenger() {
		if(seats.size() == 0) return null;
		return seats.get(0).getPassenger();
	}
	
	public boolean isPassenger(Entity entityIn) {
		for(Seat s : seats) {
			if(entityIn.equals(s.getPassenger())) return true;
		}
		return false;
	}
	
	public boolean setPositionPassenger(Entity passenger, Entity.IMoveCallback positionSetter) {
		for(int i = 0; i < seats.size(); i++) {
			if(seats.get(i).getPassenger() != null && seats.get(i).getPassenger() == passenger) {
				positionSetter.accept(passenger, vehicleIn.getPosX() + seats.get(i).xOffset(vehicleIn.vehicleRotation), 
						vehicleIn.getPosY() + seats.get(i).yOffset() + passenger.getYOffset(), vehicleIn.getPosZ() + seats.get(i).zOffset(vehicleIn.vehicleRotation));
				return true;
			}
		}
		return false;
	}
	
	public void switchSeat(Entity passenger) {
		int originalSeat = -1;
		for(int i = 0; i < seats.size() && originalSeat == -1; i++) {
			if(seats.get(i).getPassenger() != null && seats.get(i).getPassenger().equals(passenger)) {
				originalSeat = i;
			}
		}
		if(originalSeat == -1) return;
		for(int i = originalSeat + 1; i < seats.size(); i++) {
			if(!seats.get(i).ocupied()) {
				seats.get(i).takeSeat(passenger, false);
				seats.get(originalSeat).freeSeat();
				return;
			}
		}
		for(int i = 0; i < originalSeat; i++) {
			if(!seats.get(i).ocupied()) {
				seats.get(i).takeSeat(passenger, false);
				seats.get(originalSeat).freeSeat();
				return;
			}
		}
	}
}
