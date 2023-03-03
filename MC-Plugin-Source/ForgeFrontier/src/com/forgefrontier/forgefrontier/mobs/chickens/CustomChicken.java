package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.mobs.CustomMob;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.*;


/**
 * Superclass for all chicken entities in Forge Frontier
 */
public abstract class CustomChicken extends CustomMob {

    /**
     * Constructor for a custom chicken entity
     *
     * @param customName name of the custom chicken
     */
    public CustomChicken(String customName) {
        super(customName);
    }


    /**
     * spawns an instance of the entity at the location
     *
     * @param loc the location the entity will be spawned
     */
    @Override
    public CraftEntity spawnCustomEntity(Location loc, CraftEntity craftEntity) {
        return super.spawnCustomEntity(loc, craftEntity);
    }

    /**
     * @return the corresponding CraftEntity wrapper class of this entity wrapper class
     */
    @Override
    public Class<? extends CraftEntity> getCorrespondingCraftEntity() {
        return CustomCraftChicken.class;
    }

    @Override
    public int getID() {
        return 93;
    }
}