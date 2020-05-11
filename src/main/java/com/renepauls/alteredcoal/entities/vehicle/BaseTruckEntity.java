package com.renepauls.alteredcoal.entities.vehicle;

import com.renepauls.alteredcoal.objects.gui.containers.BaseTruckContainer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseTruckEntity extends LandVehicleEntity {

	public BaseTruckEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
		stepHeight = 0.6f;
		steerDegree = 3f;
		maxSteer = 10f;

		seatManager.addDriverSeat(2.1f, 1.0f, 1.7f);
		seatManager.addSeat(2.1f, -1.0f, 1.7f);
		
		this.itemHandler.orElseThrow(null).setSize(54);
		
		for(int i = 0; i < 54; i++) {
			this.inventorySlots.add(ItemStack.EMPTY);
		}
	}
	
	@Override
	public float getRenderScale() {
		return 1.8F;
	}

	@Override
	public double getMountedYOffset() {
		return 2f;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new BaseTruckContainer(id, inventory, (BaseTruckEntity) this);
	}
	
	@Override
	public boolean hasInventory() {
		return true;
	}
}
