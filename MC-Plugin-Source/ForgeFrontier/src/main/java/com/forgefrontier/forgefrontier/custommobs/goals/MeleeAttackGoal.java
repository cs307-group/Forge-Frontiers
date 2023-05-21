package com.forgefrontier.forgefrontier.custommobs.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

public class MeleeAttackGoal extends Goal {

    public MeleeAttackGoal(int priority) {
        super(priority);
    }

    @Override
    public net.minecraft.world.entity.ai.goal.Goal asNMSGoal(PathfinderMob mob) {
        return new net.minecraft.world.entity.ai.goal.MeleeAttackGoal(mob, 1.0, false);
    }

}
