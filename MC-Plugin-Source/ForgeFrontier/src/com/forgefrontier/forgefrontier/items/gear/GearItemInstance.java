package com.forgefrontier.forgefrontier.items.gear;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem.UniqueCustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.ReforgeStatistic;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.UpgradeGemInstance;
import org.bukkit.Material;

/**
 * Instance class for the GearItem class
 */
public abstract class GearItemInstance extends UniqueCustomItemInstance {
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

    /** The number of gems slots associated with this piece of gear */
    int numGemSlots;
    /** The array of GemInstances applied to the piece of gear */
    UpgradeGemInstance[] gems;
    /** The type of gems that this gear piece will accept */
    GemEnum gemEnum;

    /** The material which defines the look of the gear */
    Material material;
    /** Durability of the gear. This is useful for setting specific textures for specific durabilities */
    int durability;

    /** The lore specific to this item */
    String lore;
}
