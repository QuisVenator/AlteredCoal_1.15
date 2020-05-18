package com.renepauls.alteredcoal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;
import com.renepauls.alteredcoal.init.BlockInit;
import com.renepauls.alteredcoal.init.FluidInit;
import com.renepauls.alteredcoal.init.GuiInit;
import com.renepauls.alteredcoal.init.ItemInit;
import com.renepauls.alteredcoal.init.ModEntityTypes;
import com.renepauls.alteredcoal.init.SoundInit;
import com.renepauls.alteredcoal.network.PacketHandler;
import com.renepauls.alteredcoal.objects.fluids.OilFluid.FluidloggableBlock;
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

        BlockInit.BLOCKS_WITH_ITEM.register(eventBus);
        BlockInit.BLOCKS_WITHOUT_ITEM.register(eventBus);
        ItemInit.ITEMS.register(eventBus);
        FluidInit.FLUIDS.register(eventBus);
        ModEntityTypes.ENTITY_TYPES.register(eventBus);
        GuiInit.CONTAINER_TYPES.register(eventBus);
        SoundInit.SOUNDS.register(eventBus);
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
        	BlockInit.BLOCKS_WITH_ITEM.getEntries().stream().map(RegistryObject::get).forEach(block -> {
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
