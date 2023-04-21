package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomCraftEntity;
import com.forgefrontier.forgefrontier.mobs.CustomEntity;
import com.forgefrontier.forgefrontier.mobs.CustomMob;
import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Level;

public class SpawnerInstance implements Locatable {

    String databaseID;
    int spawnDelay;

    Spawner spawner;
    Location location;
    Location spawnLoc;
    String entityCode;
    CustomEntity mob;
    SpawnerInstance self;

    public SpawnerInstance(Spawner spawner, Location location) {
        this.spawner = spawner;
        this.location = location;
        spawnLoc = location.clone();
        spawnLoc.add(0, 1, 0);
        entityCode = spawner.getEntityCode();
        spawnDelay = 100;
        self = this;
        CraftEntity craftEntity = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity(entityCode, spawnLoc);
        if (craftEntity.getHandle() instanceof CustomEntity customEntity) {
            ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "SUCCESSFULLY SET MOB");
            mob = customEntity;
            mob.registerSpawner(this);
        }
    }

    // Database constructor
    public SpawnerInstance(Spawner spawner, Location location, String databaseID) {
        this.spawner = spawner;
        this.location = location;

        this.databaseID = databaseID;
    }

    public void entityDeath() {
        ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "SPAWNER ENTITY DEATH");
        this.mob = null;
        Runnable runnable = () -> {
            CraftEntity craftEntity = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity(entityCode, spawnLoc);
            if (craftEntity.getHandle() instanceof CustomEntity customEntity) {
                mob = customEntity;
                mob.registerSpawner(self);
            }
        };

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(ForgeFrontier.getInstance(), runnable, this.spawnDelay);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setDatabaseID(String databaseID) {
        this.databaseID = databaseID;
    }

    @Override
    public int getX() {
        return this.getLocation().getBlockX();
    }

    @Override
    public int getY() {
        return this.getLocation().getBlockY();
    }

    @Override
    public int getZ() {
        return this.getLocation().getBlockZ();
    }

    @Override
    public boolean isAt(int x, int y, int z) {
        return false;
    }

    @Override
    public String toString() {
        return "(" + this.spawner.getId() + ", " + this.entityCode + ", " + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public Spawner getSpawner() {
        return this.spawner;
    }

    public void destroy() {
        self = null;
    }
}
