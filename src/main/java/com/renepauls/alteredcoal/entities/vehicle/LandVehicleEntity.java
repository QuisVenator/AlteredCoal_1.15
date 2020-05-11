package com.renepauls.alteredcoal.entities.vehicle;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.renepauls.alteredcoal.init.BlockInit;
import com.renepauls.alteredcoal.init.SoundInit;
import com.renepauls.alteredcoal.objects.gui.containers.BaseTruckContainer;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityEquipmentPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class LandVehicleEntity extends LivingEntity implements INamedContainerProvider {

	public boolean mouseControlsEnabled = false;
	public float vehicleRotation, prevVehicleRotation;
	protected static final DataParameter<Float> steerRotation = EntityDataManager.createKey(LandVehicleEntity.class, DataSerializers.FLOAT);
	protected static final DataParameter<Boolean> mouseControlEnabled = EntityDataManager.createKey(LandVehicleEntity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Float> rotation = EntityDataManager.createKey(LandVehicleEntity.class, DataSerializers.FLOAT);
	protected float prevSteerRotation = 0f, curSteerRotation = 0f;
	protected double maxVelocity = 0.8;
	protected double currentVelocity = 0;
	protected final double acceleration = 0.007;
	protected final double deceleration = 0.02;
	protected Vec3d curLightPosition[] = new Vec3d[21];
	protected BlockPos curLightPos;
	protected boolean lightsOn = false;
	public float maxSteer = 15;
	public float steerDegree = 6;
	public SeatManager seatManager;
	protected ArrayList<ItemStack> inventorySlots = new ArrayList<ItemStack>();
	protected LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(ItemStackHandler::new);
	
	public LandVehicleEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
		
		seatManager = new SeatManager(this);
		
		vehicleRotation = prevVehicleRotation = 0;

		dataManager.register(steerRotation, 0f);
		dataManager.register(mouseControlEnabled, false);
		dataManager.register(rotation, this.rotationYaw);
	}
	
	//give access to item handler to other classes (for example for dropping items on killing)
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability) {
	  if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
	    return itemHandler.cast();
	  }
	  return super.getCapability(capability);
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
				if(dataManager.get(steerRotation) > steerDegree) {
					dataManager.set(rotation, this.vehicleRotation + steerDegree);
					dataManager.set(steerRotation, dataManager.get(steerRotation) - steerDegree);
				} else if(dataManager.get(steerRotation) < -steerDegree) {
					dataManager.set(rotation, this.vehicleRotation - steerDegree);
					dataManager.set(steerRotation, dataManager.get(steerRotation) + steerDegree);
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
		if(dataManager.get(steerRotation) > -maxSteer)
			dataManager.set(steerRotation, getSteerRotation() - steerDegree);
	}
	public void steerRight() {
		if(dataManager.get(steerRotation) < maxSteer)
			dataManager.set(steerRotation, getSteerRotation() + steerDegree);
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
	
	public boolean hasInventory() {
		return false;
	}
	
	//first passenger in list is (for now) considered the controlling one
	@Override
	@Nullable
	public Entity getControllingPassenger() {
		if(this.getPassengers().size() < 1) return null;
		else return seatManager.getControllingPassenger();
	}
	
	//mount on right click
	//TODO make tools repair, etc.
	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		if(!world.isRemote) {
			//TODO remove comented code
			//NetworkHooks.openGui((ServerPlayerEntity) player, this);
			player.startRiding(this, false);
		}
		
		return true;
	}
	
	@Override
	public boolean canFitPassenger(Entity passenger) {
		return !seatManager.isFull();
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		seatManager.takeSeat(passenger);
	}
	
	@Override
	public boolean isPassenger(Entity passenger) {
		return seatManager.isPassenger(passenger);
	}

	@Override
	public void updatePassenger(Entity passenger) {
		seatManager.setPositionPassenger(passenger, Entity::setPosition);
	}
	
	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		seatManager.removePassenger(passenger);
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		System.out.println("playing sound");
		//this.playSound(SoundInit.DRIVINIG_CAR_SOUND.get(), 1, 1);
        this.playSound(SoundInit.DRIVINIG_CAR_SOUND.get(), 100f, 1f);
	}

	//has no armor, so empty list
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		// TODO Auto-generated method stub
		return new ArrayList<ItemStack>();
	}

	//currently has no inventory
	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		// TODO Auto-generated method stub
		return ItemStack.EMPTY;
	}

	//has no inventory by default
	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
	}

	//doesn't do anytthing really
	@Override
	public HandSide getPrimaryHand() {
		// TODO Auto-generated method stub
		return HandSide.LEFT;
	}
	
	//Necessary for other stuff to work later on, seems like a bad solution but works for now
	@Override
	public boolean canPassengerSteer() {
		return false;
	}
	
	//Don't allow potions to affect vehicles, maybe some could be whitelisted later on, like invisibility
	public boolean isPotionApplicable(EffectInstance potioneffectIn) {
		return false;
	}
	   
	//copy of private method
	protected float getRelevantMoveFactor(float p_213335_1_) {
		return this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.jumpMovementFactor;
	}

	//this is here so the server can open the inventory of any vehicle without having to know what vehicle exactly the player is riding. To check if this type of vehicle has an inventory, hasInventory() is used
	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return null;
	}
	
	@Override
	public boolean writeUnlessPassenger(CompoundNBT compound) {
		boolean result = super.writeUnlessPassenger(compound);
		compound.put("inventory", itemHandler.orElseThrow(null).serializeNBT());
		return result;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		itemHandler.ifPresent(x -> ((ItemStackHandler)x).deserializeNBT(compound.getCompound("inventory")));
	}
}
