package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Abstract class to represent all custom mobs
 */
public abstract class CustomMob {

    /** the world that the entity will exist in */
    World world;

    /** The type of entity that the entity is */
    EntityType entityType;

    /** the String code that is representative of the entity */
    String code;

    /**
     * Basic constructor for a custom mob
     *
     * @param code the code that is representative of the entity
     */
    public CustomMob(String code) {
        super();
        this.code = code;
        this.world = Bukkit.getWorld("world");
    }

    /**
     * spawns an instance of the entity at the location
     *
     * @param loc the lcoation the entity will be spawned
     */
    public CraftEntity spawnCustomEntity(Location loc, CraftEntity craftEntity) {
        loc.getWorld();
        ServerLevel worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        worldServer.addFreshEntity(craftEntity.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        craftEntity.teleport(loc);
        return craftEntity;
    }

    /**
     * Used to create an entity instance of <mob-class> to spawn in the world
     *
     * @return entity spawned
     */
    public CraftEntity createCustomEntity(CraftWorld world) {
        return null;
    }

    /** Returns the code attribute for the CustomMob */
    public String getCode() {
        return this.code;
    }
}
