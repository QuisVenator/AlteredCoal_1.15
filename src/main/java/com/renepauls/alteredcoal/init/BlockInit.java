package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.objects.blocks.BlockCoritiziumOre;
import com.renepauls.alteredcoal.objects.blocks.BlockInvisibleLight;
import com.renepauls.alteredcoal.objects.fluids.OilFluid.FluidloggableBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS_WITH_ITEM = new DeferredRegister<>(ForgeRegistries.BLOCKS, AlteredCoal.MOD_ID);
	public static final DeferredRegister<Block> BLOCKS_WITHOUT_ITEM = new DeferredRegister<>(ForgeRegistries.BLOCKS, AlteredCoal.MOD_ID);

	public static final RegistryObject<Block> CORITIZIUM_ORE = BLOCKS_WITH_ITEM.register("coritizium_ore_block", () -> new BlockCoritiziumOre());
	public static final RegistryObject<Block> INVISIBLE_LIGHT = BLOCKS_WITH_ITEM.register("invisible_light_block", () -> new BlockInvisibleLight());
	
    public static RegistryObject<FlowingFluidBlock> test_fluid_block = BLOCKS_WITHOUT_ITEM.register("test_fluid_block", () ->
            new FlowingFluidBlock(FluidInit.test_fluid, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
    );
    public static RegistryObject<Block> fluidloggable_block = BLOCKS_WITHOUT_ITEM.register("fluidloggable_block", () ->
            new FluidloggableBlock(Block.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
    );
}
