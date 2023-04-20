package com.forgefrontier.forgefrontier.items.gear;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.chestpiece.LeatherChestplate;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.helmet.LeatherHelmet;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows.WoodenBow;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.WoodenSword;
import com.forgefrontier.forgefrontier.items.gear.skills.DashSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.GroundSmashSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.InfernoSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.Skill;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.ParticleGem;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.SkillGem;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.UpgradeGem;
import com.forgefrontier.forgefrontier.particles.FFCosmeticParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.PlayerParticle;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

    @Override
    public void init() {
        this.skills = new HashMap<>();
        particleManager = ForgeFrontier.getInstance().getParticleManager();
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new SkillGem());
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new ParticleGem());
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new UpgradeGem());
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new WoodenSword());
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new WoodenBow());
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new LeatherHelmet());
        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new LeatherChestplate());



        this.registerSkill(new GroundSmashSkill());
        this.registerSkill(new DashSkill());
        this.registerSkill(new InfernoSkill());
    }

    @Override
    public void disable() {

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
