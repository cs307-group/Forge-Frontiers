package com.forgefrontier.forgefrontier.mobs.slimes;

import com.forgefrontier.forgefrontier.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;

public abstract class CustomSlime extends CustomMob {

    /**
     * Constructor for a custom slime entity
     *
     * @param customName name of the custom slime
     */
    public CustomSlime(String customName) {
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
