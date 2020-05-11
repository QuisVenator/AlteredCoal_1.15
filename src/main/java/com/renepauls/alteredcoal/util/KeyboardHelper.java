package com.renepauls.alteredcoal.util;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.entities.vehicle.LandVehicleEntity;
import com.renepauls.alteredcoal.network.KeyPressedPacket;
import com.renepauls.alteredcoal.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = AlteredCoal.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class KeyboardHelper 
{
	public static ArrayList<KeyBinding> HOLD_KEYS = new ArrayList<>();
	public static ArrayList<KeyBinding> TOGGLE_KEYS = new ArrayList<>();
	public static final KeyBinding LIGHT_ON = new KeyBinding(AlteredCoal.MOD_ID+".key.toggle_lights", GLFW.GLFW_KEY_L, "key.categories.vehicles");
	public static final KeyBinding ACCELERATE = new KeyBinding(AlteredCoal.MOD_ID+".key.accelerate", GLFW.GLFW_KEY_W, "key.categories.vehicles");
	public static final KeyBinding SLOW_DOWN = new KeyBinding(AlteredCoal.MOD_ID+".key.slow_down", GLFW.GLFW_KEY_S, "key.categories.vehicles");
	public static final KeyBinding TURN_LEFT = new KeyBinding(AlteredCoal.MOD_ID+".key.turn_left", GLFW.GLFW_KEY_A, "key.categories.vehicles");
	public static final KeyBinding TURN_RIGHT = new KeyBinding(AlteredCoal.MOD_ID+".key.turn_right", GLFW.GLFW_KEY_D, "key.categories.vehicles");
	public static final KeyBinding ASCEND = new KeyBinding(AlteredCoal.MOD_ID+".key.ascend", GLFW.GLFW_KEY_SPACE, "key.categories.vehicles");
	public static final KeyBinding DESCEND = new KeyBinding(AlteredCoal.MOD_ID+".key.descend", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories.vehicles");
	public static final KeyBinding TOGGLE_MOUSE_CONTROLS = new KeyBinding(AlteredCoal.MOD_ID+".key.toggle_mouse_control", GLFW.GLFW_MOUSE_BUTTON_3, "key.categories.vehicles");
	public static final KeyBinding SWITCH_SEAT = new KeyBinding(AlteredCoal.MOD_ID+".key.switch_seat", GLFW.GLFW_KEY_C, "key.categories.vehicles");
	public static final KeyBinding OPEN_INVENTORY = new KeyBinding(AlteredCoal.MOD_ID+".key.open_inventory", GLFW.GLFW_KEY_F, "key.categories.vehicles");
	
	public static void collectAndRegister() {
		HOLD_KEYS.add(ACCELERATE);
		HOLD_KEYS.add(SLOW_DOWN);
		HOLD_KEYS.add(TURN_LEFT);
		HOLD_KEYS.add(TURN_RIGHT);
		HOLD_KEYS.add(ASCEND);
		HOLD_KEYS.add(DESCEND);
		
		TOGGLE_KEYS.add(LIGHT_ON);
		TOGGLE_KEYS.add(TOGGLE_MOUSE_CONTROLS);
		TOGGLE_KEYS.add(SWITCH_SEAT);
		TOGGLE_KEYS.add(OPEN_INVENTORY);

		for(int i = 0; i < HOLD_KEYS.size(); i++) {
			ClientRegistry.registerKeyBinding(HOLD_KEYS.get(i));
		}
		for(int i = 0; i < TOGGLE_KEYS.size(); i++) {
			ClientRegistry.registerKeyBinding(TOGGLE_KEYS.get(i));
		}
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.getRidingEntity() instanceof LandVehicleEntity) {
			for(int i = 0; i < HOLD_KEYS.size(); i++) {		
				if(HOLD_KEYS.get(i).isKeyDown()) {
					PacketHandler.INSTANCE.sendToServer(new KeyPressedPacket(i));
				}
			}
			for(int i = 0; i < TOGGLE_KEYS.size(); i++) {		
				if(TOGGLE_KEYS.get(i).isPressed()) {
					PacketHandler.INSTANCE.sendToServer(new KeyPressedPacket(i+100));
					if(TOGGLE_KEYS.get(i) == SWITCH_SEAT) {
						//TODO make work on packages. basically the server should keep seats synchronized between all players
						((LandVehicleEntity) Minecraft.getInstance().player.getRidingEntity()).seatManager.switchSeat(Minecraft.getInstance().player);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void renderEvent(RenderGameOverlayEvent.Pre event) {
		if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.getRidingEntity() instanceof LandVehicleEntity) {
			if(!((LandVehicleEntity)Minecraft.getInstance().player.getRidingEntity()).mouseControlsEnabled)
			Minecraft.getInstance().getRenderViewEntity().rotationYaw = ((LandVehicleEntity)Minecraft.getInstance().player.getRidingEntity()).vehicleRotation;
		}
	}
}