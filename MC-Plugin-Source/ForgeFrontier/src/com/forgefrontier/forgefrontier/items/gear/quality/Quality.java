package com.forgefrontier.forgefrontier.items.gear.quality;

import org.bukkit.ChatColor;

/**
 * Quality
 *
 * This class is a value attached to gear which determines the maximum possible percentage values for reforging.
 */
public class Quality {

    /** An integer value which represents the maximum possible percentage for the quality */
    int maxValue;

    /** The string representation of name of the Quality */
    String rarity;
    /** The int representation of the Quality */
    int rarityInt;

    /** The chance that a statistic on the gear associated with this quality is a percentage */
    int isPercentChance;

    /** The color associated with the quality */
    ChatColor color;

    /**
     * Default constructor for the Quality class
     */
    public Quality() {
        maxValue = 0;
        rarity = "UNASSIGNED";
        rarityInt = -1;
        color = ChatColor.GRAY;
    }

    /**
     * Specifiable constructor for the Quality class
     *
     * @param maxValue specifies the maxPercent for the Quality
     * @param rarity specifies the rarity for the Quality
     * @param isPercentChance specifies the change for the maxValue to be a percentage
     */
    public Quality(int maxValue, int isPercentChance, String rarity, ChatColor color) {
        this.maxValue = maxValue;
        this.rarity = rarity;
        this.rarityInt = determineRarityIntFromString(rarity);
        this.isPercentChance = isPercentChance;
        this.color = color;
    }

    /**
     * @return the max percentage achievable by this specific quality
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * @return the name of the isPercentChance of the quality
     */
    public int getPercentChance() {
        return isPercentChance;
    }

    /**
     * @return the name of the rarity of the quality
     */
    public String getRarity() {
        return rarity;
    }

    /**
     * @return the integer representation of the quality
     */
    public int getRarityInt() {
        return rarityInt;
    }

    /**
     * @return the color of the quality
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * @return a string representation of the quality
     */
    @Override
    public String toString() {
        return rarity;
    }

    /**
     * Returns the int representation of a rarity given the string representation
     *
     * @param rarity string representation of the rarity
     * @return the int representation of the rarity
     */
    public static int determineRarityIntFromString(String rarity) {
        return switch (rarity) {
            case "COMMON" -> 0;
            case "RARE" -> 1;
            case "UNIQUE" -> 2;
            case "MYTHIC" -> 3;
            default -> -1;
        };
    }
}
