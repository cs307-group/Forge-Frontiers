package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.forgefrontier.forgefrontier.items.gear.QualityEnum;

/**
 * ReforgeStatistic
 *
 * Represents a modifiable statistic attached to a piece of gear
 */
public class ReforgeStatistic extends CustomStat {

    /** Value of the quality which determines the maximum possible percentage for the stat */
    QualityEnum quality;

    /** Determines if the value of the stat is a percent modifier or a hard number value */
    boolean isPercent;

    /**
     * Constructor where quality of statistic is specified
     *
     * @param quality the quality of the statistic
     */
    public ReforgeStatistic(QualityEnum quality) {
        super(1, quality.getQuality().getMaxValue());
        this.quality = quality;

        //Randomly determines if the stat value is a percentage
        int percentVal = (int) (Math.random() * 100) + 1;
        this.isPercent = quality.getQuality().getPercentChance() < percentVal;
    }

    /**
     * Constructor where every stat is specified
     *
     * @param statValue to be assigned to corresponding attribute
     * @param statType to be assigned to corresponding attribute
     * @param isPercent determines if the value of the stat is a percent modifier or a hard number value
     * @param quality specifies the quality of the statistic
     */
    public ReforgeStatistic(int statValue, StatEnum statType, boolean isPercent, QualityEnum quality) {
        super(statValue, statType);
        this.isPercent = isPercent;
        this.quality = quality;
    }

    /**
     * Performs a 'reforge' on the statistic, re-rolling the values
     *
     * @return the updated stat value
     */
    public int reforge() {
        // generates random values
        int percentVal = (int) (Math.random() * 100) + 1;
        int newStatVal = (int) (Math.random() * (quality.getQuality().getMaxValue())) + 1;

        // determines if the statistic is a percentage or not
        isPercent = quality.getQuality().getPercentChance() < percentVal;

        // determines new stat type
        statType = StatEnum.getRandStatEnum();

        statValue = newStatVal;
        return statValue;
    }

    /**
     * @return a string representation of the ReforgeStatistic
     */
    @Override
    public String toString() {
        if (isPercent)
            return statType.toString() + " +" + statValue + "%";
        else
            return statType.toString() + " +" + statValue;
    }
}