package com.renepauls.alteredcoal.objects.blocks;

import com.renepauls.alteredcoal.init.BlockInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class BlockCoritiziumOre extends Block {
	public BlockCoritiziumOre() {
		super(Block.Properties.create(Material.ROCK)
				.harvestLevel(3)
				.harvestTool(ToolType.PICKAXE)
				.hardnessAndResistance(2.0F, 6.0F)
				.lightValue(2));
	}
}
