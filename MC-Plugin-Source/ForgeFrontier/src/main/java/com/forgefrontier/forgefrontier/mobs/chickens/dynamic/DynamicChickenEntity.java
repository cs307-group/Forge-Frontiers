package com.forgefrontier.forgefrontier.mobs.chickens.dynamic;

import com.forgefrontier.forgefrontier.mobs.chickens.hostile.HostileChickenEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public class DynamicChickenEntity extends HostileChickenEntity {

    public DynamicChickenEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);
    }
}
