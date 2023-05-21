package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomCraftEntity;
import com.forgefrontier.forgefrontier.mobs.CustomEntity;
import com.forgefrontier.forgefrontier.mobs.CustomMob;
import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;
import java.util.logging.Level;

public class SpawnerInstance implements Locatable {

    String id;
    int spawnDelay;

    Spawner spawner;
    String worldName;
    Location location;
    Location spawnLoc;
    String entityCode;
    CustomEntity mob;
    SpawnerInstance self;

    public SpawnerInstance(Spawner spawner, Location location) {
        this.id = UUID.randomUUID().toString();
        this.spawner = spawner;
        this.location = location;
        this.worldName = this.location.getWorld().getName();
        spawnLoc = location.clone();
        spawnLoc.add(0, 1, 0);
        entityCode = spawner.getEntityCode();
        spawnDelay = 100;
        self = this;
        CraftEntity craftEntity = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity(entityCode, spawnLoc);
        if (craftEntity.getHandle() instanceof CustomEntity customEntity) {
            //ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "SUCCESSFULLY SET MOB");
            mob = customEntity;
            mob.registerSpawner(this);
        }
    }

    public SpawnerInstance(ConfigurationSection config) {
        this.id = config.getString("id");
        this.spawner = ForgeFrontier.getInstance().getSpawnerManager().getSpawner(config.getString("spawner-id"));
        this.spawnDelay = config.getInt("spawn-delay");
        this.location = new Location(
            Bukkit.getWorld(config.getString("world")),
            config.getInt("x"),
            config.getInt("y"),
            config.getInt("z")
        );
        this.worldName = config.getString("world");
        spawnLoc = location.clone();
        spawnLoc.add(0, 1, 0);
        entityCode = spawner.getEntityCode();
        spawnDelay = 100;
        self = this;
        CraftEntity craftEntity = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity(entityCode, spawnLoc);
        if(craftEntity == null) {
            Bukkit.getScheduler().runTaskLater(ForgeFrontier.getInstance(), () -> {
                this.entityDeath();
            }, this.spawnDelay);
            return;
        }
        if (craftEntity.getHandle() instanceof CustomEntity customEntity) {
            //ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "SUCCESSFULLY SET MOB");
            mob = customEntity;
            mob.registerSpawner(this);
        }
    }

    public void save(ConfigurationSection config) {
        config.set("id", this.id);
        config.set("spawner-id", this.spawner.entityCode);
        config.set("spawn-delay", this.spawnDelay);
        config.set("world", this.worldName);
        config.set("x", this.location.getBlockX());
        config.set("y", this.location.getBlockY());
        config.set("z", this.location.getBlockZ());
    }

    public void entityDeath() {
        //ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "SPAWNER ENTITY DEATH");
        this.mob = null;
        Runnable runnable = () -> {
            CraftEntity craftEntity = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity(entityCode, spawnLoc);
            if(craftEntity == null || this.spawnLoc.getWorld() == null) {
                Bukkit.getScheduler().runTaskLater(ForgeFrontier.getInstance(), () -> {
                    this.spawnLoc.setWorld(Bukkit.getWorld(this.worldName));
                    this.entityDeath();
                }, this.spawnDelay);
                return;
            }
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
        return "(" + this.getId() + ", " + this.entityCode + ", " + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public Spawner getSpawner() {
        return this.spawner;
    }

    public void destroy() {
        self = null;
    }

    public String getId() {
        return this.id;
    }

}
