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
        
        eventBus.addListener(this::loadComplete);

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        FLUIDS.register(eventBus);
        
        BlockInit.BLOCKS.register(eventBus);
        ItemInit.ITEMS.register(eventBus);
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
    
    ////////////////////////////////////////////////
    //TODO move all below this line
    ////////////////////////////////////////////////
    public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/brown_mushroom_block");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/mushroom_stem");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation("minecraft:block/obsidian");

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, MOD_ID);

    private static ForgeFlowingFluid.Properties makeProperties()
    {
        return new ForgeFlowingFluid.Properties(test_fluid, test_fluid_flowing,
                FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).overlay(FLUID_OVERLAY).color(0x3F1080FF))
                .bucket(test_fluid_bucket).block(test_fluid_block);
    }

    public static RegistryObject<FlowingFluid> test_fluid = FLUIDS.register("test_fluid", () ->
            new ForgeFlowingFluid.Source(makeProperties())
    );
    public static RegistryObject<FlowingFluid> test_fluid_flowing = FLUIDS.register("test_fluid_flowing", () ->
            new ForgeFlowingFluid.Flowing(makeProperties())
    );

    public static RegistryObject<FlowingFluidBlock> test_fluid_block = BLOCKS.register("test_fluid_block", () ->
            new FlowingFluidBlock(test_fluid, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
    );
    public static RegistryObject<Item> test_fluid_bucket = ITEMS.register("test_fluid_bucket", () ->
            new BucketItem(test_fluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC))
    );

    // WARNING: this doesn't allow "any fluid", only the fluid from this test mod!
    public static RegistryObject<Block> fluidloggable_block = BLOCKS.register("fluidloggable_block", () ->
            new FluidloggableBlock(Block.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
    );
    public static RegistryObject<Item> fluidloggable_blockitem = ITEMS.register("fluidloggable_block", () ->
            new BlockItem(fluidloggable_block.get(), new Item.Properties().group(ItemGroup.MISC))
    );
    
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        // some sanity checks
        BlockState state = Fluids.WATER.getDefaultState().getBlockState();
        BlockState state2 = Fluids.WATER.getAttributes().getBlock(null,null,Fluids.WATER.getDefaultState());
        Validate.isTrue(state.getBlock() == Blocks.WATER && state2 == state);
        ItemStack stack = Fluids.WATER.getAttributes().getBucket(new FluidStack(Fluids.WATER, 1));
        Validate.isTrue(stack.getItem() == Fluids.WATER.getFilledBucket());
    }

    // WARNING: this doesn't allow "any fluid", only the fluid from this test mod!
    private static class FluidloggableBlock extends Block implements IWaterLoggable
    {
        public static final BooleanProperty FLUIDLOGGED = BooleanProperty.create("fluidlogged");

        public FluidloggableBlock(Properties properties)
        {
            super(properties);
            setDefaultState(getStateContainer().getBaseState().with(FLUIDLOGGED, false));
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
        {
            builder.add(FLUIDLOGGED);
        }

        @Override
        public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
            return !state.get(FLUIDLOGGED) && fluidIn == test_fluid.get();
        }

        @Override
        public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
            if (canContainFluid(worldIn, pos, state, fluidStateIn.getFluid())) {
                if (!worldIn.isRemote()) {
                    worldIn.setBlockState(pos, state.with(FLUIDLOGGED, true), 3);
                    worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
                }

                return true;
            } else {
                return false;
            }
        }

        @Override
        public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
            if (state.get(FLUIDLOGGED)) {
                worldIn.setBlockState(pos, state.with(FLUIDLOGGED, false), 3);
                return test_fluid.get();
            } else {
                return Fluids.EMPTY;
            }
        }

        @Override
        public IFluidState getFluidState(BlockState state)
        {
            return state.get(FLUIDLOGGED) ? test_fluid.get().getDefaultState() : Fluids.EMPTY.getDefaultState();
        }
    }
}
