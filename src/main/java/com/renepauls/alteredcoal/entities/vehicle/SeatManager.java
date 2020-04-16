package com.renepauls.alteredcoal.entities.vehicle;

import java.util.ArrayList;

import com.renepauls.alteredcoal.entities.SnowMobileEntity;

import net.minecraft.entity.Entity;

public class SeatManager {
	private SnowMobileEntity parent;
	private ArrayList<Seat> seats = new ArrayList<>();
	
	public SeatManager(SnowMobileEntity parent) {
		this.parent = parent;
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
				float distance = seats.get(i).distance(parent, entityIn.getPositionVec());
				if(distance < closest) {
					closest = distance;
					closestIndex = i;
				}
			}
		}
		if(closestIndex > -1) { 
			return seats.get(closestIndex).takeSeat(parent, false);
		}
		return false;
	}
}
