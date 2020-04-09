package com.renepauls.alteredcoal.network;

import com.renepauls.alteredcoal.AlteredCoal;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(AlteredCoal.MOD_ID, "vehicle_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	public static void register()
	{
		int msgId = 0;

		INSTANCE.registerMessage(msgId++, KeyPressedPacket.class, KeyPressedPacket::encode, KeyPressedPacket::decode, KeyPressedPacket::handle);
	}
}
