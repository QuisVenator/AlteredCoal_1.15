package com.renepauls.alteredcoal.objects.gui.containers;


import java.util.ArrayList;

import com.renepauls.alteredcoal.entities.vehicle.BaseTruckEntity;
import com.renepauls.alteredcoal.init.GuiInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BaseTruckContainer extends Container  {
	

	public BaseTruckContainer(final int windowId, final PlayerInventory playerInventory, final BaseTruckEntity truck) {
		super(GuiInit.BASE_TRUCK_CONTAINER.get(), windowId);
		int x = 8;
		int y = 18;
		int slotSizeX = 18;
		int slotSizeY = 18;
		int playerMainOffset = 140;
		int playerHotbarOffset = 198;
		
		for(int row = 0; row < 6; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new SlotItemHandler(truck.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(null), row * 9 + col, x + col * slotSizeX, y + row * slotSizeY));
			}
		}
		
		for(int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, row * 9 + col + 9, x + col * slotSizeX, playerMainOffset + row * slotSizeY));
			}
		}
		
		for(int row = 0; row < 1; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory,  col, x + col * slotSizeX, playerHotbarOffset + row * slotSizeY));
			}
		}
	}
	
	public BaseTruckContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer packetBuffer) {
		this(windowId, playerInventory, (BaseTruckEntity)playerInventory.player.getRidingEntity());
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		// TODO Set real conditions
		return true;
	}
}
