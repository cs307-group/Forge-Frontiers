package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.mobs.chickens.CustomCraftChicken;
import com.forgefrontier.forgefrontier.utils.Manager;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.ArrayList;
import java.util.Arrays;
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
        mob.spawnCustomEntity(player.getLocation(), mob.createCustomEntity());
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof CustomMob) {

        }
    }

    @EventHandler
    public void onEntityDeath (EntityDeathEvent event) {
        System.out.println("REGISTERED DEATH EVENT");
        LivingEntity entity = event.getEntity();
        if (entity instanceof CraftChicken) {
            System.out.println("IS CRAFT CHICKEN");
        }
        if (entity instanceof CustomCraftChicken) {
            System.out.println("IS CUSTOM CRAFT CHICKEN");
        }
        ArrayList<ItemStack> drops = (ArrayList<ItemStack>) event.getDrops();
        for (ItemStack drop : drops) {
            if (CustomItemManager.asCustomItemInstance(drop) != null) {
                System.out.println("IS CUSTOM ITEM");
                CustomItemInstance dropInstance = CustomItemManager.asCustomItemInstance(drop);
                System.out.println(dropInstance.toString());
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
