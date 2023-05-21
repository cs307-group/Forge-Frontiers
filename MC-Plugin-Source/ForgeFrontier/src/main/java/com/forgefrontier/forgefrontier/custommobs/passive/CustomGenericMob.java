package com.forgefrontier.forgefrontier.custommobs.passive;

import com.forgefrontier.forgefrontier.custommobs.CustomEntityHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.Level;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMob;

import java.util.List;

public class CustomGenericMob extends CraftMob {

    private static class CustomMobImpl extends PathfinderMob {

        CustomEntityHandler handler;

        protected CustomMobImpl(EntityType<? extends PathfinderMob> entitytype, Level world, CustomEntityHandler handler) {
            super(entitytype, world);
            this.handler = handler;
            this.handler.init(this);
            this.registerGoals();
        }

        // Could override dropAllDeathLoot(DamageSource damagesource); instead.
        @Override
        protected void dropFromLootTable(DamageSource damagesource, boolean flag) {
            handler.dropLoot(this);
        }

        @Override
        public boolean hurt(DamageSource damagesource, float f) {
            boolean res = super.hurt(damagesource, f);
            if(res)
                handler.hurt(this);
            return res;
        }

        @Override
        public void die(DamageSource damagesource) {
            super.die(damagesource);
            handler.runOnDeath(this);
        }

        @Override
        public void tick() {
            super.tick();
            handler.tick(this);
        }

        @Override
        protected void registerGoals() {
            // Needed to prevent it running before handler has been set.
            if(this.handler == null)
                return;
            List<WrappedGoal> goals = this.handler.getNMSGoals(this);
            for(WrappedGoal goal: goals) {
                this.goalSelector.addGoal(goal.getPriority(), goal.getGoal());
            }

            List<WrappedGoal> targetGoals = this.handler.getNMSTargetGoals(this);
            for(WrappedGoal goal: targetGoals) {
                this.targetSelector.addGoal(goal.getPriority(), goal.getGoal());
            }
        }

    }

    public CustomGenericMob(CustomEntityHandler handler, EntityType<? extends PathfinderMob> entityType, ServerLevel level) {
        super(level.getCraftServer(), new CustomMobImpl(entityType, level, handler));
    }

}
