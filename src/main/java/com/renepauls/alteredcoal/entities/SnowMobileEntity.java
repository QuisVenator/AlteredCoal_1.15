package com.renepauls.alteredcoal.entities;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.renepauls.alteredcoal.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
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
	//TODO make tools repair, etc.
	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		if(!world.isRemote)
			player.startRiding(this, false);
		
		return true;
	}

	//TODO remove
	/*
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
	*/
	
	//should set an offset for the seat TODO get to work
	public float seatOffset() {
		return 0.8f;
	}
	@Override
	public double getMountedYOffset() {
		return this.getHeight() * 0.5D;
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
	
	//Necessary for other stuff to work later on, seems like a bad solutionbut works for now
	@Override
	public boolean canPassengerSteer() {
		return false;
	}
	
	//Don't allow potions to affect vehicles, maybe some could be whitelisted later on, like invisibillity
	public boolean isPotionApplicable(EffectInstance potioneffectIn) {
		return false;
	}
	
	//Copy of original travel method, but without considering sliperiness

	   public void travel(Vec3d p_213352_1_) {
	      if (this.isServerWorld() || this.canPassengerSteer()) {
	         double d0 = 0.08D;
	         IAttributeInstance gravity = this.getAttribute(ENTITY_GRAVITY);
	         boolean flag = this.getMotion().y <= 0.0D;
	         
	         d0 = gravity.getValue();

	         if (!this.isInWater() || (LivingEntity)this instanceof PlayerEntity && ((PlayerEntity)(LivingEntity)this).abilities.isFlying) {
	            if (!this.isInLava() || (LivingEntity)this instanceof PlayerEntity && ((PlayerEntity)(LivingEntity)this).abilities.isFlying) {
	               if (this.isElytraFlying()) {
	                  Vec3d vec3d3 = this.getMotion();
	                  if (vec3d3.y > -0.5D) {
	                     this.fallDistance = 1.0F;
	                  }

	                  Vec3d vec3d = this.getLookVec();
	                  float f6 = this.rotationPitch * ((float)Math.PI / 180F);
	                  double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
	                  double d11 = Math.sqrt(horizontalMag(vec3d3));
	                  double d12 = vec3d.length();
	                  float f3 = MathHelper.cos(f6);
	                  f3 = (float)((double)f3 * (double)f3 * Math.min(1.0D, d12 / 0.4D));
	                  vec3d3 = this.getMotion().add(0.0D, d0 * (-1.0D + (double)f3 * 0.75D), 0.0D);
	                  if (vec3d3.y < 0.0D && d9 > 0.0D) {
	                     double d3 = vec3d3.y * -0.1D * (double)f3;
	                     vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
	                  }

	                  if (f6 < 0.0F && d9 > 0.0D) {
	                     double d13 = d11 * (double)(-MathHelper.sin(f6)) * 0.04D;
	                     vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
	                  }

	                  if (d9 > 0.0D) {
	                     vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
	                  }

	                  this.setMotion(vec3d3.mul((double)0.99F, (double)0.98F, (double)0.99F));
	                  this.move(MoverType.SELF, this.getMotion());
	                  if (this.collidedHorizontally && !this.world.isRemote) {
	                     double d14 = Math.sqrt(horizontalMag(this.getMotion()));
	                     double d4 = d11 - d14;
	                     float f4 = (float)(d4 * 10.0D - 3.0D);
	                     if (f4 > 0.0F) {
	                        this.playSound(this.getFallSound((int)f4), 1.0F, 1.0F);
	                        this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f4);
	                     }
	                  }

	                  if (this.onGround && !this.world.isRemote) {
	                     this.setFlag(7, false);
	                  }
	               } else {
	                  BlockPos blockpos = this.getPositionUnderneath();
	                  float f5 = this.world.getBlockState(blockpos).getSlipperiness(world, blockpos, this);
	                  float f7 = this.onGround ? 0.6f * 0.91F : 0.91F;
	                  this.moveRelative(this.getRelevantMoveFactor(0.6f), p_213352_1_);
	                  this.setMotion(this.getMotion());
	                  this.move(MoverType.SELF, this.getMotion());
	                  Vec3d vec3d5 = this.getMotion();
	                  if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
	                     vec3d5 = new Vec3d(vec3d5.x, 0.2D, vec3d5.z);
	                  }

	                  double d10 = vec3d5.y;
	                  if (this.isPotionActive(Effects.LEVITATION)) {
	                     d10 += (0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vec3d5.y) * 0.2D;
	                     this.fallDistance = 0.0F;
	                  } else if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
	                     if (this.getPosY() > 0.0D) {
	                        d10 = -0.1D;
	                     } else {
	                        d10 = 0.0D;
	                     }
	                  } else if (!this.hasNoGravity()) {
	                     d10 -= d0;
	                  }

	                  this.setMotion(vec3d5.x * (double)f7, d10 * (double)0.98F, vec3d5.z * (double)f7);
	               }
	            } else {
	               double d7 = this.getPosY();
	               this.moveRelative(0.02F, p_213352_1_);
	               this.move(MoverType.SELF, this.getMotion());
	               this.setMotion(this.getMotion().scale(0.5D));
	               if (!this.hasNoGravity()) {
	                  this.setMotion(this.getMotion().add(0.0D, -d0 / 4.0D, 0.0D));
	               }

	               Vec3d vec3d4 = this.getMotion();
	               if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vec3d4.x, vec3d4.y + (double)0.6F - this.getPosY() + d7, vec3d4.z)) {
	                  this.setMotion(vec3d4.x, (double)0.3F, vec3d4.z);
	               }
	            }
	         } else {
	            double d1 = this.getPosY();
	            float f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
	            float f1 = 0.02F;
	            float f2 = (float)EnchantmentHelper.getDepthStriderModifier(this);
	            if (f2 > 3.0F) {
	               f2 = 3.0F;
	            }

	            if (!this.onGround) {
	               f2 *= 0.5F;
	            }

	            if (f2 > 0.0F) {
	               f += (0.54600006F - f) * f2 / 3.0F;
	               f1 += (this.getAIMoveSpeed() - f1) * f2 / 3.0F;
	            }

	            if (this.isPotionActive(Effects.DOLPHINS_GRACE)) {
	               f = 0.96F;
	            }

	            f1 *= (float)this.getAttribute(SWIM_SPEED).getValue();
	            this.moveRelative(f1, p_213352_1_);
	            this.move(MoverType.SELF, this.getMotion());
	            Vec3d vec3d1 = this.getMotion();
	            if (this.collidedHorizontally && this.isOnLadder()) {
	               vec3d1 = new Vec3d(vec3d1.x, 0.2D, vec3d1.z);
	            }

	            this.setMotion(vec3d1.mul((double)f, (double)0.8F, (double)f));
	            if (!this.hasNoGravity() && !this.isSprinting()) {
	               Vec3d vec3d2 = this.getMotion();
	               double d2;
	               if (flag && Math.abs(vec3d2.y - 0.005D) >= 0.003D && Math.abs(vec3d2.y - d0 / 16.0D) < 0.003D) {
	                  d2 = -0.003D;
	               } else {
	                  d2 = vec3d2.y - d0 / 16.0D;
	               }

	               this.setMotion(vec3d2.x, d2, vec3d2.z);
	            }

	            Vec3d vec3d6 = this.getMotion();
	            if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vec3d6.x, vec3d6.y + (double)0.6F - this.getPosY() + d1, vec3d6.z)) {
	               this.setMotion(vec3d6.x, (double)0.3F, vec3d6.z);
	            }
	         }
	      }

	      this.prevLimbSwingAmount = this.limbSwingAmount;
	      double d5 = this.getPosX() - this.prevPosX;
	      double d6 = this.getPosZ() - this.prevPosZ;
	      double d8 = this instanceof IFlyingAnimal ? this.getPosY() - this.prevPosY : 0.0D;
	      float f8 = MathHelper.sqrt(d5 * d5 + d8 * d8 + d6 * d6) * 4.0F;
	      if (f8 > 1.0F) {
	         f8 = 1.0F;
	      }

	      this.limbSwingAmount += (f8 - this.limbSwingAmount) * 0.4F;
	      this.limbSwing += this.limbSwingAmount;
	   }
	   
	//copy of private method
	private float getRelevantMoveFactor(float p_213335_1_) {
		return this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.jumpMovementFactor;
	}
}
