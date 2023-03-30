package com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.eggsplosive;

import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.eggbox.EggBoxEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class EggsplosiveEntity extends EggBoxEntity {

    public EggsplosiveEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
    }
}
