package com.renepauls.alteredcoal.entities;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.renepauls.alteredcoal.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityEquipmentPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//TODO make mobEntity copy to test which methods are important
public class SnowMobileEntity extends LivingEntity{

	public boolean mouseControlsEnabled = false;
	public float vehicleRotation, prevVehicleRotation;
	private static final DataParameter<Float> steerRotation = EntityDataManager.createKey(SnowMobileEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> mouseControlEnabled = EntityDataManager.createKey(SnowMobileEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> rotation = EntityDataManager.createKey(SnowMobileEntity.class, DataSerializers.FLOAT);
	protected float prevSteerRotation = 0f, curSteerRotation = 0f;
	private double maxVelocity = 0.8;
	private double currentVelocity = 0;
	private final double acceleration = 0.007;
	private final double deceleration = 0.02;
	private Vec3d curLightPosition[] = new Vec3d[21];
	private BlockPos curLightPos;
	private boolean lightsOn = false;
	
	public SnowMobileEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
		//allow to go up one block
		this.stepHeight = 1.0f;
		
		vehicleRotation = prevVehicleRotation = 0;

		dataManager.register(steerRotation, 0f);
		dataManager.register(mouseControlEnabled, false);
		dataManager.register(rotation, this.rotationYaw);
	}
	
	
	@Override
	public void tick() {
		if(!this.world.isRemote) {
			if(lightsOn) this.shineLights();
			
			Entity captain = this.getControllingPassenger();
			if(captain != null && captain instanceof ServerPlayerEntity && !dataManager.get(mouseControlEnabled)) {
				//((ServerPlayerEntity)captain).rotationYaw = this.vehicleRotation;
				//((ServerPlayerEntity)captain).setPositionAndUpdate(x, y, z);
			}
			if(captain != null && dataManager.get(mouseControlEnabled)) {
				//turn vehicle with passenger
				dataManager.set(steerRotation, captain.rotationYaw - this.vehicleRotation);
			} else if(captain == null && currentVelocity > 0) {
				decelerate();
			}
			
			if(currentVelocity > 0.01) {
				if(dataManager.get(steerRotation) > 6f) {
					dataManager.set(rotation, this.vehicleRotation + 6f);
					dataManager.set(steerRotation, dataManager.get(steerRotation) - 6f);
				} else if(dataManager.get(steerRotation) < -6f) {
					dataManager.set(rotation, this.vehicleRotation - 6f);
					dataManager.set(steerRotation, dataManager.get(steerRotation) + 6f);
				} else {
					dataManager.set(rotation, this.vehicleRotation + getSteerRotation());
					dataManager.set(steerRotation, 0f);
				}
			}
			
			//get velocity in x and y from orientation of player
			double xMove = Math.sin(Math.toRadians(this.vehicleRotation)) * -1;
			double zMove = Math.sin(Math.toRadians(this.vehicleRotation + 90));
			
			//to not get a higher velocity going diagonally (because 2*sin(45) > 1) we normalize the values to velocity
			double normalizer = currentVelocity / Math.sqrt(Math.pow(xMove, 2) + Math.pow(zMove, 2));
			xMove *= normalizer;
			zMove *= normalizer;
			
			//move the vehicle and player, apparently this moves the entity the given distance. 
			//Velocity is NOT a value that persists until changed by aceleration (which would seem reasonable but some names are really messed up...).
			//this.addVelocity(xMove, 0, zMove);
			if(this.onGround)
				this.addVelocity(xMove, 0, zMove);
		} else {
		}
		prevSteerRotation = curSteerRotation;
		curSteerRotation = getSteerRotation();
		this.prevVehicleRotation = this.vehicleRotation;
		this.vehicleRotation = dataManager.get(rotation) % 360;
		
		super.tick();
		
		//copied tick
		
		//end copied tick
	}
	
	public void decelerate() {
		currentVelocity = Math.max(currentVelocity - deceleration, 0.0d);
	}
	
	public void accelerate() {
		System.out.println("accelerating");
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

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
	   if (mouseControlEnabled.equals(key)) {
	      mouseControlsEnabled = dataManager.get(mouseControlEnabled);
	   }

	}
	
	public void toggleMouseControls() {
		dataManager.set(mouseControlEnabled, !dataManager.get(mouseControlEnabled));
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
			double xMove = Math.sin(Math.toRadians(this.vehicleRotation + getSteerRotation())) * (-i+2);
			double zMove = Math.sin(Math.toRadians(this.vehicleRotation + getSteerRotation() + 90)) * (i-2);
			curLightPosition[i] = curLightPosition[i].add(xMove, 1, zMove);

			curLightPos = new BlockPos(curLightPosition[i]);
			if(this.world.getBlockState(curLightPos).isAir(this.world, curLightPos)) {
				//System.out.println(this.world.getBlockState(curLightPos).getBlock().getRegistryName() + " == " + Blocks.AIR.getRegistryName() + " is " +
				//		(this.world.getBlockState(curLightPos).getBlock().getRegistryName() == Blocks.AIR.getRegistryName()));
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
	
	public void steerLeft() {
		if(dataManager.get(steerRotation) > -15)
			dataManager.set(steerRotation, getSteerRotation() - 6f);
	}
	public void steerRight() {
		if(dataManager.get(steerRotation) < 15)
			dataManager.set(steerRotation, getSteerRotation() + 6f);
	}

	public float getSteerRotation() {
		return dataManager.get(steerRotation);
	}
	public float getPrevSteerRotation() {
		return prevSteerRotation;
	}
	public float getCurSteerRotation() {
		return curSteerRotation;
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
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		if(!world.isRemote)
			player.startRiding(this, false);
		
		return true;
		//return super.processInteract(player, hand);
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


	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		// TODO Auto-generated method stub
		return new ArrayList<ItemStack>();
	}


	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		// TODO Auto-generated method stub
		return ItemStack.EMPTY;
	}


	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public HandSide getPrimaryHand() {
		// TODO Auto-generated method stub
		return HandSide.LEFT;
	}
	
	@Override
	public boolean canPassengerSteer() {
		return false;
	}
}
