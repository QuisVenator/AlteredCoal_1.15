package com.renepauls.alteredcoal.network;

import java.util.function.Supplier;

import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;
import com.renepauls.alteredcoal.util.KeyboardHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class VehicleSteeringRotationPacket {
	public int degree;
	
    public VehicleSteeringRotationPacket(int degree) {
    	this.degree = degree;
    }

    public static VehicleSteeringRotationPacket decode(ByteBuf buf) {
    	return new VehicleSteeringRotationPacket(buf.readInt());
    }

    public static void encode(VehicleSteeringRotationPacket msg, ByteBuf buf) {
    	buf.writeInt(msg.degree);
    }

    public static void handle(final VehicleSteeringRotationPacket msg, Supplier<NetworkEvent.Context> ctx) {
    	ctx.get().enqueueWork(() -> {
    		
			PlayerEntity player = ctx.get().getSender();
			Entity entity = player.getRidingEntity();
    	});
    	ctx.get().setPacketHandled(true);
    }
}
