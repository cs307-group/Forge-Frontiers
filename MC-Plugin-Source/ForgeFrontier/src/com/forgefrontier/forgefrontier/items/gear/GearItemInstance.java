package com.forgefrontier.forgefrontier.items.gear;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem.UniqueCustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.ReforgeStatistic;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemValues;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

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
    GemValues[] gems;
    /** The type of gems that this gear piece will accept */
    GemEnum gemEnum;

    /** The material which defines the look of the gear */
    Material material;
    /** Durability of the gear. This is useful for setting specific textures for specific durabilities */
    int durability;

    /** The lore specific to this item */
    String lore;

    /** JSON data about this particular GearItemInstance */
    HashMap<String, String> gearData;

    public GearItemInstance(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Sets the gear data based off GearItemInstance attributes. Only call after setting the attributes
     */
    public void setGearData() {
        gearData = new HashMap<>();

        gearData.put("name", name);
        gearData.put("quality", quality.toString());
        gearData.put("num-base-stats", Integer.toString(numBaseStats));

        // stores the base stats
        StringBuilder baseStatArrayString = new StringBuilder();
        System.out.println("BaseStats: " + Arrays.toString(baseStats) + ":" + numBaseStats);
        for (int i = 0; i < numBaseStats; i++) {
            baseStatArrayString.append(baseStats[i].toString());
        }
        System.out.println("STRING: " + baseStatArrayString);
        baseStatArrayString = new StringBuilder(baseStatArrayString.substring(0, baseStatArrayString.length() - 1));
        gearData.put("base-stats", baseStatArrayString + "}");

        // stores the ReforgeStatistics
        StringBuilder reforgeStatArrayString = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            reforgeStatArrayString.append(reforgeStats[i].toString());
        }
        reforgeStatArrayString = new StringBuilder(reforgeStatArrayString.substring(0, reforgeStatArrayString.length() - 1));
        gearData.put("reforge-stats", reforgeStatArrayString + "}");

        gearData.put("num-gem-slots", Integer.toString(numGemSlots));

        // stores the gems
        StringBuilder gemsArrayString = new StringBuilder();
        for (int i = 0; i < numGemSlots; i++) {
            if (gems[i] == null) {
                gemsArrayString.append("{EMPTY}");
            } else {
                gemsArrayString.append(gems[i].toString());
            }
        }
        gearData.put("gems", gemsArrayString.toString());

        gearData.put("gem-enum", gemEnum.toString());
        gearData.put("material", material.toString());
        gearData.put("durability", Integer.toString(durability));
        gearData.put("lore", lore);
    }

    /**
     * updates the attributes after the gear data has been set
     */
    public void setAttributes() {

        name = gearData.get("name");
        quality = QualityEnum.getQualityEnumFromString(
                gearData.get("quality")).getQuality();
        numBaseStats = Integer.parseInt(gearData.get("num-base-stats"));

        String baseStatsString = name = gearData.get("base-stats");
        baseStats = new BaseStatistic[numBaseStats];
        for (int i = 0; i < numBaseStats; i++) {
            baseStats[i] =
                    new BaseStatistic(baseStatsString.substring(0, baseStatsString.indexOf("}") + 1));
            baseStatsString = baseStatsString.substring(baseStatsString.indexOf("}") + 1);
        }

        reforgeStats = new ReforgeStatistic[3];
        String reforgeStatsString = name = gearData.get("reforge-stats");
        for (int i = 0; i < 3; i++) {
            reforgeStats[i] =
                    new ReforgeStatistic(reforgeStatsString.substring(0, reforgeStatsString.indexOf("}") + 1));
            reforgeStatsString = reforgeStatsString.substring(reforgeStatsString.indexOf("}") + 1);
        }

        numGemSlots = Integer.parseInt(gearData.get("num-gem-slots"));

        gems = new GemValues[numGemSlots];
        String gemsString = name = gearData.get("gems");
        for (int i = 0; i < numGemSlots; i++) {
            if (gemsString.substring(0, gemsString.indexOf("}") + 1).equals("{EMPTY}")) {
                gems[i] = null;
            } else {
                gems[i] =
                        new GemValues(gemsString.substring(0, gemsString.indexOf("}") + 1));
            }
            gemsString = gemsString.substring(gemsString.indexOf("}") + 1);
        }

        name = gearData.get("gem-enum");
        name = gearData.get("material");
        name = gearData.get("durability");
        name = gearData.get("lore");
    }

    /** returns the value of this.baseStats */
    public BaseStatistic[] getBaseStats() {
        return this.baseStats;
    }

    public void setBaseStats(BaseStatistic[] baseStats) {
        this.numBaseStats = baseStats.length;
        this.baseStats = baseStats;
    }

    /** returns the value of this.reforgeStats */
    public ReforgeStatistic[] getReforgeStats() {
        return this.reforgeStats;
    }

    /** returns the value of this.gems */
    public GemValues[] getGems() {
        return this.gems;
    }
}
