package com.forgefrontier.forgefrontier.items.gear;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Handles events related to GearItem classes
 */
public class GearItemManager extends Manager implements Listener {

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

    }

    @Override
    public void disable() {

    }

    /**
     * Called when a player changes the item in their armor slots
     *
     * @param e the event object storing data about the event
     */
    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent e) {
        System.out.println("ARMOR CHANGE EVENT ACTIVATED");
        Player p = (Player) e.getPlayer();
        FFPlayer ffPlayer = plugin.getPlayerManager().getFFPlayerFromID(p.getUniqueId());
        System.out.println("BEFORE:\n" + ffPlayer.getStatsString());
        CustomItemInstance newItemInstance = CustomItemManager.asCustomItemInstance(e.getNewItem());
        if (newItemInstance != null) {
            System.out.println(newItemInstance.getData().toString() + " : " + (newItemInstance instanceof CustomArmor.CustomArmorInstance));
        }
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
        System.out.println("AFTER:\n" + ffPlayer.getStatsString());
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