package com.renepauls.alteredcoal.network;

import java.util.function.Supplier;

import com.renepauls.alteredcoal.entities.vehicle.LandVehicleEntity;
import com.renepauls.alteredcoal.util.KeyboardHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class KeyPressedPacket {
	public int keyCode;
	
    public KeyPressedPacket(int keyCode) {
    	this.keyCode = keyCode;
    }

    public static KeyPressedPacket decode(ByteBuf buf) {
    	return new KeyPressedPacket(buf.readInt());
    }

    public static void encode(KeyPressedPacket msg, ByteBuf buf) {
    	buf.writeInt(msg.keyCode);
    }

    public static void handle(final KeyPressedPacket msg, Supplier<NetworkEvent.Context> ctx) {
    	System.out.println("Received package...");
    	ctx.get().enqueueWork(() -> {
    		
			PlayerEntity player = ctx.get().getSender();
			Entity entity = player.getRidingEntity();
    	
			if(entity instanceof LandVehicleEntity) {
				LandVehicleEntity vehicle = (LandVehicleEntity)entity;
				
				//TODO implement checks for it actually being the captain
				switch(msg.keyCode) {
				case 0:
					vehicle.accelerate();
					break;
				case 1:
					vehicle.decelerate();
					break;
				case 2:
					vehicle.steerLeft();
					break;
				case 3:
					vehicle.steerRight();
					break;
				case 4:
					//ascend
					break;
				case 5:
					//descend
					break;
				case 100:
					vehicle.toggleLights();
					break;
				case 101:
					vehicle.toggleMouseControls();
					break;
				case 102:
					vehicle.seatManager.switchSeat(player);
					break;
				case 103:
					System.out.println("Opening inventory");
					if(vehicle.hasInventory()) {
						NetworkHooks.openGui((ServerPlayerEntity)player, vehicle);	
					}
					break;
				}
			}
    	});
    	ctx.get().setPacketHandled(true);
    }
}
