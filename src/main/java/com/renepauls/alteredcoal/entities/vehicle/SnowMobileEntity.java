package com.renepauls.alteredcoal.entities.vehicle;

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
public class SnowMobileEntity extends LandVehicleEntity{
	
	public SnowMobileEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
		
		//allow to go up one block
		this.stepHeight = 1.0f;
		
		vehicleRotation = prevVehicleRotation = 0;

		seatManager.addDriverSeat(0.0f, 0.0f, this.getHeight() * 0.5f);
	}
	
	
	@Override
	public void tick() {
		super.tick();
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
}
