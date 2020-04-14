package com.renepauls.alteredcoal.entities;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.mojang.datafixers.Dynamic;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.network.play.server.SEntityEquipmentPacket;
import net.minecraft.network.play.server.SSpawnMobPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

public abstract class ReducedLivingEntity extends Entity {
   private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
   private static final UUID SLOW_FALLING_ID = UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA");
   private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", (double)0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL)).setSaved(false);
   private static final AttributeModifier SLOW_FALLING = new AttributeModifier(SLOW_FALLING_ID, "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION).setSaved(false); // Add -0.07 to 0.08 so we get the vanilla default of 0.01
   public static final net.minecraft.entity.ai.attributes.IAttribute SWIM_SPEED = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "forge.swimSpeed", 1.0D, 0.0D, 1024.0D).setShouldWatch(true);
   public static final net.minecraft.entity.ai.attributes.IAttribute NAMETAG_DISTANCE = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "forge.nameTagDistance", 64.0D, 0.0D, Float.MAX_VALUE).setShouldWatch(true);
   public static final net.minecraft.entity.ai.attributes.IAttribute ENTITY_GRAVITY = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "forge.entity_gravity", 0.08D, -8.0D, 8.0D).setShouldWatch(true);
   protected static final DataParameter<Byte> LIVING_FLAGS = EntityDataManager.createKey(LivingEntity.class, DataSerializers.BYTE);
   private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(LivingEntity.class, DataSerializers.FLOAT);
   private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.createKey(LivingEntity.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.createKey(LivingEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(LivingEntity.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> BEE_STING_COUNT = EntityDataManager.createKey(LivingEntity.class, DataSerializers.VARINT);
   private static final DataParameter<Optional<BlockPos>> BED_POSITION = EntityDataManager.createKey(LivingEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
   private AbstractAttributeMap attributes;
   private final Map<Effect, EffectInstance> activePotionsMap = Maps.newHashMap();
   private final NonNullList<ItemStack> handInventory = NonNullList.withSize(2, ItemStack.EMPTY);
   private final NonNullList<ItemStack> armorArray = NonNullList.withSize(4, ItemStack.EMPTY);
   public boolean isSwingInProgress;
   public Hand swingingHand;
   public int swingProgressInt;
   public int arrowHitTimer;
   public int beeStingRemovalCooldown;
   public int hurtTime;
   public int maxHurtTime;
   public float attackedAtYaw;
   public int deathTime;
   public float prevSwingProgress;
   public float swingProgress;
   protected int ticksSinceLastSwing;
   public float prevLimbSwingAmount;
   public float limbSwingAmount;
   public float limbSwing;
   public final int maxHurtResistantTime = 20;
   public final float randomUnused2;
   public final float randomUnused1;
   public float renderYawOffset;
   public float prevRenderYawOffset;
   public float rotationYawHead;
   public float prevRotationYawHead;
   public float jumpMovementFactor = 0.02F;
   protected PlayerEntity attackingPlayer;
   protected int recentlyHit;
   protected boolean dead;
   protected int idleTime;
   protected float prevOnGroundSpeedFactor;
   protected float onGroundSpeedFactor;
   protected float movedDistance;
   protected float prevMovedDistance;
   protected float unused180;
   protected int scoreValue;
   /** Damage taken in the last hit. Mobs are resistant to damage less than this for a short time after taking damage. */
   protected float lastDamage;
   protected boolean isJumping;
   public float moveStrafing;
   public float moveVertical;
   public float moveForward;
   protected int newPosRotationIncrements;
   protected double interpTargetX;
   protected double interpTargetY;
   protected double interpTargetZ;
   protected double interpTargetYaw;
   protected double interpTargetPitch;
   protected double interpTargetHeadYaw;
   protected int interpTicksHead;
   private boolean potionsNeedUpdate = true;
   @Nullable
   private LivingEntity revengeTarget;
   private int revengeTimer;
   private LivingEntity lastAttackedEntity;
   /** Holds the value of ticksExisted when setLastAttacker was last called. */
   private int lastAttackedEntityTime;
   private float landMovementFactor;
   private int jumpTicks;
   private float absorptionAmount;
   protected ItemStack activeItemStack = ItemStack.EMPTY;
   protected int activeItemStackUseCount;
   protected int ticksElytraFlying;
   private BlockPos prevBlockpos;
   private DamageSource lastDamageSource;
   private long lastDamageStamp;
   protected int spinAttackDuration;
   private float swimAnimation;
   private float lastSwimAnimation;
   protected Brain<?> brain;

   protected ReducedLivingEntity(EntityType<? extends ReducedLivingEntity> type, World worldIn) {
      super(type, worldIn);
      this.registerAttributes();
      this.setHealth(this.getMaxHealth());
      this.preventEntitySpawning = true;
      this.randomUnused1 = (float)((Math.random() + 1.0D) * (double)0.01F);
      this.recenterBoundingBox();
      this.randomUnused2 = (float)Math.random() * 12398.0F;
      this.rotationYaw = (float)(Math.random() * (double)((float)Math.PI * 2F));
      this.rotationYawHead = this.rotationYaw;
      this.stepHeight = 0.6F;
      this.brain = this.createBrain(new Dynamic<>(NBTDynamicOps.INSTANCE, new CompoundNBT()));
   }

   public Brain<?> getBrain() {
      return this.brain;
   }

   protected Brain<?> createBrain(Dynamic<?> dynamicIn) {
      return new Brain<>(ImmutableList.of(), ImmutableList.of(), dynamicIn);
   }

   /**
    * Called by the /kill command.
    */
   public void onKillCommand() {
      this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
   }

   public boolean canAttack(EntityType<?> typeIn) {
      return true;
   }

   protected void registerData() {
      this.dataManager.register(LIVING_FLAGS, (byte)0);
      this.dataManager.register(POTION_EFFECTS, 0);
      this.dataManager.register(HIDE_PARTICLES, false);
      this.dataManager.register(ARROW_COUNT_IN_ENTITY, 0);
      this.dataManager.register(BEE_STING_COUNT, 0);
      this.dataManager.register(HEALTH, 1.0F);
      this.dataManager.register(BED_POSITION, Optional.empty());
   }

   protected void registerAttributes() {
      this.getAttributes().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
      this.getAttributes().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
      this.getAttributes().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      this.getAttributes().registerAttribute(SharedMonsterAttributes.ARMOR);
      this.getAttributes().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
      this.getAttributes().registerAttribute(SWIM_SPEED);
      this.getAttributes().registerAttribute(NAMETAG_DISTANCE);
      this.getAttributes().registerAttribute(ENTITY_GRAVITY);
   }

   protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
      if (!this.isInWater()) {
         this.handleWaterMovement();
      }

      if (!this.world.isRemote && this.fallDistance > 3.0F && onGroundIn) {
         float f = (float)MathHelper.ceil(this.fallDistance - 3.0F);
         if (!state.isAir(world, pos)) {
            double d0 = Math.min((double)(0.2F + f / 15.0F), 2.5D);
            int i = (int)(150.0D * d0);
            ((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), this.getPosX(), this.getPosY(), this.getPosZ(), i, 0.0D, 0.0D, 0.0D, (double)0.15F);
         }
      }

      super.updateFallState(y, onGroundIn, state, pos);
   }

   public boolean canBreatheUnderwater() {
      return this.getCreatureAttribute() == CreatureAttribute.UNDEAD;
   }

   @OnlyIn(Dist.CLIENT)
   public float getSwimAnimation(float partialTicks) {
      return MathHelper.lerp(partialTicks, this.lastSwimAnimation, this.swimAnimation);
   }

   /**
    * Gets called every tick from main Entity class
    */
   public void baseTick() {
      this.prevSwingProgress = this.swingProgress;

      super.baseTick();
      this.world.getProfiler().startSection("livingEntityBaseTick");
      if (this.isAlive()) {
         if (this.isEntityInsideOpaqueBlock()) {
            this.attackEntityFrom(DamageSource.IN_WALL, 1.0F);
         } 
      }

      if (this.isImmuneToFire() || this.world.isRemote) {
         this.extinguish();
      }

      if (this.isAlive()) {
         if (this.areEyesInFluid(FluidTags.WATER) && this.world.getBlockState(new BlockPos(this.getPosX(), this.getPosYEye(), this.getPosZ())).getBlock() != Blocks.BUBBLE_COLUMN) {

            if (!this.world.isRemote && this.isPassenger() && this.getRidingEntity() != null && !this.getRidingEntity().canBeRiddenInWater(this)) {
               this.stopRiding();
            }
         } else if (this.getAir() < this.getMaxAir()) {
            this.setAir(this.determineNextAir(this.getAir()));
         }

         if (!this.world.isRemote) {
            BlockPos blockpos = new BlockPos(this);
            if (!Objects.equal(this.prevBlockpos, blockpos)) {
               this.prevBlockpos = blockpos;
            }
         }
      }

      if (this.isAlive() && this.isInWaterRainOrBubbleColumn()) {
         this.extinguish();
      }

      if (this.hurtTime > 0) {
         --this.hurtTime;
      }

      if (this.getHealth() <= 0.0F) {
         this.onDeathUpdate();
      }

      if (this.recentlyHit > 0) {
         --this.recentlyHit;
      } else {
         this.attackingPlayer = null;
      }

      if (this.lastAttackedEntity != null && !this.lastAttackedEntity.isAlive()) {
         this.lastAttackedEntity = null;
      }

      if (this.revengeTarget != null) {
         if (!this.revengeTarget.isAlive()) {
            this.setRevengeTarget((LivingEntity)null);
         } else if (this.ticksExisted - this.revengeTimer > 100) {
            this.setRevengeTarget((LivingEntity)null);
         }
      }

      this.updatePotionEffects();
      this.prevMovedDistance = this.movedDistance;
      this.prevRenderYawOffset = this.renderYawOffset;
      this.prevRotationYawHead = this.rotationYawHead;
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
      this.world.getProfiler().endSection();
   }

   /**
    * If Animal, checks if the age timer is negative
    */
   public boolean isChild() {
      return false;
   }

   public float getRenderScale() {
      return this.isChild() ? 0.5F : 1.0F;
   }

   public boolean canBeRiddenInWater() {
      return false;
   }

   /**
    * handles entity death timer, experience orb and particle creation
    */
   protected void onDeathUpdate() {
      ++this.deathTime;
      if (this.deathTime == 20) {

         for(int i = 0; i < 20; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
         }
      }

   }

   /**
    * Entity won't drop items or experience points if this returns false
    */
   protected boolean canDropLoot() {
      return !this.isChild();
   }

   protected int determineNextAir(int currentAir) {
      return Math.min(currentAir + 4, this.getMaxAir());
   }

   /**
    * Get the experience points the entity currently has.
    */
   protected int getExperiencePoints(PlayerEntity player) {
      return 0;
   }

   /**
    * Only use is to identify if class is an instance of player for experience dropping
    */
   protected boolean isPlayer() {
      return false;
   }

   public Random getRNG() {
      return this.rand;
   }

   @Nullable
   public LivingEntity getRevengeTarget() {
      return this.revengeTarget;
   }

   public int getRevengeTimer() {
      return this.revengeTimer;
   }

   /**
    * Hint to AI tasks that we were attacked by the passed EntityLivingBase and should retaliate. Is not guaranteed to
    * change our actual active target (for example if we are currently busy attacking someone else)
    */
   public void setRevengeTarget(@Nullable LivingEntity livingBase) {
      this.revengeTarget = livingBase;
      this.revengeTimer = this.ticksExisted;
   }

   @Nullable
   public LivingEntity getLastAttackedEntity() {
      return this.lastAttackedEntity;
   }

   public int getLastAttackedEntityTime() {
      return this.lastAttackedEntityTime;
   }

   public void setLastAttackedEntity(Entity entityIn) {
      if (entityIn instanceof LivingEntity) {
         this.lastAttackedEntity = (LivingEntity)entityIn;
      } else {
         this.lastAttackedEntity = null;
      }

      this.lastAttackedEntityTime = this.ticksExisted;
   }

   public int getIdleTime() {
      return this.idleTime;
   }

   public void setIdleTime(int idleTimeIn) {
      this.idleTime = idleTimeIn;
   }

   protected void playEquipSound(ItemStack stack) {
      if (!stack.isEmpty()) {
         SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
         Item item = stack.getItem();
         if (item instanceof ArmorItem) {
            soundevent = ((ArmorItem)item).getArmorMaterial().getSoundEvent();
         } else if (item == Items.ELYTRA) {
            soundevent = SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA;
         }

         this.playSound(soundevent, 1.0F, 1.0F);
      }
   }

   public void writeAdditional(CompoundNBT compound) {
      compound.putFloat("Health", this.getHealth());
      compound.putShort("HurtTime", (short)this.hurtTime);
      compound.putInt("HurtByTimestamp", this.revengeTimer);
      compound.putShort("DeathTime", (short)this.deathTime);
      compound.put("Attributes", SharedMonsterAttributes.writeAttributes(this.getAttributes()));
      if (!this.activePotionsMap.isEmpty()) {
         ListNBT listnbt = new ListNBT();

         for(EffectInstance effectinstance : this.activePotionsMap.values()) {
            listnbt.add(effectinstance.write(new CompoundNBT()));
         }

         compound.put("ActiveEffects", listnbt);
      }

      compound.put("Brain", this.brain.serialize(NBTDynamicOps.INSTANCE));
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditional(CompoundNBT compound) {
      if (compound.contains("Attributes", 9) && this.world != null && !this.world.isRemote) {
         SharedMonsterAttributes.readAttributes(this.getAttributes(), compound.getList("Attributes", 10));
      }

      if (compound.contains("ActiveEffects", 9)) {
         ListNBT listnbt = compound.getList("ActiveEffects", 10);

         for(int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            EffectInstance effectinstance = EffectInstance.read(compoundnbt);
            if (effectinstance != null) {
               this.activePotionsMap.put(effectinstance.getPotion(), effectinstance);
            }
         }
      }

      if (compound.contains("Health", 99)) {
         this.setHealth(compound.getFloat("Health"));
      }

      this.hurtTime = compound.getShort("HurtTime");
      this.deathTime = compound.getShort("DeathTime");
      this.revengeTimer = compound.getInt("HurtByTimestamp");
      if (compound.contains("Team", 8)) {
         String s = compound.getString("Team");
         ScorePlayerTeam scoreplayerteam = this.world.getScoreboard().getTeam(s);
         boolean flag = scoreplayerteam != null && this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), scoreplayerteam);
         if (!flag) {
            LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", (Object)s);
         }
      }

      if (compound.getBoolean("FallFlying")) {
         this.setFlag(7, true);
      }

      if (compound.contains("SleepingX", 99) && compound.contains("SleepingY", 99) && compound.contains("SleepingZ", 99)) {
         BlockPos blockpos = new BlockPos(compound.getInt("SleepingX"), compound.getInt("SleepingY"), compound.getInt("SleepingZ"));
      }

      if (compound.contains("Brain", 10)) {
         this.brain = this.createBrain(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.get("Brain")));
      }

   }

   protected void updatePotionEffects() {
      Iterator<Effect> iterator = this.activePotionsMap.keySet().iterator();

      try {
         while(iterator.hasNext()) {
            Effect effect = iterator.next();
            EffectInstance effectinstance = this.activePotionsMap.get(effect);
            if (effectinstance.getDuration() % 600 == 0) {
               this.onChangedPotionEffect(effectinstance, false);
            }
         }
      } catch (ConcurrentModificationException var11) {
         ;
      }

      if (this.potionsNeedUpdate) {
         if (!this.world.isRemote) {
            this.updatePotionMetadata();
         }

         this.potionsNeedUpdate = false;
      }

      int i = this.dataManager.get(POTION_EFFECTS);
      boolean flag1 = this.dataManager.get(HIDE_PARTICLES);
      if (i > 0) {
         boolean flag;
         if (this.isInvisible()) {
            flag = this.rand.nextInt(15) == 0;
         } else {
            flag = this.rand.nextBoolean();
         }

         if (flag1) {
            flag &= this.rand.nextInt(5) == 0;
         }

         if (flag && i > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;
            this.world.addParticle(flag1 ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
         }
      }

   }

   /**
    * Clears potion metadata values if the entity has no potion effects. Otherwise, updates potion effect color,
    * ambience, and invisibility metadata values
    */
   protected void updatePotionMetadata() {
      if (this.activePotionsMap.isEmpty()) {
         this.resetPotionEffectMetadata();
         this.setInvisible(false);
      } else {
         Collection<EffectInstance> collection = this.activePotionsMap.values();
         this.setInvisible(this.isPotionActive(Effects.INVISIBILITY));
      }
   }

   public double getVisibilityMultiplier(@Nullable Entity lookingEntity) {
      double d0 = 1.0D;
      if (this.isDiscrete()) {
         d0 *= 0.8D;
      }

      if (this.isInvisible()) {
         float f = this.func_213343_cS();
         if (f < 0.1F) {
            f = 0.1F;
         }

         d0 *= 0.7D * (double)f;
      }

      if (lookingEntity != null) {
         ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.HEAD);
         Item item = itemstack.getItem();
         EntityType<?> entitytype = lookingEntity.getType();
         if (entitytype == EntityType.SKELETON && item == Items.SKELETON_SKULL || entitytype == EntityType.ZOMBIE && item == Items.ZOMBIE_HEAD || entitytype == EntityType.CREEPER && item == Items.CREEPER_HEAD) {
            d0 *= 0.5D;
         }
      }

      return d0;
   }

   public boolean canAttack(LivingEntity target) {
      return true;
   }

   /**
    * Returns true if all of the potion effects in the specified collection are ambient.
    */
   public static boolean areAllPotionsAmbient(Collection<EffectInstance> potionEffects) {
      for(EffectInstance effectinstance : potionEffects) {
         if (!effectinstance.isAmbient()) {
            return false;
         }
      }

      return true;
   }

   /**
    * Resets the potion effect color and ambience metadata values
    */
   protected void resetPotionEffectMetadata() {
      this.dataManager.set(HIDE_PARTICLES, false);
      this.dataManager.set(POTION_EFFECTS, 0);
   }

   public boolean clearActivePotions() {
      if (this.world.isRemote) {
         return false;
      } else {
         Iterator<EffectInstance> iterator = this.activePotionsMap.values().iterator();

         boolean flag;
         for(flag = false; iterator.hasNext(); flag = true) {
            EffectInstance effect = iterator.next();
            this.onFinishedPotionEffect(effect);
            iterator.remove();
         }

         return flag;
      }
   }

   public Collection<EffectInstance> getActivePotionEffects() {
      return this.activePotionsMap.values();
   }

   public Map<Effect, EffectInstance> getActivePotionMap() {
      return this.activePotionsMap;
   }

   public boolean isPotionActive(Effect potionIn) {
      return this.activePotionsMap.containsKey(potionIn);
   }

   /**
    * returns the PotionEffect for the supplied Potion if it is active, null otherwise.
    */
   @Nullable
   public EffectInstance getActivePotionEffect(Effect potionIn) {
      return this.activePotionsMap.get(potionIn);
   }

   /**
    * Returns true if this entity is undead.
    */
   public boolean isEntityUndead() {
      return this.getCreatureAttribute() == CreatureAttribute.UNDEAD;
   }

   /**
    * Removes the given potion effect from the active potion map and returns it. Does not call cleanup callbacks for the
    * end of the potion effect.
    */
   @Nullable
   public EffectInstance removeActivePotionEffect(@Nullable Effect potioneffectin) {
      return this.activePotionsMap.remove(potioneffectin);
   }

   public boolean removePotionEffect(Effect effectIn) {
      EffectInstance effectinstance = this.removeActivePotionEffect(effectIn);
      if (effectinstance != null) {
         this.onFinishedPotionEffect(effectinstance);
         return true;
      } else {
         return false;
      }
   }

   protected void onNewPotionEffect(EffectInstance id) {
      this.potionsNeedUpdate = true;

   }

   protected void onChangedPotionEffect(EffectInstance id, boolean reapply) {
      this.potionsNeedUpdate = true;
      if (reapply && !this.world.isRemote) {
         Effect effect = id.getPotion();
      }

   }

   protected void onFinishedPotionEffect(EffectInstance effect) {
      this.potionsNeedUpdate = true;

   }

   /**
    * Heal living entity (param: amount of half-hearts)
    */
   public void heal(float healAmount) {
      if (healAmount <= 0) return;
      float f = this.getHealth();
      if (f > 0.0F) {
         this.setHealth(f + healAmount);
      }

   }

   public float getHealth() {
      return this.dataManager.get(HEALTH);
   }

   public void setHealth(float health) {
      this.dataManager.set(HEALTH, MathHelper.clamp(health, 0.0F, this.getMaxHealth()));
   }

   /**
    * Called when the entity is attacked.
    */
   public boolean attackEntityFrom(DamageSource source, float amount) {
      if (this.isInvulnerableTo(source)) {
         return false;
      } else if (this.world.isRemote) {
         return false;
      } else if (this.getHealth() <= 0.0F) {
         return false;
      } else if (source.isFireDamage() && this.isPotionActive(Effects.FIRE_RESISTANCE)) {
         return false;
      } else {

         this.idleTime = 0;
         float f = amount;
         if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
            amount *= 0.75F;
         }

         boolean flag = false;
         float f1 = 0.0F;
         if (amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);
            f1 = amount;
            amount = 0.0F;
            if (!source.isProjectile()) {
               Entity entity = source.getImmediateSource();
            }

            flag = true;
         }

         this.limbSwingAmount = 1.5F;
         boolean flag1 = true;
         if ((float)this.hurtResistantTime > 10.0F) {
            if (amount <= this.lastDamage) {
               return false;
            }

            this.damageEntity(source, amount - this.lastDamage);
            this.lastDamage = amount;
            flag1 = false;
         } else {
            this.lastDamage = amount;
            this.hurtResistantTime = 20;
            this.damageEntity(source, amount);
            this.maxHurtTime = 10;
            this.hurtTime = this.maxHurtTime;
         }

         this.attackedAtYaw = 0.0F;
         Entity entity1 = source.getTrueSource();
         if (entity1 != null) {
            if (entity1 instanceof LivingEntity) {
               this.setRevengeTarget((LivingEntity)entity1);
            }

            if (entity1 instanceof PlayerEntity) {
               this.recentlyHit = 100;
               this.attackingPlayer = (PlayerEntity)entity1;
            } else if (entity1 instanceof net.minecraft.entity.passive.TameableEntity) {
               net.minecraft.entity.passive.TameableEntity wolfentity = (net.minecraft.entity.passive.TameableEntity)entity1;
               if (wolfentity.isTamed()) {
                  this.recentlyHit = 100;
                  LivingEntity livingentity = wolfentity.getOwner();
                  if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
                     this.attackingPlayer = (PlayerEntity)livingentity;
                  } else {
                     this.attackingPlayer = null;
                  }
               }
            }
         }

         if (flag1) {
            if (flag) {
               this.world.setEntityState(this, (byte)29);
            } else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
               this.world.setEntityState(this, (byte)33);
            } else {
               byte b0;
               if (source == DamageSource.DROWN) {
                  b0 = 36;
               } else if (source.isFireDamage()) {
                  b0 = 37;
               } else if (source == DamageSource.SWEET_BERRY_BUSH) {
                  b0 = 44;
               } else {
                  b0 = 2;
               }

               this.world.setEntityState(this, b0);
            }

            if (source != DamageSource.DROWN && (!flag || amount > 0.0F)) {
               this.markVelocityChanged();
            }

            if (entity1 != null) {
               double d1 = entity1.getPosX() - this.getPosX();

               double d0;
               for(d0 = entity1.getPosZ() - this.getPosZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                  d1 = (Math.random() - Math.random()) * 0.01D;
               }

               this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)this.rotationYaw);
               this.knockBack(entity1, 0.4F, d1, d0);
            } else {
               this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
            }
         }

         if (this.getHealth() <= 0.0F) {
            if (!this.checkTotemDeathProtection(source)) {
               SoundEvent soundevent = this.getDeathSound();
               if (flag1 && soundevent != null) {
                  this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
               }

               this.onDeath(source);
            }
         } else if (flag1) {
            this.playHurtSound(source);
         }

         boolean flag2 = !flag || amount > 0.0F;
         if (flag2) {
            this.lastDamageSource = source;
            this.lastDamageStamp = this.world.getGameTime();
         }

         if (entity1 instanceof ServerPlayerEntity) {
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity)entity1, this, source, f, amount, flag);
         }

         return flag2;
      }
   }

   protected void constructKnockBackVector(LivingEntity entityIn) {
      entityIn.knockBack(this, 0.5F, entityIn.getPosX() - this.getPosX(), entityIn.getPosZ() - this.getPosZ());
   }

   private boolean checkTotemDeathProtection(DamageSource damageSourceIn) {
      if (damageSourceIn.canHarmInCreative()) {
         return false;
      } else {
         ItemStack itemstack = null;

         for(Hand hand : Hand.values()) {
            ItemStack itemstack1 = this.getHeldItem(hand);
            if (itemstack1.getItem() == Items.TOTEM_OF_UNDYING) {
               itemstack = itemstack1.copy();
               itemstack1.shrink(1);
               break;
            }
         }

         if (itemstack != null) {

            this.setHealth(1.0F);
            this.clearActivePotions();
            this.world.setEntityState(this, (byte)35);
         }

         return itemstack != null;
      }
   }

   @Nullable
   public DamageSource getLastDamageSource() {
      if (this.world.getGameTime() - this.lastDamageStamp > 40L) {
         this.lastDamageSource = null;
      }

      return this.lastDamageSource;
   }

   protected void playHurtSound(DamageSource source) {
      SoundEvent soundevent = this.getHurtSound(source);
      if (soundevent != null) {
         this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
      }

   }

   /**
    * Determines whether the entity can block the damage source based on the damage source's location, whether the
    * damage source is blockable, and whether the entity is blocking.
    */
   private boolean canBlockDamageSource(DamageSource damageSourceIn) {
      Entity entity = damageSourceIn.getImmediateSource();
      boolean flag = false;
      if (entity instanceof AbstractArrowEntity) {
         AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity)entity;
         if (abstractarrowentity.getPierceLevel() > 0) {
            flag = true;
         }
      }

      return false;
   }

   /**
    * Renders broken item particles using the given ItemStack
    */
   @OnlyIn(Dist.CLIENT)
   private void renderBrokenItemStack(ItemStack stack) {
      if (!stack.isEmpty()) {
         if (!this.isSilent()) {
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, this.getSoundCategory(), 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F, false);
         }
      }

   }

   /**
    * Called when the mob's health reaches 0.
    */
   public void onDeath(DamageSource cause) {
      if (!this.removed && !this.dead) {
         Entity entity = cause.getTrueSource();

         this.dead = true;
         if (!this.world.isRemote) {
            this.spawnDrops(cause);
         }

         this.world.setEntityState(this, (byte)3);
      }
   }

   protected void func_226298_f_(@Nullable LivingEntity p_226298_1_) {
      if (!this.world.isRemote) {
         boolean flag = false;
         if (p_226298_1_ instanceof WitherEntity) {
               if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
               BlockPos blockpos = new BlockPos(this);
               BlockState blockstate = Blocks.WITHER_ROSE.getDefaultState();
               if (this.world.isAirBlock(blockpos) && blockstate.isValidPosition(this.world, blockpos)) {
                  this.world.setBlockState(blockpos, blockstate, 3);
                  flag = true;
               }
            }

            if (!flag) {
               ItemEntity itementity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), new ItemStack(Items.WITHER_ROSE));
               this.world.addEntity(itementity);
            }
         }

      }
   }

   protected void spawnDrops(DamageSource damageSourceIn) {
      Entity entity = damageSourceIn.getTrueSource();

      int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(this, entity, damageSourceIn);
      this.captureDrops(new java.util.ArrayList<>());

      boolean flag = this.recentlyHit > 0;
      if (this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
         this.dropLoot(damageSourceIn, flag);
         this.dropSpecialItems(damageSourceIn, i, flag);
      }

      this.dropInventory();
      this.func_226294_cV_();

      Collection<ItemEntity> drops = captureDrops(null);
   }

   protected void dropInventory() {
   }

   protected void func_226294_cV_() {
      if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
         int i = this.getExperiencePoints(this.attackingPlayer);

         while(i > 0) {
            int j = ExperienceOrbEntity.getXPSplit(i);
            i -= j;
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), j));
         }
      }


   }

   protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
   }

   public ResourceLocation getLootTableResourceLocation() {
      return this.getType().getLootTable();
   }

   protected void dropLoot(DamageSource damageSourceIn, boolean p_213354_2_) {
      ResourceLocation resourcelocation = this.getLootTableResourceLocation();
      LootTable loottable = this.world.getServer().getLootTableManager().getLootTableFromLocation(resourcelocation);
      LootContext.Builder lootcontext$builder = this.getLootContextBuilder(p_213354_2_, damageSourceIn);
      loottable.generate(lootcontext$builder.build(LootParameterSets.ENTITY), this::entityDropItem);
   }

   protected LootContext.Builder getLootContextBuilder(boolean p_213363_1_, DamageSource damageSourceIn) {
      LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.world)).withRandom(this.rand).withParameter(LootParameters.THIS_ENTITY, this).withParameter(LootParameters.POSITION, new BlockPos(this)).withParameter(LootParameters.DAMAGE_SOURCE, damageSourceIn).withNullableParameter(LootParameters.KILLER_ENTITY, damageSourceIn.getTrueSource()).withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, damageSourceIn.getImmediateSource());
      if (p_213363_1_ && this.attackingPlayer != null) {
         lootcontext$builder = lootcontext$builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
      }

      return lootcontext$builder;
   }

   /**
    * Constructs a knockback vector from the given direction ratio and magnitude and adds it to the entity's velocity.
    * If it is on the ground (i.e. {@code this.onGround}), the Y-velocity is increased as well, clamping it to {@code
    * .4}.
    * 
    * The entity's existing horizontal velocity is halved, and if the entity is on the ground the Y-velocity is too.
    */
   public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
      if (!(this.rand.nextDouble() < this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue())) {
         this.isAirBorne = true;
         Vec3d vec3d = this.getMotion();
         Vec3d vec3d1 = (new Vec3d(xRatio, 0.0D, zRatio)).normalize().scale((double)strength);
         this.setMotion(vec3d.x / 2.0D - vec3d1.x, this.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double)strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
      }
   }

   @Nullable
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.ENTITY_GENERIC_HURT;
   }

   @Nullable
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_GENERIC_DEATH;
   }

   protected SoundEvent getFallSound(int heightIn) {
      return heightIn > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
   }

   protected SoundEvent getDrinkSound(ItemStack stack) {
      return stack.func_226629_F_();
   }

   public SoundEvent getEatSound(ItemStack itemStackIn) {
      return itemStackIn.func_226630_G_();
   }

   public BlockState getBlockState() {
      return this.world.getBlockState(new BlockPos(this));
   }

   private boolean canGoThroughtTrapDoorOnLadder(BlockPos pos, BlockState state) {
      if (state.get(TrapDoorBlock.OPEN)) {
         BlockState blockstate = this.world.getBlockState(pos.down());
         if (blockstate.getBlock() == Blocks.LADDER && blockstate.get(LadderBlock.FACING) == state.get(TrapDoorBlock.HORIZONTAL_FACING)) {
            return true;
         }
      }

      return false;
   }

   /**
    * Returns true if the entity has not been {@link #removed}.
    */
   public boolean isAlive() {
      return !this.removed && this.getHealth() > 0.0F;
   }

   protected int func_225508_e_(float p_225508_1_, float p_225508_2_) {
      EffectInstance effectinstance = this.getActivePotionEffect(Effects.JUMP_BOOST);
      float f = effectinstance == null ? 0.0F : (float)(effectinstance.getAmplifier() + 1);
      return MathHelper.ceil((p_225508_1_ - 3.0F - f) * p_225508_2_);
   }

   /**
    * Plays the fall sound for the block landed on
    */
   protected void playFallSound() {
      if (!this.isSilent()) {
         int i = MathHelper.floor(this.getPosX());
         int j = MathHelper.floor(this.getPosY() - (double)0.2F);
         int k = MathHelper.floor(this.getPosZ());
         BlockPos pos = new BlockPos(i, j, k);
         BlockState blockstate = this.world.getBlockState(pos);
         if (!blockstate.isAir(this.world, pos)) {
            SoundType soundtype = blockstate.getSoundType(world, pos, this);
            this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
         }

      }
   }

   /**
    * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
    */
   @OnlyIn(Dist.CLIENT)
   public void performHurtAnimation() {
      this.maxHurtTime = 10;
      this.hurtTime = this.maxHurtTime;
      this.attackedAtYaw = 0.0F;
   }

   /**
    * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
    */
   public int getTotalArmorValue() {
      IAttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.ARMOR);
      return MathHelper.floor(iattributeinstance.getValue());
   }

   protected void damageArmor(float damage) {
   }

   protected void damageShield(float damage) {
   }

   /**
    * Reduces damage, depending on armor
    */
   protected float applyArmorCalculations(DamageSource source, float damage) {
      if (!source.isUnblockable()) {
         this.damageArmor(damage);
         damage = CombatRules.getDamageAfterAbsorb(damage, (float)this.getTotalArmorValue(), (float)this.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue());
      }

      return damage;
   }

   /**
    * Reduces damage, depending on potions
    */
   protected float applyPotionDamageCalculations(DamageSource source, float damage) {
      if (source.isDamageAbsolute()) {
         return damage;
      } else {
         if (this.isPotionActive(Effects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD) {
            int i = (this.getActivePotionEffect(Effects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f = damage * (float)j;
            float f1 = damage;
            damage = Math.max(f / 25.0F, 0.0F);
            float f2 = f1 - damage;
            if (f2 > 0.0F && f2 < 3.4028235E37F) {
               if (source.getTrueSource() instanceof ServerPlayerEntity) {
                  ((ServerPlayerEntity)source.getTrueSource()).addStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
               }
            }
         }

         if (damage <= 0.0F) {
            return 0.0F;
         } else {
            int k = EnchantmentHelper.getEnchantmentModifierDamage(this.getArmorInventoryList(), source);
            if (k > 0) {
               damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
            }

            return damage;
         }
      }
   }

   /**
    * Deals damage to the entity. This will take the armor of the entity into consideration before damaging the health
    * bar.
    */
   protected void damageEntity(DamageSource damageSrc, float damageAmount) {
      if (!this.isInvulnerableTo(damageSrc)) {
         if (damageAmount <= 0) return;
         damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
         damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);

      }
   }

   /**
    * Returns the maximum health of the entity (what it is able to regenerate up to, what it spawned with, etc)
    */
   public final float getMaxHealth() {
      return (float)this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue();
   }

   /**
    * counts the amount of arrows stuck in the entity. getting hit by arrows increases this, used in rendering
    */
   public final int getArrowCountInEntity() {
      return this.dataManager.get(ARROW_COUNT_IN_ENTITY);
   }

   /**
    * sets the amount of arrows stuck in the entity. used for rendering those
    */
   public final void setArrowCountInEntity(int count) {
      this.dataManager.set(ARROW_COUNT_IN_ENTITY, count);
   }

   public final int getBeeStingCount() {
      return this.dataManager.get(BEE_STING_COUNT);
   }

   public final void setBeeStingCount(int p_226300_1_) {
      this.dataManager.set(BEE_STING_COUNT, p_226300_1_);
   }

   /**
    * Handler for {@link World#setEntityState}
    */
   @OnlyIn(Dist.CLIENT)
   public void handleStatusUpdate(byte id) {
      switch(id) {
      case 2:
      case 33:
      case 36:
      case 37:
      case 44:
         boolean flag1 = id == 33;
         boolean flag2 = id == 36;
         boolean flag3 = id == 37;
         boolean flag = id == 44;
         this.limbSwingAmount = 1.5F;
         this.hurtResistantTime = 20;
         this.maxHurtTime = 10;
         this.hurtTime = this.maxHurtTime;
         this.attackedAtYaw = 0.0F;
         if (flag1) {
            this.playSound(SoundEvents.ENCHANT_THORNS_HIT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         DamageSource damagesource;
         if (flag3) {
            damagesource = DamageSource.ON_FIRE;
         } else if (flag2) {
            damagesource = DamageSource.DROWN;
         } else if (flag) {
            damagesource = DamageSource.SWEET_BERRY_BUSH;
         } else {
            damagesource = DamageSource.GENERIC;
         }

         SoundEvent soundevent1 = this.getHurtSound(damagesource);
         if (soundevent1 != null) {
            this.playSound(soundevent1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         this.attackEntityFrom(DamageSource.GENERIC, 0.0F);
         break;
      case 3:
         SoundEvent soundevent = this.getDeathSound();
         if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }
         break;
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 31:
      case 32:
      case 34:
      case 35:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 45:
      case 53:
      default:
         super.handleStatusUpdate(id);
         break;
      case 29:
         this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
         break;
      case 30:
         this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
         break;
      case 46:
         int i = 128;

         for(int j = 0; j < 128; ++j) {
            double d0 = (double)j / 127.0D;
            float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            double d1 = MathHelper.lerp(d0, this.prevPosX, this.getPosX()) + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth() * 2.0D;
            double d2 = MathHelper.lerp(d0, this.prevPosY, this.getPosY()) + this.rand.nextDouble() * (double)this.getHeight();
            double d3 = MathHelper.lerp(d0, this.prevPosZ, this.getPosZ()) + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth() * 2.0D;
            this.world.addParticle(ParticleTypes.PORTAL, d1, d2, d3, (double)f, (double)f1, (double)f2);
         }
         break;
      case 47:
         this.renderBrokenItemStack(this.getItemStackFromSlot(EquipmentSlotType.MAINHAND));
         break;
      case 48:
         this.renderBrokenItemStack(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND));
         break;
      case 49:
         this.renderBrokenItemStack(this.getItemStackFromSlot(EquipmentSlotType.HEAD));
         break;
      case 50:
         this.renderBrokenItemStack(this.getItemStackFromSlot(EquipmentSlotType.CHEST));
         break;
      case 51:
         this.renderBrokenItemStack(this.getItemStackFromSlot(EquipmentSlotType.LEGS));
         break;
      case 52:
         this.renderBrokenItemStack(this.getItemStackFromSlot(EquipmentSlotType.FEET));
         break;
      case 54:
         HoneyBlock.func_226936_b_(this);
      }

   }

   /**
    * sets the dead flag. Used when you fall off the bottom of the world.
    */
   protected void outOfWorld() {
      this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
   }

   public IAttributeInstance getAttribute(IAttribute attribute) {
      return this.getAttributes().getAttributeInstance(attribute);
   }

   /**
    * Returns this entity's attribute map (where all its attributes are stored)
    */
   public AbstractAttributeMap getAttributes() {
      if (this.attributes == null) {
         this.attributes = new AttributeMap();
      }

      return this.attributes;
   }

   public CreatureAttribute getCreatureAttribute() {
      return CreatureAttribute.UNDEFINED;
   }

   public ItemStack getHeldItemMainhand() {
      return this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
   }

   public ItemStack getHeldItemOffhand() {
      return this.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
   }

   public ItemStack getHeldItem(Hand hand) {
      if (hand == Hand.MAIN_HAND) {
         return this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
      } else if (hand == Hand.OFF_HAND) {
         return this.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
      } else {
         throw new IllegalArgumentException("Invalid hand " + hand);
      }
   }

   public void setHeldItem(Hand hand, ItemStack stack) {
      if (hand == Hand.MAIN_HAND) {
         this.setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
      } else {
         if (hand != Hand.OFF_HAND) {
            throw new IllegalArgumentException("Invalid hand " + hand);
         }

         this.setItemStackToSlot(EquipmentSlotType.OFFHAND, stack);
      }

   }

   public boolean hasItemInSlot(EquipmentSlotType slotIn) {
      return !this.getItemStackFromSlot(slotIn).isEmpty();
   }

   public abstract Iterable<ItemStack> getArmorInventoryList();

   public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);

   public abstract void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack);

   public float func_213343_cS() {
      Iterable<ItemStack> iterable = this.getArmorInventoryList();
      int i = 0;
      int j = 0;

      for(ItemStack itemstack : iterable) {
         if (!itemstack.isEmpty()) {
            ++j;
         }

         ++i;
      }

      return i > 0 ? (float)j / (float)i : 0.0F;
   }

   /**
    * Set sprinting switch for Entity.
    */
   public void setSprinting(boolean sprinting) {
      super.setSprinting(sprinting);
      IAttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      if (iattributeinstance.getModifier(SPRINTING_SPEED_BOOST_ID) != null) {
         iattributeinstance.removeModifier(SPRINTING_SPEED_BOOST);
      }

      if (sprinting) {
         iattributeinstance.applyModifier(SPRINTING_SPEED_BOOST);
      }

   }

   /**
    * Returns the volume for the sounds this mob makes.
    */
   protected float getSoundVolume() {
      return 1.0F;
   }

   /**
    * Gets the pitch of living sounds in living entities.
    */
   protected float getSoundPitch() {
      return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
   }

   /**
    * Dead and sleeping entities cannot move
    */
   protected boolean isMovementBlocked() {
      return this.getHealth() <= 0.0F;
   }

   /**
    * Moves the entity to a position out of the way of its mount.
    */
   private void dismountEntity(Entity entityIn) {
      if (this.world.getBlockState(new BlockPos(entityIn)).getBlock().isIn(BlockTags.PORTALS)) {
         this.setPosition(entityIn.getPosX(), entityIn.getPosYHeight(1.0D) + 0.001D, entityIn.getPosZ());
      } else if (!(entityIn instanceof BoatEntity) && !(entityIn instanceof AbstractHorseEntity)) {
         double d1 = entityIn.getPosX();
         double d13 = entityIn.getPosYHeight(1.0D);
         double d3 = entityIn.getPosZ();
         Direction direction = entityIn.getAdjustedHorizontalFacing();
         if (direction != null && direction.getAxis() != Direction.Axis.Y) {
            Direction direction1 = direction.rotateY();
            int[][] aint1 = new int[][]{{0, 1}, {0, -1}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}, {-1, 0}, {1, 0}, {0, 1}};
            double d14 = Math.floor(this.getPosX()) + 0.5D;
            double d15 = Math.floor(this.getPosZ()) + 0.5D;
            double d16 = this.getBoundingBox().maxX - this.getBoundingBox().minX;
            double d17 = this.getBoundingBox().maxZ - this.getBoundingBox().minZ;
            AxisAlignedBB axisalignedbb3 = new AxisAlignedBB(d14 - d16 / 2.0D, entityIn.getBoundingBox().minY, d15 - d17 / 2.0D, d14 + d16 / 2.0D, Math.floor(entityIn.getBoundingBox().minY) + (double)this.getHeight(), d15 + d17 / 2.0D);

            for(int[] aint : aint1) {
               double d9 = (double)(direction.getXOffset() * aint[0] + direction1.getXOffset() * aint[1]);
               double d10 = (double)(direction.getZOffset() * aint[0] + direction1.getZOffset() * aint[1]);
               double d11 = d14 + d9;
               double d12 = d15 + d10;
               AxisAlignedBB axisalignedbb2 = axisalignedbb3.offset(d9, 0.0D, d10);
               if (this.world.hasNoCollisions(this, axisalignedbb2)) {
                  BlockPos blockpos2 = new BlockPos(d11, this.getPosY(), d12);
                  if (this.world.getBlockState(blockpos2).isTopSolid(this.world, blockpos2, this)) {
                     this.setPositionAndUpdate(d11, this.getPosY() + 1.0D, d12);
                     return;
                  }

                  BlockPos blockpos1 = new BlockPos(d11, this.getPosY() - 1.0D, d12);
                  if (this.world.getBlockState(blockpos1).isTopSolid(this.world, blockpos1, this) || this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER)) {
                     d1 = d11;
                     d13 = this.getPosY() + 1.0D;
                     d3 = d12;
                  }
               } else {
                  BlockPos blockpos = new BlockPos(d11, this.getPosY() + 1.0D, d12);
                  if (this.world.hasNoCollisions(this, axisalignedbb2.offset(0.0D, 1.0D, 0.0D)) && this.world.getBlockState(blockpos).isTopSolid(this.world, blockpos, this)) {
                     d1 = d11;
                     d13 = this.getPosY() + 2.0D;
                     d3 = d12;
                  }
               }
            }
         }

         this.setPositionAndUpdate(d1, d13, d3);
      } else {
         double d0 = (double)(this.getWidth() / 2.0F + entityIn.getWidth() / 2.0F) + 0.4D;
         AxisAlignedBB axisalignedbb = entityIn.getBoundingBox();
         float f;
         double d2;
         int i;
         if (entityIn instanceof BoatEntity) {
            d2 = axisalignedbb.maxY;
            i = 2;
            f = 0.0F;
         } else {
            d2 = axisalignedbb.minY;
            i = 3;
         }

         AxisAlignedBB axisalignedbb1 = this.getBoundingBox().offset(-this.getPosX(), -this.getPosY(), -this.getPosZ());
         ImmutableSet<Entity> immutableset = ImmutableSet.of(this, entityIn);
         double d7 = 0.001D;

         for(int j = 0; j < i; ++j) {
            double d8 = d2 + d7;

            ++d7;
         }

         this.setPosition(entityIn.getPosX(), entityIn.getPosYHeight(1.0D) + 0.001D, entityIn.getPosZ());
      }
   }

   @OnlyIn(Dist.CLIENT)
   public boolean getAlwaysRenderNameTagForRender() {
      return this.isCustomNameVisible();
   }

   protected float getJumpUpwardsMotion() {
      return 0.42F * this.getJumpFactor();
   }

   /**
    * Causes this entity to do an upwards motion (jumping).
    */
   protected void jump() {
      float f = this.getJumpUpwardsMotion();
      if (this.isPotionActive(Effects.JUMP_BOOST)) {
         f += 0.1F * (float)(this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1);
      }

      Vec3d vec3d = this.getMotion();
      this.setMotion(vec3d.x, (double)f, vec3d.z);
      if (this.isSprinting()) {
         float f1 = this.rotationYaw * ((float)Math.PI / 180F);
         this.setMotion(this.getMotion().add((double)(-MathHelper.sin(f1) * 0.2F), 0.0D, (double)(MathHelper.cos(f1) * 0.2F)));
      }

      this.isAirBorne = true;
   }

   @OnlyIn(Dist.CLIENT)
   protected void handleFluidSneak() {
      this.setMotion(this.getMotion().add(0.0D, (double)-0.04F  * this.getAttribute(SWIM_SPEED).getValue(), 0.0D));
   }

   protected void handleFluidJump(Tag<Fluid> fluidTag) {
      this.setMotion(this.getMotion().add(0.0D, (double)0.04F * this.getAttribute(SWIM_SPEED).getValue(), 0.0D));
   }

   protected float getWaterSlowDown() {
      return 0.8F;
   }

   public void travel(Vec3d p_213352_1_) {

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

   private float getRelevantMoveFactor(float p_213335_1_) {
      return this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.jumpMovementFactor;
   }

   /**
    * the movespeed used for the new AI system
    */
   public float getAIMoveSpeed() {
      return this.landMovementFactor;
   }

   /**
    * set the movespeed used for the new AI system
    */
   public void setAIMoveSpeed(float speedIn) {
      this.landMovementFactor = speedIn;
   }

   public boolean attackEntityAsMob(Entity entityIn) {
      this.setLastAttackedEntity(entityIn);
      return false;
   }

   /**
    * Called to update the entity's position/logic.
    */
   public void tick() {
      super.tick();
      if (!this.world.isRemote) {
         int i = this.getArrowCountInEntity();
         if (i > 0) {
            if (this.arrowHitTimer <= 0) {
               this.arrowHitTimer = 20 * (30 - i);
            }

            --this.arrowHitTimer;
            if (this.arrowHitTimer <= 0) {
               this.setArrowCountInEntity(i - 1);
            }
         }

         int j = this.getBeeStingCount();
         if (j > 0) {
            if (this.beeStingRemovalCooldown <= 0) {
               this.beeStingRemovalCooldown = 20 * (30 - j);
            }

            --this.beeStingRemovalCooldown;
            if (this.beeStingRemovalCooldown <= 0) {
               this.setBeeStingCount(j - 1);
            }
         }

         for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            ItemStack itemstack;
            switch(equipmentslottype.getSlotType()) {
            case HAND:
               itemstack = this.handInventory.get(equipmentslottype.getIndex());
               break;
            case ARMOR:
               itemstack = this.armorArray.get(equipmentslottype.getIndex());
               break;
            default:
               continue;
            }

            ItemStack itemstack1 = this.getItemStackFromSlot(equipmentslottype);
            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
               if (!itemstack1.equals(itemstack, true))
               ((ServerWorld)this.world).getChunkProvider().sendToAllTracking(this, new SEntityEquipmentPacket(this.getEntityId(), equipmentslottype, itemstack1));
               if (!itemstack.isEmpty()) {
                  this.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentslottype));
               }

               if (!itemstack1.isEmpty()) {
                  this.getAttributes().applyAttributeModifiers(itemstack1.getAttributeModifiers(equipmentslottype));
               }

               switch(equipmentslottype.getSlotType()) {
               case HAND:
                  this.handInventory.set(equipmentslottype.getIndex(), itemstack1.copy());
                  break;
               case ARMOR:
                  this.armorArray.set(equipmentslottype.getIndex(), itemstack1.copy());
               }
            }
         }


         if (!this.glowing) {
            boolean flag = this.isPotionActive(Effects.GLOWING);
            if (this.getFlag(6) != flag) {
               this.setFlag(6, flag);
            }
         }
      }

      this.livingTick();
      double d0 = this.getPosX() - this.prevPosX;
      double d1 = this.getPosZ() - this.prevPosZ;
      float f2 = (float)(d0 * d0 + d1 * d1);
      float f3 = this.renderYawOffset;
      float f4 = 0.0F;
      this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
      float f5 = 0.0F;
      if (f2 > 0.0025000002F) {
         f5 = 1.0F;
         f4 = (float)Math.sqrt((double)f2) * 3.0F;
         float f = (float)MathHelper.atan2(d1, d0) * (180F / (float)Math.PI) - 90.0F;
         float f1 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f);
         if (95.0F < f1 && f1 < 265.0F) {
            f3 = f - 180.0F;
         } else {
            f3 = f;
         }
      }

      if (this.swingProgress > 0.0F) {
         f3 = this.rotationYaw;
      }

      if (!this.onGround) {
         f5 = 0.0F;
      }

      this.onGroundSpeedFactor += (f5 - this.onGroundSpeedFactor) * 0.3F;
      this.world.getProfiler().startSection("headTurn");
      f4 = this.updateDistance(f3, f4);
      this.world.getProfiler().endSection();
      this.world.getProfiler().startSection("rangeChecks");

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
         this.prevRenderYawOffset -= 360.0F;
      }

      while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
         this.prevRenderYawOffset += 360.0F;
      }

      while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
         this.prevRotationYawHead -= 360.0F;
      }

      while(this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
         this.prevRotationYawHead += 360.0F;
      }

      this.world.getProfiler().endSection();
      this.movedDistance += f4;

   }

   protected float updateDistance(float p_110146_1_, float p_110146_2_) {
      float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
      this.renderYawOffset += f * 0.3F;
      float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
      boolean flag = f1 < -90.0F || f1 >= 90.0F;
      if (f1 < -75.0F) {
         f1 = -75.0F;
      }

      if (f1 >= 75.0F) {
         f1 = 75.0F;
      }

      this.renderYawOffset = this.rotationYaw - f1;
      if (f1 * f1 > 2500.0F) {
         this.renderYawOffset += f1 * 0.2F;
      }

      if (flag) {
         p_110146_2_ *= -1.0F;
      }

      return p_110146_2_;
   }

   /**
    * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
    * use this to react to sunlight and start to burn.
    */
   public void livingTick() {
      if (this.jumpTicks > 0) {
         --this.jumpTicks;
      }

      if (this.canPassengerSteer()) {
         this.newPosRotationIncrements = 0;
         this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
      }

      if (this.newPosRotationIncrements > 0) {
         double d0 = this.getPosX() + (this.interpTargetX - this.getPosX()) / (double)this.newPosRotationIncrements;
         double d2 = this.getPosY() + (this.interpTargetY - this.getPosY()) / (double)this.newPosRotationIncrements;
         double d4 = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / (double)this.newPosRotationIncrements;
         double d6 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
         this.rotationYaw = (float)((double)this.rotationYaw + d6 / (double)this.newPosRotationIncrements);
         this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
         --this.newPosRotationIncrements;
         this.setPosition(d0, d2, d4);
         this.setRotation(this.rotationYaw, this.rotationPitch);
      }

      if (this.interpTicksHead > 0) {
         this.rotationYawHead = (float)((double)this.rotationYawHead + MathHelper.wrapDegrees(this.interpTargetHeadYaw - (double)this.rotationYawHead) / (double)this.interpTicksHead);
         --this.interpTicksHead;
      }

      Vec3d vec3d = this.getMotion();
      double d1 = vec3d.x;
      double d3 = vec3d.y;
      double d5 = vec3d.z;
      if (Math.abs(vec3d.x) < 0.003D) {
         d1 = 0.0D;
      }

      if (Math.abs(vec3d.y) < 0.003D) {
         d3 = 0.0D;
      }

      if (Math.abs(vec3d.z) < 0.003D) {
         d5 = 0.0D;
      }

      this.setMotion(d1, d3, d5);
      this.world.getProfiler().startSection("ai");
      if (this.isMovementBlocked()) {
         this.isJumping = false;
         this.moveStrafing = 0.0F;
         this.moveForward = 0.0F;
      }

      this.world.getProfiler().endSection();
      this.world.getProfiler().startSection("jump");
      if (this.isJumping) {
         if (!(this.submergedHeight > 0.0D) || this.onGround && !(this.submergedHeight > 0.4D)) {
            if (this.isInLava()) {
               this.handleFluidJump(FluidTags.LAVA);
            } else if ((this.onGround || this.submergedHeight > 0.0D && this.submergedHeight <= 0.4D) && this.jumpTicks == 0) {
               this.jump();
               this.jumpTicks = 10;
            }
         } else {
            this.handleFluidJump(FluidTags.WATER);
         }
      } else {
         this.jumpTicks = 0;
      }

      this.world.getProfiler().endSection();
      this.world.getProfiler().startSection("travel");
      this.moveStrafing *= 0.98F;
      this.moveForward *= 0.98F;
      this.updateElytra();
      AxisAlignedBB axisalignedbb = this.getBoundingBox();
      this.travel(new Vec3d((double)this.moveStrafing, (double)this.moveVertical, (double)this.moveForward));
      this.world.getProfiler().endSection();
      this.world.getProfiler().startSection("push");
      if (this.spinAttackDuration > 0) {
         --this.spinAttackDuration;
         this.updateSpinAttack(axisalignedbb, this.getBoundingBox());
      }

      this.collideWithNearbyEntities();
      this.world.getProfiler().endSection();
   }

   /**
    * Called each tick. Updates state for the elytra.
    */
   private void updateElytra() {
      boolean flag = this.getFlag(7);
      if (flag && !this.onGround && !this.isPassenger()) {
         ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.CHEST);
         if (itemstack.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemstack)) {
            flag = true;
            if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0) {
            }
         } else {
            flag = false;
         }
      } else {
         flag = false;
      }

      if (!this.world.isRemote) {
         this.setFlag(7, flag);
      }

   }

   protected void updateEntityActionState() {
   }

   protected void collideWithNearbyEntities() {
      List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), EntityPredicates.pushableBy(this));
      if (!list.isEmpty()) {
         int i = this.world.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
         if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
            int j = 0;

            for(int k = 0; k < list.size(); ++k) {
               if (!list.get(k).isPassenger()) {
                  ++j;
               }
            }

            if (j > i - 1) {
               this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
            }
         }

         for(int l = 0; l < list.size(); ++l) {
            Entity entity = list.get(l);
            this.collideWithEntity(entity);
         }
      }

   }

   protected void updateSpinAttack(AxisAlignedBB p_204801_1_, AxisAlignedBB p_204801_2_) {
      AxisAlignedBB axisalignedbb = p_204801_1_.union(p_204801_2_);
      List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);
      if (!list.isEmpty()) {
         for(int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);
            if (entity instanceof LivingEntity) {
               this.spinAttack((LivingEntity)entity);
               this.spinAttackDuration = 0;
               this.setMotion(this.getMotion().scale(-0.2D));
               break;
            }
         }
      } else if (this.collidedHorizontally) {
         this.spinAttackDuration = 0;
      }


   }

   protected void collideWithEntity(Entity entityIn) {
      entityIn.applyEntityCollision(this);
   }

   protected void spinAttack(LivingEntity p_204804_1_) {
   }

   public void startSpinAttack(int p_204803_1_) {
      this.spinAttackDuration = p_204803_1_;

   }

   public boolean isSpinAttacking() {
      return (this.dataManager.get(LIVING_FLAGS) & 4) != 0;
   }

   /**
    * Dismounts this entity from the entity it is riding.
    */
   public void stopRiding() {
      Entity entity = this.getRidingEntity();
      super.stopRiding();
      if (entity != null && entity != this.getRidingEntity() && !this.world.isRemote) {
         this.dismountEntity(entity);
      }

   }

   /**
    * Handles updating while riding another entity
    */
   public void updateRidden() {
      super.updateRidden();
      this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
      this.onGroundSpeedFactor = 0.0F;
      this.fallDistance = 0.0F;
   }
}
