package com.forgefrontier.forgefrontier.items.gear;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
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
 * A class derivative of UniqueCustomItem which is used as a base to derive different custom gear types, which
 * each have their own statistics which affect the player who is equipping it. It is linked to its own custom appearance
 * as well.
 */

public abstract class GearItem extends UniqueCustomItem {

    /** The ID specific to this type of item */
    final int BASE_ID = 0;

    /** A string representing the name of the item */
    String name;

    /** The quality of the gear */
    Quality quality;

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

}
