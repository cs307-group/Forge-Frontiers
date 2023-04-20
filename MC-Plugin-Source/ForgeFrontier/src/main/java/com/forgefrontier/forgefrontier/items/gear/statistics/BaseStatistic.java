package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;

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
    public BaseStatistic(int minValue, int maxValue, GemEnum gemEnum) {
        super(minValue, maxValue, gemEnum);
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

    /**
     * Constructor given a string to specify data
     *
     * @param data the string specifying the attributes of the object
     */
    public BaseStatistic(String data) {
        super(data);
    }

}
