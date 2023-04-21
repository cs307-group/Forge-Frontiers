package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mining.MiningArea;
import com.forgefrontier.forgefrontier.mining.MiningManager;
import com.forgefrontier.forgefrontier.mining.MiningWandItem;
import com.forgefrontier.forgefrontier.mobs.CustomMob;
import com.forgefrontier.forgefrontier.utils.Manager;
import com.forgefrontier.forgefrontier.utils.QuadTree;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class SpawnerManager extends Manager implements Listener {

    Map<String, Map<String, SpawnerInstance>> spawnerInstanceTree;
    Map<String, Spawner> spawners;

    Map<String, String> entityCodes;

    public SpawnerManager(ForgeFrontier plugin) {
        super(plugin);
        this.spawnerInstanceTree = new HashMap<>();
        this.spawners = new HashMap<>();
        this.entityCodes = new HashMap<>();
    }

    @Override
    public void init() {
        this.plugin.getCustomItemManager().registerCustomItem(new SpawnBlockItem());

        int spawnerInd = 0;
        ConfigurationSection configSection;
        while((configSection = this.plugin.getConfig("spawners").getConfigurationSection("spawners." + spawnerInd)) != null) {
            Spawner spawner = new Spawner(configSection);
            this.spawners.put(spawner.getEntityCode(), spawner);
            spawnerInd += 1;
        }

        int instanceInd = 0;
        while((configSection = this.plugin.getConfig("spawners").getConfigurationSection("instances." + instanceInd)) != null) {
            SpawnerInstance instance = new SpawnerInstance(configSection);
            boolean success = this.addSpawnerInstance(instance);
            if(!success) {
                ForgeFrontier.getInstance().getLogger().info("Unable to import spawner.");
            }
            instanceInd += 1;
        }

    }

    @Override
    public void disable() {
        this.save();
    }

    public void save() {
        FileConfiguration config = this.plugin.getConfig("spawners");
        config.set("spawners", null);
        int spawnerInd = 0;
        for(String name: this.spawners.keySet()) {
            Spawner spawner = this.spawners.get(name);
            spawner.save(config.createSection("spawners." + spawnerInd));
            spawnerInd += 1;
        }
        config.set("instances", null);
        int instanceInd = 0;
        for(String world: this.spawnerInstanceTree.keySet()) {
            for(SpawnerInstance instance: this.spawnerInstanceTree.get(world).values()) {
                instance.save(config.createSection("instances." + instanceInd));
                instanceInd += 1;
            }
        }
        try {
            config.save(new File(plugin.getDataFolder(), "spawners.yml"));
        } catch (IOException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to save spawner configuration.");
        }
    }

    public Spawner getSpawner(String spawnerId) {
        return this.spawners.get(spawnerId);
    }

    public String getEntityCode(String id) {
        return this.entityCodes.get(id);
    }

    public boolean addSpawnerInstance(SpawnerInstance spawnerInstance) {
        Location location = spawnerInstance.getLocation().clone();
        Map<String, SpawnerInstance> tree = spawnerInstanceTree.get(spawnerInstance.worldName);
        if (tree == null) {
            tree = new HashMap<>();
            spawnerInstanceTree.put(spawnerInstance.worldName, tree);
        }
        if (tree.get(spawnerInstance.getId()) != null) {
            tree.remove(spawnerInstance.getId());
            return true;
        }
        tree.put(spawnerInstance.getId(), spawnerInstance);
       // plugin.getLogger().log(Level.WARNING, location.getBlock().getLocation().toString());
        return true;
    }

    public void removeSpawnerInstance(SpawnerInstance spawnerInstance) {
        Map<String, SpawnerInstance> tree = spawnerInstanceTree.get(spawnerInstance.worldName);
        if (tree == null) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Spawner Instance to remove.");
            return;
        }
        tree.remove(spawnerInstance.getId());
        spawnerInstance.getLocation().getBlock().setType(Material.AIR);
        spawnerInstance.destroy();
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.hasMetadata("spawner-block")) {
            plugin.getLogger().log(Level.WARNING, "IS SPAWNER BLOCK");
            Map<String, SpawnerInstance> insts = spawnerInstanceTree.get(event.getBlock().getWorld().getUID());
            if (insts == null) return;
            plugin.getLogger().log(Level.WARNING, "NON-NULL INSTS");
            SpawnerInstance instance = insts.get((String) block.getMetadata("spawner-block").get(0).value());
            if (instance == null) return;
            plugin.getLogger().log(Level.WARNING, "SPAWNER BREAK EVENT COMPLETE");
            removeSpawnerInstance(instance);
        } else {
            plugin.getLogger().log(Level.WARNING, "NOT SPAWNER BLOCK");
        }
    }

    public Map<String, Spawner> getSpawners() {
        return this.spawners;
    }

    public void registerSpawner(String entityCode) {
        this.spawners.put(entityCode, new Spawner(entityCode));
    }
}
