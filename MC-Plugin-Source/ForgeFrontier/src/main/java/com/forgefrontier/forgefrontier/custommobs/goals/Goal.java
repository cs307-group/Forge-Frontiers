package com.forgefrontier.forgefrontier.custommobs.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;

public abstract class Goal {

    int priority;

    public Goal(int priority) {
        this.priority = priority;
    }

    public abstract net.minecraft.world.entity.ai.goal.Goal asNMSGoal(PathfinderMob mob);

    public WrappedGoal asNMSWrappedGoal(PathfinderMob mob) {
        return new WrappedGoal(this.priority, this.asNMSGoal(mob));
    }



}
