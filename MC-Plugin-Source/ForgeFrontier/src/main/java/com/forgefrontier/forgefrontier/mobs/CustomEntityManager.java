package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.mobs.slimes.CustomSlimeEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hitbox.HitBoxEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.SlimeBoss;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.*;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.gameparticles.MobParticles;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.SlimeBossEntity;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

/**
 * Handles Events for Custom Mobs and includes methods to spawn/de-spawn them
 */
public class CustomEntityManager extends Manager implements Listener {

    Map<String, CustomMob> entities;
    private final HashMap<UUID, String> instanceEntityMap;

    /**
     * Constructor for the manager
     *
     * @param plugin the plugin instance for Forge Frontier
     */
    public CustomEntityManager(ForgeFrontier plugin) {
        super(plugin);
        this.entities = new HashMap<>();
        this.instanceEntityMap = new HashMap<>();

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
    public boolean spawnEntity(String code, Player player) {
        CustomMob mob = ForgeFrontier.getInstance().getCustomEntityManager().getEntities().get(code);
        if (mob == null) {
            return false;
        }
        CraftEntity ce = mob.createCustomEntity((CraftWorld)player.getWorld());

        instanceEntityMap.put(ce.getUniqueId(), code);
        plugin.getLogger().log(Level.INFO,"Spawned Entity: " + ce.getUniqueId());
        mob.spawnCustomEntity(player.getLocation(), ce);
        return true;
    }


    /**
     * Spawns an entity with the specified code
     *
     * @param code the code corresponding to the mob to be spawned
     * @param loc the location to be spawned at
     * @return if the execution was successful
     */
    public CraftEntity spawnEntity(String code, Location loc) {
        CustomMob mob = ForgeFrontier.getInstance().getCustomEntityManager().getEntities().get(code);
        if (mob == null) {
            return null;
        }
        CraftEntity ce = mob.createCustomEntity((CraftWorld)loc.getWorld());
        instanceEntityMap.put(ce.getUniqueId(), code);
        plugin.getLogger().log(Level.INFO,"Spawned Entity: " + ce.getUniqueId());

        return mob.spawnCustomEntity(loc, ce);
    }

    @EventHandler
    public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
        CraftLivingEntity entity = (CraftLivingEntity) event.getEntity();
        String namePlate = ChatColor.WHITE + "";

        if (entity.hasMetadata("dynamic")) {
            HashMap<String, Double> dynamicValues = (HashMap<String, Double>) entity.getMetadata("dynamic").get(0).value();
            double damage = dynamicValues.get("incoming");
            event.setDamage(damage);
            namePlate += "[☆] ";
        }

        if (entity.hasMetadata("code")) {
            // checks if the entity is a hitbox entity
            int currHealth = (((int) (entity.getHealth() - event.getDamage())));
            if (currHealth < 0) {
                currHealth = 0;
            }
            if (!(((String) entity.getMetadata("code").get(0).value()).contains("HitBox"))) {
                // sets the nameplate of the entity
                if (entity.getHandle() instanceof HitBoxEntity hitBox) {
                    namePlate += entity.getMetadata("name").get(0).value() +
                            ": " + currHealth + "/" + ((int) entity.getMaxHealth());
                    entity.setCustomName(namePlate);
                } else {
                    namePlate += entity.getMetadata("name").get(0).value() +
                            ": " + currHealth + "/" + ((int) entity.getMaxHealth());
                    entity.setCustomName(namePlate);
                    entity.setCustomNameVisible(true);
                }
            }
        }

        // Updates the boss health bar if the entity contains metadata for it
        if (entity.hasMetadata("bossbar")) {
            Object o = entity.getMetadata("bossbar").get(0).value();
            if (o instanceof BossBar bossBar) {
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

        CraftLivingEntity entity = (CraftLivingEntity) event.getEntity();
        CustomMob cm = getMobFromID(entity.getUniqueId());

        if (cm != null) {
            plugin.getLogger().log(Level.INFO,"Entity Death: " + cm.getCode());
            handleCustomDeath(entity, cm);
        } else {
            plugin.getLogger().log(Level.INFO,"Non Custom Death: " + entity.getUniqueId());
        }
        // handles custom item drops
        if (entity.hasMetadata("custom-drop-keys") && entity.hasMetadata("custom-drop-table")) {
            event.getDrops().clear();
            HashMap<String, Integer> dropTable = (HashMap<String, Integer>) entity.getMetadata("custom-drop-table").get(0).value();
            ArrayList<String> dropKeys = (ArrayList<String>) entity.getMetadata("custom-drop-keys").get(0).value();
            for (String dropKey : dropKeys) {
                int rand = (int) (Math.random() * 100) + 1;
                if (rand <= dropTable.get(dropKey)) {
                    event.getDrops().add(CustomItemManager.getCustomItem(dropKey).asInstance(null).asItemStack());
                }
            }
        }

        // handles native item drops
        if (entity.hasMetadata("drop-keys") && entity.hasMetadata("drop-table")) {
            HashMap<Material, Integer> dropTable = (HashMap<Material, Integer>) entity.getMetadata("drop-table").get(0).value();
            ArrayList<Material> dropKeys = (ArrayList<Material>) entity.getMetadata("drop-keys").get(0).value();
            for (Material dropKey : dropKeys) {
                int rand = (int) (Math.random() * 100) + 1;
                if (rand <= dropTable.get(dropKey)) {
                    event.getDrops().add(new ItemStack(dropKey));
                }
            }
        }

        // Removes the boss health bar if the entity contains metadata for it
        if (entity.hasMetadata("bossbar")) {
            Object o = entity.getMetadata("bossbar").get(0).value();
            if (o instanceof BossBar bossBar) {
                bossBar.setProgress(0);
                bossBar.removeAll();
            }
            if (entity.getKiller() != null) {
                // upgrades the player's tier when killed
                if (entity.hasMetadata("tier")) {
                    int tier = (int) entity.getMetadata("tier").get(0).value();
                    plugin.getPlayerManager().getFFPlayerFromID(entity.getKiller().getUniqueId()).setTier(tier);
                }
            }
            if (cm != null && cm.getCode().equals(SlimeBoss.CODE)) {
                SlimeBossEntity slimeBossEntity = (SlimeBossEntity) entity.getHandle();
                if (slimeBossEntity != null) {
                    slimeBossEntity.onDeath();
                }
            } else if (cm != null && cm.getCode().equals(SlimeBossTimer.CODE)) {
                SlimeBossTimerEntity slimeBossTimerEntity = (SlimeBossTimerEntity) entity.getHandle();
                if (slimeBossTimerEntity != null) {
                    slimeBossTimerEntity.onDeath();
                }
            } else if (cm != null && cm.getCode().equals(SlimeBossSmall.CODE)) {
                SlimeBossSmallEntity smallEntity = (SlimeBossSmallEntity) entity.getHandle();
                if ( smallEntity != null) {
                    smallEntity.onDeath();
                }
            }
        }

        if (cm != null && cm.getCode().equals(SlimeBossSwift.CODE)) {
            SlimeBossSwiftEntity swiftEntity = (SlimeBossSwiftEntity) entity.getHandle();
            if (swiftEntity != null) {
                swiftEntity.onDeath();
            }
        }

        if (entity.hasMetadata("code")) {
            // checks if the entity is a slime entity
            if (entity instanceof Slime slime) {
                if (slime instanceof CraftEntity craftEntity) {
                    CustomSlimeEntity customSlimeEntity = (CustomSlimeEntity) craftEntity.getHandle();
                    if (customSlimeEntity != null) {
                        customSlimeEntity.dropItems();
                        customSlimeEntity.setLoc(null);
                    }
                }
                slime.setHealth(1);
                slime.teleport(new Location(slime.getWorld(), 0, -70, 0));
                slime.setGravity(true);
            }
        }
    }

    /** getter for the entities hashmap in the manager */
    public Map<String, CustomMob> getEntities() {
        return entities;
    }

    public void insertEntityInstance(UUID entityID, String entityCode) {
        this.instanceEntityMap.put(entityID, entityCode);
    }

    public CustomMob getMobFromID(UUID entityID) {
        if (!instanceEntityMap.containsKey(entityID)) return null;
        return entities.get(instanceEntityMap.get(entityID));
    }

    public void handleCustomDeath(LivingEntity e, CustomMob cm) {
        if (cm.getCode().equals("PoisonChicken")) {
            MobParticles.POISON_CHICKEN_DEATH_PARTICLE.playParticleAtLocation(e.getWorld(),e.getLocation());
            return;
        }
        MobParticles.GENERIC_CUSTOM_DEATH_PARTICLE.playParticleAtLocation(e.getWorld(),e.getLocation());
    }


}
