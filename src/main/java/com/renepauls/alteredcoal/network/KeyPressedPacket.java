package com.renepauls.alteredcoal.network;

import java.util.function.Supplier;

import com.renepauls.alteredcoal.entities.SnowMobileEntity;
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
    	
			if(entity instanceof SnowMobileEntity) {
				SnowMobileEntity vehicle = (SnowMobileEntity)entity;
				
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
			}
    	});
    	ctx.get().setPacketHandled(true);
    }
}
