package com.renepauls.alteredcoal.entities;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SnowMobileEntity extends AnimalEntity{

	double maxVelocity = 0.8;
	
	public SnowMobileEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
		//allow to go up one block
		this.stepHeight = 1.0f;
	}
	
	
	@Override
	public void tick() {
		Entity captain = this.getControllingPassenger();
		if(captain != null) {
			
			//turn vehicle with passenger
			this.setRotation(captain.getYaw(0), captain.getPitch(0));
			
			//get velocity in x and y from orientation of player
			double xMove = Math.sin(Math.toRadians(captain.getRotationYawHead())) * -0.2;
			double zMove = Math.sin(Math.toRadians(captain.getRotationYawHead() + 90)) * 0.2;
			
			//to not get a higher velocity going diagonally (because 2*sin(45) > 1) we normalize the values to max velocity
			double normalizer = maxVelocity / (Math.abs(xMove) + Math.abs(zMove));
			xMove *= normalizer;
			zMove *= normalizer;
			
			//move the vehicle and player, apparently this moves the entity the given distance. 
			//Velocity is NOT a value that persists until changed by aceleration (which would seem reasonable but some names are really messed up...).
			this.addVelocity(xMove, 0, zMove);
		}
			
		super.tick();
	}
	
	//returns a scale that affects hitbox
	@Override
	public float getRenderScale() {
		return 1.0F;
	}
	
	//first passenger in list is (for now) considered the controlling one
	@Override
	@Nullable
	public Entity getControllingPassenger() {
		if(this.getPassengers().size() < 1) return null;
		else return this.getPassengers().get(0);
	}
	
	//mount on right click
	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		if(!world.isRemote)
			player.startRiding(this, false);
		
		return super.processInteract(player, hand);
	}

	//only there because (for now) vehicles inherit from AnimalEntity
	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		// TODO Auto-generated method stub
		return null;
	}

	//overriden to call custom, named function instead of unmapped one!
	@Override
	public void updatePassenger(Entity passenger) {
	   this.func_226266_a_(passenger, Entity::setPosition);
	}
	//does basically the same as unmapped function func_226266_a_ (also adds xOffset
	//gets called from updatePassenger
	public void setPositionPassenger(Entity passenger, Entity.IMoveCallback positionSetter) {
	   if (this.isPassenger(passenger)) {
	      positionSetter.accept(passenger, this.getPosX() + seatOffset(), this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ());
	   }
	}
	
	//should set an offset for the seat TODO get to work
	public float seatOffset() {
		return 0.8f;
	}
	
	@Override
	public double getMountedYOffset() {
		return this.getHeight() * 0.5D;
	}
}
