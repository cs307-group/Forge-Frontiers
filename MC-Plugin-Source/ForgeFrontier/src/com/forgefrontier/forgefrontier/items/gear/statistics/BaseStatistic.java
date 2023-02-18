package com.forgefrontier.forgefrontier.items.gear.statistics;

/**
 * BaseStatistic
 *
 * Represents an *NON-modifiable statistic attached to a piece of gear
 */
public class BaseStatistic extends CustomStat {

    /**
     * Constructor with min and max stat values specified
     *
     * @param minValue minimum possible value for the statValue
     * @param maxValue maximum possible value for the statValue
     */
    public BaseStatistic(int minValue, int maxValue) {
        super(minValue, maxValue);
    }

    /**
     * Constructor with every attribute specified
     *
     * @param statValue to be assigned to corresponding attribute
     * @param statType to be assigned to corresponding attribute
     */
    public BaseStatistic(int statValue, StatEnum statType) {
        super(statValue, statType);
    }
}
