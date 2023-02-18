package com.forgefrontier.forgefrontier.items.gear;

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

    /** The chance that a statistic on the gear associated with this quality is a percentage */
    int isPercentChance;

    /**
     * Default constructor for the Quality class
     */
    public Quality() {
        maxValue = 0;
        rarity = "UNASSIGNED";
    }

    /**
     * Specifiable constructor for the Quality class
     *
     * @param maxValue specifies the maxPercent for the Quality
     * @param rarity specifies the rarity for the Quality
     * @param isPercentChance specifies the change for the maxValue to be a percentage
     */
    public Quality(int maxValue, int isPercentChance, String rarity) {
        this.maxValue = maxValue;
        this.rarity = rarity;
        this.isPercentChance = isPercentChance;
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
     * @return a string representation of the quality
     */
    @Override
    public String toString() {
        return rarity + ": " + maxValue;
    }
}
