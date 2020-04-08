package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.objects.blocks.BlockCoritiziumOre;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, AlteredCoal.MOD_ID);
	
	public static final RegistryObject<Block> CORITIZIUM_ORE = BLOCKS.register("coritizium_ore_block", () -> new BlockCoritiziumOre());
}
