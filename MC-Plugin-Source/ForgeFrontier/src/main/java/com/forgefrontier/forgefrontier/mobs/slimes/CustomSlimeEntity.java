package com.forgefrontier.forgefrontier.mobs.slimes;

import com.forgefrontier.forgefrontier.mobs.CustomEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public abstract class CustomSlimeEntity extends Slime implements CustomEntity {

    public CustomSlimeEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);
    }

    /**
     * Overwrites basic Slime AI
     */
    @Override
    protected void registerGoals() {
        // removes all previous AI
    }

    /**
     * Called once a tick to update the entity
     */
    @Override
    public void tick() {
        super.tick();
        this.customTick();
    }

    /**
     * Function to define AI of mob in
     */
    @Override
    public void customTick() {
    }
}
