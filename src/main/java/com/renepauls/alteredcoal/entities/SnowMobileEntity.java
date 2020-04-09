package com.renepauls.alteredcoal.entities;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.renepauls.alteredcoal.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class SnowMobileEntity extends AnimalEntity{

	private double maxVelocity = 0.8;
	private double currentVelocity = 0;
	private final double acceleration = 0.007;
	private final double deceleration = 0.02;
	public boolean mouseControlsEnabled = true;
	private Vec3d curLightPosition[] = new Vec3d[21];
	private BlockPos curLightPos;
	private boolean lightsOn = false;
	
	public SnowMobileEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
		super(type, worldIn);
		//allow to go up one block
		this.stepHeight = 1.0f;
	}
	
	
	@Override
	public void tick() {
		if(!this.world.isRemote) {
			if(lightsOn) this.shineLights();

			Entity captain = this.getControllingPassenger();
			if(captain != null && mouseControlsEnabled) {
				//turn vehicle with passenger
				this.setRotation(captain.getYaw(0), captain.getPitch(0));
			} else if(captain == null && currentVelocity > 0) {
				decelerate();
			}
			
			//get velocity in x and y from orientation of player
			double xMove = Math.sin(Math.toRadians(this.getRotationYawHead())) * -1;
			double zMove = Math.sin(Math.toRadians(this.getRotationYawHead() + 90));
			
			//TODO fix (pythagoras)
			//to not get a higher velocity going diagonally (because 2*sin(45) > 1) we normalize the values to velocity
			double normalizer = currentVelocity / Math.sqrt(Math.pow(xMove, 2) + Math.pow(zMove, 2));
			xMove *= normalizer;
			zMove *= normalizer;
			
			//move the vehicle and player, apparently this moves the entity the given distance. 
			//Velocity is NOT a value that persists until changed by aceleration (which would seem reasonable but some names are really messed up...).
			this.addVelocity(xMove, 0, zMove);
		}
			
		super.tick();
	}
	
	public void decelerate() {
		currentVelocity = Math.max(currentVelocity - deceleration, 0.0d);
	}
	
	public void accelerate() {
		currentVelocity = Math.min(currentVelocity + acceleration, maxVelocity);
	}
	
	public void toggleLights() {
		lightsOn = ! lightsOn;
		if(!lightsOn) {
			for(int i = 20; i >= 0; i--) {
				if(curLightPosition[i] != null) {
					if(this.world.getBlockState(new BlockPos(curLightPosition[i])).getBlock().getRegistryName() == BlockInit.INVISIBLE_LIGHT.get().getRegistryName()) {
						this.world.setBlockState(new BlockPos(curLightPosition[i]), Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}
	
	public void toggleMouseControls() {
		mouseControlsEnabled = !mouseControlsEnabled;
	}
	
	protected void shineLights() {
		int solidBlockEncountered = -1;
		for(int i = 20; i >= 0; i--) {

			if(curLightPosition[i] != null) {
				if(this.world.getBlockState(new BlockPos(curLightPosition[i])).getBlock().getRegistryName() == BlockInit.INVISIBLE_LIGHT.get().getRegistryName()) {
					this.world.setBlockState(new BlockPos(curLightPosition[i]), Blocks.AIR.getDefaultState());
				}
			}
			
			curLightPosition[i] = this.getPositionVec();
			double xMove = Math.sin(Math.toRadians(this.getRotationYawHead())) * (-i+2);
			double zMove = Math.sin(Math.toRadians(this.getRotationYawHead() + 90)) * (i-2);
			curLightPosition[i] = curLightPosition[i].add(xMove, 1, zMove);

			curLightPos = new BlockPos(curLightPosition[i]);
			if(this.world.getBlockState(curLightPos).isAir(this.world, curLightPos)) {
				System.out.println(this.world.getBlockState(curLightPos).getBlock().getRegistryName() + " == " + Blocks.AIR.getRegistryName() + " is " +
						(this.world.getBlockState(curLightPos).getBlock().getRegistryName() == Blocks.AIR.getRegistryName()));
				if(this.world.getBlockState(curLightPos).getBlock().getRegistryName() == Blocks.AIR.getRegistryName()) {
					this.world.setBlockState(curLightPos, BlockInit.INVISIBLE_LIGHT.get().getDefaultState());
				}
			} else {
				solidBlockEncountered = i;
			}
		}
		if(solidBlockEncountered >= 0) {
			for(int i = solidBlockEncountered; i <= 20; i++) {
				if(curLightPosition[i] != null) {
					if(this.world.getBlockState(new BlockPos(curLightPosition[i])).getBlock().getRegistryName() == BlockInit.INVISIBLE_LIGHT.get().getRegistryName()) {
						this.world.setBlockState(new BlockPos(curLightPosition[i]), Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}
	
	
	//returns a scale that affects hitbox
	@Override
	public float getRenderScale() {
		return 1.3F;
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
