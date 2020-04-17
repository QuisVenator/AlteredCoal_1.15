package com.renepauls.alteredcoal;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;
import com.renepauls.alteredcoal.init.BlockInit;
import com.renepauls.alteredcoal.init.ItemInit;
import com.renepauls.alteredcoal.init.ModEntityTypes;
import com.renepauls.alteredcoal.network.PacketHandler;
import com.renepauls.alteredcoal.util.KeyboardHelper;

@Mod("alteredcoal")
public class AlteredCoal
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final String MOD_ID = "alteredcoal";
    public static AlteredCoal instance;

    public AlteredCoal() {
    	final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
    	eventBus.addListener(this::setup);
        // Register the doClientStuff method for modloading
    	eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        instance = this;
        
        ItemInit.ITEMS.register(eventBus);
        BlockInit.BLOCKS.register(eventBus);
        ModEntityTypes.ENTITY_TYPES.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
    	PacketHandler.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	KeyboardHelper.collectAndRegister();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        //when items are registered, for each block create BlockItem and register
        @SubscribeEvent
        public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        	final IForgeRegistry<Item> registry = event.getRegistry();
        	BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
        		registry.register(new BlockItem(block, new Item.Properties().group(AlteredCoalGroup.instance)).setRegistryName(block.getRegistryName()));
        	});
        }
    }
    
    public static class AlteredCoalGroup extends ItemGroup {
        public static final AlteredCoalGroup instance = new AlteredCoalGroup(ItemGroup.GROUPS.length, "actab");
        		
    	private AlteredCoalGroup(int index, String label) {
    		super(index, label);
    	}
    	
    	@Override
    	public ItemStack createIcon() {
    		return new ItemStack(ItemInit.STACK.get());
    	}
    }
}
