package com.renepauls.alteredcoal.entities;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

public class BetaCarEntity extends AnimalEntity{

	public BetaCarEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		player.startRiding(this, false);
		
		return super.processInteract(player, hand);
	}

	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
