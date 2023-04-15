package com.forgefrontier.forgefrontier.items.gear;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.skills.GroundSmashSkill;
import com.forgefrontier.forgefrontier.items.gear.skills.Skill;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.SkillGem;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;
import java.util.logging.Level;

/**
 * Handles events related to GearItem classes
 */
public class GearItemManager extends Manager implements Listener {

    Map<String, Skill> skills;

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

        ForgeFrontier.getInstance().getCustomItemManager().registerCustomItem(new SkillGem());

        this.registerSkill(new GroundSmashSkill());

    }

    @Override
    public void disable() {

    }

    public void registerSkill(Skill skill) {
        this.skills.put(skill.getId(), skill);
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
        Player p = (Player) e.getPlayer();

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
        }

        // Updates player stats in database
        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(ffPlayer.playerID, ffPlayer.getCurrentHealth(),
                ffPlayer.getStats(), ffPlayer.getAscension(), ffPlayer.getTier());
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
