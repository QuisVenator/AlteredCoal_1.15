package com.renepauls.alteredcoal.objects.fluids;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.init.FluidInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.Block.Properties;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class OilFluid {


    // WARNING: this doesn't allow "any fluid", only the fluid from this test mod!
    public static class FluidloggableBlock extends Block implements IWaterLoggable
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
            return !state.get(FLUIDLOGGED) && fluidIn == FluidInit.test_fluid.get();
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
                return FluidInit.test_fluid.get();
            } else {
                return Fluids.EMPTY;
            }
        }

        @Override
        public IFluidState getFluidState(BlockState state)
        {
            return state.get(FLUIDLOGGED) ? FluidInit.test_fluid.get().getDefaultState() : Fluids.EMPTY.getDefaultState();
        }
    }
}
