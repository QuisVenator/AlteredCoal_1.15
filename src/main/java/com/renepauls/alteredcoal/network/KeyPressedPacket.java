package com.renepauls.alteredcoal.network;

import java.util.function.Supplier;

import com.renepauls.alteredcoal.entities.vehicle.LandVehicleEntity;
import com.renepauls.alteredcoal.util.KeyboardHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

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
				
				if(msg.keyCode == KeyboardHelper.LIGHT_ON.getKey().getKeyCode()) {
    		
    				vehicle.toggleLights();
    			} 
				else if(msg.keyCode == KeyboardHelper.ACCELERATE.getKey().getKeyCode()) {
					vehicle.accelerate();
    			}
				else if(msg.keyCode == KeyboardHelper.SLOW_DOWN.getKey().getKeyCode()) {
					vehicle.decelerate();
				}
				else if(msg.keyCode == KeyboardHelper.TOGGLE_MOUSE_CONTROLS.getKey().getKeyCode()) {
					vehicle.toggleMouseControls();
				}
				else if(msg.keyCode == KeyboardHelper.TURN_LEFT.getKey().getKeyCode()) {
					vehicle.steerLeft();
				}
				else if(msg.keyCode == KeyboardHelper.TURN_RIGHT.getKey().getKeyCode()) {
					vehicle.steerRight();
				}
				else if(msg.keyCode == KeyboardHelper.SWITCH_SEAT.getKey().getKeyCode()) {
					vehicle.seatManager.switchSeat(player);
				}
			}
    	});
    	ctx.get().setPacketHandled(true);
    }
}
