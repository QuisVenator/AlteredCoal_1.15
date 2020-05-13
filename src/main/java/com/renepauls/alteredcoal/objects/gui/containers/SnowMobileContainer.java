package com.renepauls.alteredcoal.objects.gui.containers;


import java.util.ArrayList;

import com.renepauls.alteredcoal.entities.vehicle.BaseTruckEntity;
import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;
import com.renepauls.alteredcoal.init.GuiInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SnowMobileContainer extends Container  {
	

	public SnowMobileContainer(final int windowId, final PlayerInventory playerInventory, final SnowMobileEntity vehicle) {
		super(GuiInit.SNOW_MOBILE_CONTAINER.get(), windowId);
		int x1 = 26;
		int x2 = 116;
		int y = 18;
		int slotSizeX = 18;
		int slotSizeY = 18;
		int playerX = 8;
		int playerMainOffset = 82;
		int playerHotbarOffset = 140;
		
		for(int row = 0; row < 3; row++) {
			for (int col = 0; col < 2; col++) {
				this.addSlot(new SlotItemHandler(vehicle.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(null), row * 2 + col, x1 + col * slotSizeX, y + row * slotSizeY));
				this.addSlot(new SlotItemHandler(vehicle.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(null), row * 2 + col + 6, x2 + col * slotSizeX, y + row * slotSizeY));
			}
		}
		
		for(int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, row * 9 + col + 9, playerX + col * slotSizeX, playerMainOffset + row * slotSizeY));
			}
		}
		
		for(int row = 0; row < 1; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory,  col, playerX + col * slotSizeX, playerHotbarOffset + row * slotSizeY));
			}
		}
	}
	
	public SnowMobileContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer packetBuffer) {
		this(windowId, playerInventory, (SnowMobileEntity)playerInventory.player.getRidingEntity());
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		// TODO Set real conditions
		return true;
	}
}
