package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Manager;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.schedule.Activity;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.loot.LootContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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
        mob.spawnCustomEntity(player.getLocation(), mob.createCustomEntity());
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
        CraftChicken entity = (CraftChicken) event.getEntity(); //TODO: Change this to more generic class type

        // Updates the boss health bar if the entity contains metadata for it
        if (entity.hasMetadata("bossbar")) {
            Object o = entity.getMetadata("bossbar").get(0).value();
            if (o instanceof BossBar bossBar) {
                System.out.println("UPDATE BAR");
                double currHealth = (entity.getHealth() - event.getDamage());
                if (currHealth < 0)
                    currHealth = 0;
                bossBar.setProgress(currHealth / entity.getMaxHealth());
            }
        }

        if (entity.hasMetadata("code")) {
            //TODO: this is temporary, would re-create instance and call function to execute same
            // entity.getHandle().du().a(Activity.b);
            // entity.setTarget((Player) event.getDamager());
            // entity.attack(event.getDamager());
        }
    }

    @EventHandler
    public void onEntityDeath (EntityDeathEvent event) {
        System.out.println("REGISTERED DEATH EVENT");
        LivingEntity entity = event.getEntity();

        // Removes the boss health bar if the entity contains metadata for it
        if (entity.hasMetadata("bossbar")) {
            Object o = entity.getMetadata("bossbar").get(0).value();
            if (o instanceof BossBar bossBar) {
                bossBar.setProgress(0);
                bossBar.removeAll();
            }
        }
    }

    private LootContext buildLootContext(LivingEntity entity) {
        return null;
    }

    /** getter for the entities hashmap in the manager */
    public Map<String, CustomMob> getEntities() {
        return entities;
    }
}
