package com.renepauls.alteredcoal.entities;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SimpleCarEntity extends AnimalEntity{

	double maxVelocity = 0.2;
	
	public SimpleCarEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
		this.stepHeight = 1.0f;
	}
	
	@Override
	public void tick() {
		Entity captain = this.getControllingPassenger();
		if(captain != null) {
			this.setRotation(captain.getYaw(0), captain.getPitch(0));
			double xMove = Math.sin(Math.toRadians(captain.getRotationYawHead())) * -0.2;
			double zMove = Math.sin(Math.toRadians(captain.getRotationYawHead() + 90)) * 0.2;
			double normalizer = maxVelocity / (Math.abs(xMove) + Math.abs(zMove));
			xMove *= normalizer;
			zMove *= normalizer;
			this.addVelocity(xMove, 0, zMove);
		}
			
		super.tick();
	}
	
	@Override
	@Nullable
	public Entity getControllingPassenger() {
		if(this.getPassengers().size() < 1) return null;
		else return this.getPassengers().get(0);
	}
	
	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		if(!world.isRemote)
			player.startRiding(this, false);
		
		return super.processInteract(player, hand);
	}

	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
