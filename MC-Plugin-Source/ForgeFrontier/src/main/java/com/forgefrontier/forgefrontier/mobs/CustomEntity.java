package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.spawners.SpawnerInstance;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Interface to handle all custom actions of custom mobs in Forge Frontier
 */
public interface CustomEntity {
    void customTick();
    void registerSpawner(SpawnerInstance spawner);
    void updateSpawnerOnDeath();
}
