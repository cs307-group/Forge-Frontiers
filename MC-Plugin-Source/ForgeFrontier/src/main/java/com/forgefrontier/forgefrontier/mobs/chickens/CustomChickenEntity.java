package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.mobs.CustomEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

/**
 * Class that overwrites basic chicken AI and attributes, and allows for overwriting them
 */
public abstract class CustomChickenEntity extends Chicken implements CustomEntity {

    private final int expensiveTickVal = 40;
    private int expensiveTickCurrVal = 0;

    public CustomChickenEntity(EntityType<? extends Chicken> entityTypes, Level world) {
        super(entityTypes, world);
    }

    /**
     * Overwrites basic Chicken AI
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

    public boolean determineExpensive() {
        if (expensiveTickCurrVal == expensiveTickVal) {
            expensiveTickCurrVal = 0;
            return true;
        }
        expensiveTickCurrVal++;
        return false;
    }
}
