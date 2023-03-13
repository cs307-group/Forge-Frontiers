package com.forgefrontier.forgefrontier.mobs.chickens;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public class CustomChickenEntity extends Chicken {
    public CustomChickenEntity(EntityType<? extends Chicken> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    protected void registerGoals() {

    }
}
