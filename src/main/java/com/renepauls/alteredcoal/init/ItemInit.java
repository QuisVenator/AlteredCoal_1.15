package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.AlteredCoal.AlteredCoalGroup;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, AlteredCoal.MOD_ID);

	public static final RegistryObject<Item> STACK = ITEMS.register("stack_item", () -> new Item(new Item.Properties().group(AlteredCoalGroup.instance).maxStackSize(4)));
	public static final RegistryObject<Item> CORITIZIUM_INGOT = ITEMS.register("coritizium_ingot_item", () -> new Item(new Item.Properties().group(AlteredCoalGroup.instance)));
	public static final RegistryObject<Item> CORITIZIUM_NUGGET = ITEMS.register("coritizium_nugget_item", () -> new Item(new Item.Properties().group(AlteredCoalGroup.instance)));
    public static RegistryObject<Item> fluidloggable_blockitem = ITEMS.register("fluidloggable_block", () -> 
    	new BlockItem(BlockInit.fluidloggable_block.get(), new Item.Properties().group(ItemGroup.MISC)));
    public static RegistryObject<Item> test_fluid_bucket = ITEMS.register("test_fluid_bucket", () -> 
    	new BucketItem(FluidInit.test_fluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
}
