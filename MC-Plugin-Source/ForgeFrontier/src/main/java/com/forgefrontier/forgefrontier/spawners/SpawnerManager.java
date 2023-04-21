package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomMob;
import com.forgefrontier.forgefrontier.utils.Manager;
import com.forgefrontier.forgefrontier.utils.QuadTree;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class SpawnerManager extends Manager implements Listener {

    Map<UUID, Map<String, SpawnerInstance>> spawnerInstanceTree;
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

        //TODO: database integration
    }

    public Spawner getSpawner(String spawnerId) {
        return this.spawners.get(spawnerId);
    }

    public String getEntityCode(String id) {
        return this.entityCodes.get(id);
    }

    @Override
    public void disable() {
        //TODO: update database on exit
        /*
        ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "Disabling SpawnerManager");
        ArrayList<JSONObject> jsonList = new ArrayList<>();
        for (String key : spawners.keySet()) {
            Spawner spawner = spawners.get(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", spawner.getId());
            jsonObject.put("friendlyName", spawner.getFriendlyName());
            jsonObject.put("materialRepresentation", spawner.getMaterialRepresentation().toString());
            jsonObject.put("entityCode", spawner.getEntityCode());
            jsonList.add(jsonObject);
        }

        try (FileWriter file = new FileWriter("src\\main\\resources\\spawners.json")) {
            file.write(jsonList.toString());
            ForgeFrontier.getInstance().getLogger().log(Level.WARNING, jsonList.toString() + " | " + file.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }

    public void initializeSpawnerInstance(SpawnerInstance spawnerInstance, Consumer<Boolean> callback) {
        boolean success = addSpawnerInstance(spawnerInstance);
        if(!success) {
            callback.accept(false);
            return;
        }
        //TODO: implement database functionality (add spawner to database list)
        callback.accept(true);
    }

    public boolean addSpawnerInstance(SpawnerInstance spawnerInstance) {
        Location location = spawnerInstance.getLocation().clone();
        if (location.getWorld() == null) {
            return false;
        }
        Map<String, SpawnerInstance> tree = spawnerInstanceTree.get(location.getWorld().getUID());
        if (tree == null) {
            tree = new HashMap<>();
            spawnerInstanceTree.put(location.getWorld().getUID(), tree);
        }
        if (tree.get(spawnerInstance.getSpawner().getId()) != null) {
            tree.remove(tree.get(spawnerInstance.getSpawner().getId()).getSpawner().getId());
            return true;
        }
        tree.put(spawnerInstance.getSpawner().getId(), spawnerInstance);
        plugin.getLogger().log(Level.WARNING, location.getBlock().getLocation().toString());
        return true;
    }

    public void removeSpawnerInstance(SpawnerInstance spawnerInstance) {
        World world = spawnerInstance.getLocation().getWorld();
        Map<String, SpawnerInstance> tree = spawnerInstanceTree.get(world.getUID());
        if (tree == null) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Spawner Instance to remove.");
            return;
        }
        tree.remove(spawnerInstance.getSpawner().getId());
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
