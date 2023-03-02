package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles Events for Custom Mobs and includes methods to spawn/de-spawn them
 */
public class CustomEntityManager extends Manager implements Listener {

    Map<String, CustomMob> entities;

    /**
     * Constructor for the manager
     *
     * @param plugin the plugin instance for Forge Frontier
     */
    public CustomEntityManager(ForgeFrontier plugin) {
        super(plugin);
        this.entities = new HashMap<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void disable() {

    }

    /** Method to run to register a new CustomItem. If not run, the custom item will not be able to be identified. */
    public void registerCustomEntity(CustomMob mob) {
        this.entities.put(mob.getCode(), mob);
    }

    /**
     * Spawns an entity with the specified code
     *
     * @param code the code corresponding to the mob to be spawned
     * @param player the player spawning the mob
     * @return if the execution was successful
     */
    public static boolean spawnEntity(String code, Player player) {
        CustomMob mob = ForgeFrontier.getInstance().getCustomEntityManager().getEntities().get(code);
        if (mob == null) {
            return false;
        }
        System.out.println("Spawning at " + player.getLocation().toVector());
        mob.spawnCustomEntity(player.getLocation());
        return true;
    }

    /** getter for the entities hashmap in the manager */
    public Map<String, CustomMob> getEntities() {
        return entities;
    }
}
