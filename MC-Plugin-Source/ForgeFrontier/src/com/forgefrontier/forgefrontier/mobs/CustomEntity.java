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
    public CraftEntity spawnCustomEntity(Location loc, CraftEntity craftEntity);

    /** creates an entity instance of the class to be spawned in the world */
    public CraftEntity createCustomEntity();

    /** returns the class type of the craftentity corresponding to the CustomEntity wrapper */
    public Class<? extends CraftEntity> getCorrespondingCraftEntity();

    public int getID();
}
