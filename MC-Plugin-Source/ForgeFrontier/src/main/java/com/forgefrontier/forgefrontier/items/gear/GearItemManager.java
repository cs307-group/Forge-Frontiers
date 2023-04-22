package com.forgefrontier.forgefrontier.items.gear;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmorCreator;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.chestpiece.LeatherChestplate;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.helmet.LeatherHelmet;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.WoodenBow;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.ChickenWing;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.WoodenSword;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.skills.DashSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.GroundSmashSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.InfernoSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.Skill;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.ParticleGem;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.SkillGem;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.UpgradeGem;
import com.forgefrontier.forgefrontier.particles.FFCosmeticParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.PlayerParticle;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.player.PlayerManager;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.logging.Level;

/**
 * Handles events related to GearItem classes
 */
public class GearItemManager extends Manager implements Listener {

    Map<String, Skill> skills;
    ParticleManager particleManager;
    /**
     * Constructor for initializing fields
     *
     * @param plugin the plugin class
     */
    public GearItemManager(ForgeFrontier plugin) {
        super(plugin);
    }
    int statUpdateJob = -1;
    PlayerManager playerManager;
    @Override
    public void init() {
        this.skills = new HashMap<>();
        particleManager = ForgeFrontier.getInstance().getParticleManager();

        CustomItemManager cim = ForgeFrontier.getInstance().getCustomItemManager();

        cim.registerCustomItem(new SkillGem());
        cim.registerCustomItem(new ParticleGem());
        cim.registerCustomItem(new UpgradeGem());
        cim.registerCustomItem(new WoodenSword());
        cim.registerCustomItem(new WoodenBow());
        cim.registerCustomItem(new LeatherHelmet());
        cim.registerCustomItem(new LeatherChestplate());
        cim.registerCustomItem(new ChickenWing());

        registerArmorSet("Copper", QualityEnum.COMMON, 1, 0, "GOLDEN", "&7A mediocre %item% made of rusted copper.", new int[] {3, 6, 4, 2}, new int[]{0,0,0,0});
        registerArmorSet("Refined Copper", QualityEnum.COMMON, 2, 1, "GOLDEN", "&7A copper %item% refined to be more resilient.", new int[] {7, 11, 9, 6}, new int[]{0,0,0,0});

        registerArmorSet("Silver", QualityEnum.COMMON, 2, 0, "IRON", "&7Silver %item% that enhance your physical form when worn.", new int[] {1, 2, 1, 1}, new int[]{1,3,2,1});
        registerArmorSet("Sterling Silver", QualityEnum.COMMON, 2, 1, "IRON", "&7Sterling Silver %item% that both defends and enhances you.", new int[] {5, 10, 7, 4}, new int[]{3,5,4,2});

        registerArmorSet("Brass", QualityEnum.COMMON, 2, 1, "GOLDEN", "&7A durable %item% made of brass that defends well against attacks.", new int[] {8, 11, 8, 5}, new int[]{2,4,3,1});

        registerLeatherArmorSet("Featherwoven", QualityEnum.RARE, 2, 1, Color.WHITE, "&7Lightweight %item% made from magical feather", new int[] {8, 11, 8, 5}, new int[]{2,4,3,1});
        registerSkullHelmet("ZephyrHelmet", "Zephyr Helmet", QualityEnum.UNIQUE, 3, 3,
                "&7The &fZephyr Helmet &7is a symbol of speed and agility,\n" +
                "&7crafted with the power of &fwind.", new int[] {20, 11, 8, 20}, new int[]{5,6,8,4});
        registerLeatherArmorSet("Ooze", QualityEnum.RARE, 2, 1, Color.GREEN, "&2Adaptable %item% reinforced with magical slime material.", new int[] {8, 11, 8, 5}, new int[]{2,4,3,1});

        //cim.registerCustomItem(new CustomArmorCreator("CopperHelmet", "Copper Helmet", QualityEnum.COMMON.getQuality(), 2, 0, Material.GOLDEN_HELMET, ));

        playerManager = plugin.getPlayerManager();
        statUpdateJob = plugin.getServer().getScheduler()
                .scheduleSyncRepeatingTask(plugin, this::updateAllPlayerStats,200,400);

        this.registerSkill(new GroundSmashSkill());
        this.registerSkill(new DashSkill());
        this.registerSkill(new InfernoSkill());
    }

    public void registerSkullHelmet(String skullCode, String itemName, QualityEnum quality, int base, int gemAmt, String lore,
                              int[] def, int[] hp) {
        CustomItemManager cim = ForgeFrontier.getInstance().getCustomItemManager();
        cim.registerCustomItem(new CustomArmorCreator(
                skullCode,
                itemName,
                quality.getQuality(),
                gemAmt, Material.PLAYER_HEAD,
                lore.replace("%item%", "helmet"),
                EquipmentSlot.HEAD,
                def[0],
                hp[0]
        ));
    }

    public void registerArmorSet(String itemName, QualityEnum quality, int base, int gemAmt, String setPrefix, String lore,
                                 int[] def, int[] hp) {
        CustomItemManager cim = ForgeFrontier.getInstance().getCustomItemManager();

        cim.registerCustomItem(new CustomArmorCreator(
            itemName + "Helmet",
            itemName + " Helmet",
            quality.getQuality(),
            gemAmt, Material.matchMaterial(setPrefix + "_HELMET"),
            lore.replace("%item%", "helmet"),
            EquipmentSlot.HEAD,
            def[0],
            hp[0]
        ));
        cim.registerCustomItem(new CustomArmorCreator(
            itemName + "Chestplate",
            itemName + " Chestplate",
            quality.getQuality(),
            gemAmt, Material.matchMaterial(setPrefix + "_CHESTPLATE"),
            lore.replace("%item%", "chestplate"),
            EquipmentSlot.CHEST,
            def[1],
            hp[1]
        ));
        cim.registerCustomItem(new CustomArmorCreator(
            itemName + "Leggings",
            itemName + " Leggings",
            quality.getQuality(),
            gemAmt, Material.matchMaterial(setPrefix + "_LEGGINGS"),
            lore.replace("%item%", "pair of leggings"),
            EquipmentSlot.LEGS,
            def[2],
            hp[2]
        ));
        cim.registerCustomItem(new CustomArmorCreator(
            itemName + "Boots",
            itemName + " Boots",
            quality.getQuality(),
            gemAmt, Material.matchMaterial(setPrefix + "_BOOTS"),
            lore.replace("%item%", "pair of boots"),
            EquipmentSlot.FEET,
            def[3],
            hp[3]
        ));
    }

    public void registerLeatherArmorSet(String itemName, QualityEnum quality, int base, int gemAmt, Color leatherColor, String lore,
                                        int[] def, int[] hp) {
        CustomItemManager cim = ForgeFrontier.getInstance().getCustomItemManager();

        cim.registerCustomItem(new CustomArmorCreator(
                itemName + "Helmet",
                itemName + " Helmet",
                quality.getQuality(),
                gemAmt, Material.matchMaterial("LEATHER_HELMET"),
                leatherColor,
                lore.replace("%item%", "helmet"),
                EquipmentSlot.HEAD,
                def[0],
                hp[0]
        ));
        cim.registerCustomItem(new CustomArmorCreator(
                itemName + "Chestplate",
                itemName + " Chestplate",
                quality.getQuality(),
                gemAmt, Material.matchMaterial("LEATHER_CHESTPLATE"),
                leatherColor,
                lore.replace("%item%", "chestplate"),
                EquipmentSlot.CHEST,
                def[1],
                hp[1]
        ));
        cim.registerCustomItem(new CustomArmorCreator(
                itemName + "Leggings",
                itemName + " Leggings",
                quality.getQuality(),
                gemAmt, Material.matchMaterial("LEATHER_LEGGINGS"),
                leatherColor,
                lore.replace("%item%", "pair of leggings"),
                EquipmentSlot.LEGS,
                def[2],
                hp[2]
        ));
        cim.registerCustomItem(new CustomArmorCreator(
                itemName + "Boots",
                itemName + " Boots",
                quality.getQuality(),
                gemAmt, Material.matchMaterial("LEATHER_BOOTS"),
                leatherColor,
                lore.replace("%item%", "pair of boots"),
                EquipmentSlot.FEET,
                def[3],
                hp[3]
        ));
    }


    @Override
    public void disable() {
        if (statUpdateJob != -1) {
            plugin.getServer().getScheduler().cancelTask(statUpdateJob);
        }
    }

    public void updateAllPlayerStats() {
        java.util.Collection<? extends org.bukkit.entity.Player> onlinePlayers = Bukkit.getOnlinePlayers();
        Collection<FFPlayer> ffPlayers = playerManager.getFFPlayers().values();
        ArrayList<FFPlayer> notOnline = new ArrayList<>();
        for (FFPlayer fp : ffPlayers) {
            boolean online = false;
            for (Player p : onlinePlayers) { if (p.getUniqueId() == fp.playerID)  {online = true; break;} }
            if (!online) { notOnline.add(fp); }
            ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(fp);
        }
        for (FFPlayer off : notOnline) playerManager.getFFPlayers().remove(off.getPlayerID());
    }

    public void registerSkill(Skill skill) {
        this.skills.put(skill.getId(), skill);
    }

    public FFCosmeticParticle getPlayableParticle(String particleID) {
        return particleManager.getCosmeticParticle(particleID);
    }
    public Skill getSkill(String skillId) {
        return this.skills.get(skillId);
    }

    /**
     * Called when a player changes the item in their armor slots
     *
     * @param e the event object storing data about the event
     */
    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent e) {
        Player p = e.getPlayer();

        FFPlayer ffPlayer = plugin.getPlayerManager().getFFPlayerFromID(p.getUniqueId());
        CustomItemInstance newItemInstance = CustomItemManager.asCustomItemInstance(e.getNewItem());
        if (e.getOldItem() != null) {
            CustomItemInstance oldItemInstance = CustomItemManager.asCustomItemInstance(e.getOldItem());
            CustomArmor.CustomArmorInstance oldArmorInstance = (CustomArmor.CustomArmorInstance) oldItemInstance;

            // if there was a custom armor that is being swapped out update the player stats
            if (oldArmorInstance != null) {
                ffPlayer.updateStatsOnArmorDequip(oldArmorInstance);
            }
        }

        // updates player stats values if the armor is a custom armor
        if (newItemInstance instanceof CustomArmor.CustomArmorInstance newArmorInstance) {
            ffPlayer.updateStatsOnArmorEquip(newArmorInstance);
            plugin.getLogger().log(Level.INFO, "Adding new cosmetic on slot: " + e.getSlotType().ordinal());
            particleManager.addPlayerCosmetic(p, newArmorInstance.getParticleEffect(), e.getSlotType().ordinal());
        } else {
            plugin.getLogger().log(Level.INFO, "Armor change into nothing: " + e.getSlotType().ordinal());
            particleManager.addPlayerCosmetic(p, null, e.getSlotType().ordinal());
        }

    }

    public Skill getRandomSkill() {
        int index = (int) (Math.random() * this.skills.size());
        Iterator<String> keyIter = this.skills.keySet().iterator();
        while(index > 0) {
            index -= 1;
            keyIter.next();
        }
        return this.skills.get(keyIter.next());
    }


    /*
     * Is called when an item being used by a player is set to take damage
     *
     * @param event the event specifying the specific
     *
    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        CustomItemInstance customItemInstance = CustomItemManager.asCustomItemInstance(player.getInventory().getItem(EquipmentSlot.HAND));
        if (customItemInstance == null) {
            return;
        }

        if (customItemInstance instanceof GearItemInstance) {
            event.setCancelled(true);
        }
    }*/
}
