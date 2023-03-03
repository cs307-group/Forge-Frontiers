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
    @Override
    public CraftEntity spawnCustomEntity(Location loc, CraftEntity craftEntity) {
        WorldServer worldServer = ((CraftWorld)Bukkit.getServer().getWorld("world")).getHandle();
        worldServer.addFreshEntity(craftEntity.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        craftEntity.teleport(loc);
        if (craftEntity instanceof CustomCraftChicken) {
            System.out.println("CUSTOM MOB CHECK AS CUSTOM CRAFT CHICKEN");
        }
        return craftEntity;
    }

    /**
     * Used to create an entity instance of <mob-class> to spawn in the world
     *
     * @return entity spawned
     */
    public CraftEntity createCustomEntity() {
        return null;
    }

    /** Returns the code attribute for the CustomMob */
    public String getCode() {
        return this.code;
    }

    @Override
    public void executeBehavior() {

    }
}
