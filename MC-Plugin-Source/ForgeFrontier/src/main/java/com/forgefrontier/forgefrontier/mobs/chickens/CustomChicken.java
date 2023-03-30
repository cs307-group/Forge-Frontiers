package com.forgefrontier.forgefrontier.mobs.chickens;

import com.forgefrontier.forgefrontier.mobs.CustomMob;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;

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
}