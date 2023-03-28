package com.forgefrontier.forgefrontier.mobs.chickens;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;

public class CustomChickenEntity extends Chicken {

    private long lastDamageTime = 0;

    public CustomChickenEntity(EntityType<? extends Chicken> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0, 10));
        this.goalSelector.addGoal(2, new FollowMobGoal(this, 1.0, 10, 1));
        //this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0, 25));
    }

    @Override
    public void tick() {
        super.tick();

        Player nearestPlayer = this.getLevel().getNearestPlayer(this, 15.0);
        if (nearestPlayer != null) {
            this.setTarget(nearestPlayer);
            org.bukkit.entity.Player spigotPlayer = Bukkit.getPlayer(nearestPlayer.getUUID());
            if (spigotPlayer != null && System.currentTimeMillis() - lastDamageTime > 1000 &&
                    nearestPlayer.distanceTo(this) <= 1) {
                spigotPlayer.damage(1);
                lastDamageTime = System.currentTimeMillis();
            }
        } else {
            this.setTarget(null);
        }
    }
}
