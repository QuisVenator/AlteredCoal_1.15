package com.renepauls.alteredcoal.init;

import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.AlteredCoal.AlteredCoalGroup;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, AlteredCoal.MOD_ID);

	public static final RegistryObject<Item> STACK = ITEMS.register("stack_item", () -> new Item(new Item.Properties().group(AlteredCoalGroup.instance).maxStackSize(4)));
	public static final RegistryObject<Item> CORITIZIUM_INGOT = ITEMS.register("coritizium_ingot_item", () -> new Item(new Item.Properties().group(AlteredCoalGroup.instance)));
	public static final RegistryObject<Item> CORITIZIUM_NUGGET = ITEMS.register("coritizium_nugget_item", () -> new Item(new Item.Properties().group(AlteredCoalGroup.instance)));
}
