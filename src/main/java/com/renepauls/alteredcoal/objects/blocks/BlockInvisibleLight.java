package com.renepauls.alteredcoal.objects.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInvisibleLight extends AirBlock {

	public BlockInvisibleLight() {
		super(Block.Properties.create(Material.AIR).lightValue(15).sound(null));
	}
	
}
