package com.forgefrontier.forgefrontier.mobs.chickens;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CustomChickenEntity extends Chicken {
    public CustomChickenEntity(EntityType<? extends Chicken> entitytypes, Level world) {
        super(entitytypes, world);

        /*
        AttributeMap map = this.getAttributes();
        AttributeInstance attackAttribute = new AttributeInstance(map, Attributes.ATTACK_DAMAGE, 10);
         */
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(this, Player.class, 100, true, true, null));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1, true));
    }
}
