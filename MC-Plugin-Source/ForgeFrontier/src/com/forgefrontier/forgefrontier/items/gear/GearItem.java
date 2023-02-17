package com.forgefrontier.forgefrontier.items.gear;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.UniqueItemIdentifier;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.ReforgeStatistic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * GearItem
 *
 * A class derivative of CustomItem which is used as a base to derive different custom gear types, which
 * each have their own statistics which affect the player who is equipping it. It is linked to its own custom appearance
 * as well.
 */

public abstract class GearItem extends CustomItem {

    /** The ID specific to this type of item */
    final int BASE_ID = 0;

    /** A string representing the name of the item */
    String name;

    /** The quality of the gear */
    Quality quality;

    /** The Unique Item ID which contains the BaseID and UniqueID for the item */
    UniqueItemIdentifier UUID;

    /** The number of base statistics associated with this type of gear */
    int numBaseStats;

    /** An array of BaseStatistics associated with this type of gear */
    BaseStatistic[] baseStats;

    /** An array of ReforgeStatistics associated with this type of gear */
    ReforgeStatistic[] reforgeStats;

    /**
     * Default constructor
     *
     * @param code the code for the CustomItem
     */
    public GearItem(String code) {
        super(code);

        name = "GearItem";
        quality = QualityEnum.UNASSIGNED.getQuality();
    }

    /**
     * onInteract() overridable method from CustomItem
     *
     * Defines what occurs when the player used the item to interact, i.e. left or right click with the item
     * selected in their hotbar
     *
     * @param e the PlayerInteractEvent which defines the event which occurred in game
     */
    @Override
    public void onInteract(PlayerInteractEvent e) {

    }

    /**
     * asItemStack() overridable method from CustomItem
     *
     * Returns null, as it is not expected this will ever be called
     *
     * @return the in game representation of the item as an ItemStack object
     */
    @Override
    protected ItemStack asItemStack() {
        return null;
    }
}
