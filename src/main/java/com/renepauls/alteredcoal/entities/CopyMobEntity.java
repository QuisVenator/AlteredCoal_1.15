package com.renepauls.alteredcoal.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public abstract class CopyMobEntity extends LivingEntity {
   protected CopyMobEntity(EntityType<? extends CopyMobEntity> type, World worldIn) {
      super(type, worldIn);
   }


   public final boolean processInitialInteract(PlayerEntity player, Hand hand) {
      if (!this.isAlive()) {
    	  return false;
      }  else {
    	  return this.processInteract(player, hand) ? true : super.processInitialInteract(player, hand);
      }
   }

   protected boolean processInteract(PlayerEntity player, Hand hand) {
      return false;
   }

   /*
    * FOR FUCKS SAKE THIS HAS TO STAY LIKE THIS!!!!!!! 
    * otherwise it just fucks everything up, doesn't go up blocks, doesn't continue rolling, etc...
    * */
   public boolean canPassengerSteer() {
      return false;
   }
   ///////////////////////////////////////////////////////confirmed stay
}