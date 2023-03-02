package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.mobs.chickens.CustomCraftChicken;
import net.minecraft.server.level.WorldServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Abstract class to represent all custom mobs
 */
public abstract class CustomMob implements CustomEntity {

    /** the world that the entity will exist in */
    World world;

    /** The type of entity that the entity is */
    EntityType entityType;

    /** the String code that is representative of the entity */
    String code;
    Entity entity;
    CraftEntity craftEntity;

    /**
     * Basic constructor for a custom mob
     *
     * @param code the code that is representative of the entity
     * @param entityType the type of entity the mob is
     */
    public CustomMob(String code, EntityType entityType, CraftEntity craftEntity) {
        super();
        this.code = code;
        this.world = Bukkit.getWorld("world");
        this.entityType = entityType;
        this.craftEntity = craftEntity;
    }

    /**
     * spawns an instance of the entity at the location
     *
     * @param loc the lcoation the entity will be spawned
     */
    @Override
    public Entity spawnCustomEntity(Location loc) {
        WorldServer worldServer = ((CraftWorld)Bukkit.getServer().getWorld("world")).getHandle();
        this.entity = this.world.spawnEntity(loc, craftEntity.getType());
        return this.entity;
    }

    /** Returns the code attribute for the CustomMob */
    public String getCode() {
        return this.code;
    }

    @Override
    public void executeBehavior() {

    }
}
