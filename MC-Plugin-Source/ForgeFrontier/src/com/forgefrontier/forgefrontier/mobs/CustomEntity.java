package com.forgefrontier.forgefrontier.mobs;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;

/**
 * Interface to handle all custom actions of custom mobs in Forge Frontier
 */
public interface CustomEntity {

    /** defines the behaviour of the entity */
    public void executeBehavior();

    /** spawns the custom entity in the world at the specified location */
    public Entity spawnCustomEntity(Location loc);

    public void initHealthValues(double maxHealth, double health, CraftEntity entity);
}
