package com.forgefrontier.forgefrontier.custommobs.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

public class LookAtPlayerGoal extends Goal {

    public LookAtPlayerGoal(int priority) {
        super(priority);
    }

    @Override
    public net.minecraft.world.entity.ai.goal.Goal asNMSGoal(PathfinderMob mob) {
        return new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(mob, Player.class, 5, 1.0f);
    }

}
