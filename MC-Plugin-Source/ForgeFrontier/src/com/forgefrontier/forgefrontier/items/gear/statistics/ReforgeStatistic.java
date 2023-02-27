package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.destroystokyo.paper.event.entity.WitchThrowPotionEvent;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;

/**
 * ReforgeStatistic
 *
 * Represents a modifiable statistic attached to a piece of gear
 */
public class ReforgeStatistic extends CustomStat {

    /** Value of the quality which determines the maximum possible percentage for the stat */
    Quality quality;

    /** Determines if the value of the stat is a percent modifier or a hard number value */
    boolean isPercent;

    /**
     * Constructor where quality of statistic is specified
     *
     * @param quality the quality of the statistic
     */
    public ReforgeStatistic(Quality quality, GemEnum gemEnum) {
        super(1, quality.getMaxValue(), StatEnum.getRandStatEnum(gemEnum));
        this.quality = quality;

        if (statType == StatEnum.CDMG || statType == StatEnum.CRATE) {
            this.isPercent = true;
        } else {
            //Randomly determines if the stat value is a percentage
            int percentVal = (int) (Math.random() * 100) + 1;
            this.isPercent = quality.getPercentChance() < percentVal;
        }
    }

    /**
     * Constructor where every stat is specified
     *
     * @param statValue to be assigned to corresponding attribute
     * @param statType to be assigned to corresponding attribute
     * @param isPercent determines if the value of the stat is a percent modifier or a hard number value
     * @param quality specifies the quality of the statistic
     */
    public ReforgeStatistic(int statValue, StatEnum statType, boolean isPercent, Quality quality) {
        super(statValue, statType);
        this.isPercent = isPercent;
        this.quality = quality;
    }

    /**
     * Constructor which creates an object based on data in string format
     *
     * @param data the string which specifies the attributes of the object
     */
    public ReforgeStatistic(String data) {
        super();
        // removes curly brackets
        data = data.substring(1, data.length() - 1);

        if (data.contains("%")) {
            isPercent = true;
            quality = QualityEnum.getQualityEnumFromString(data.substring(0, data.indexOf(":"))).getQuality();
            String withoutQuality = data.substring(data.indexOf(":") + 1);
            statType = StatEnum.getEnumFromString(withoutQuality.substring(0, withoutQuality.indexOf((":"))));
            statValue = Integer.parseInt(withoutQuality.substring(withoutQuality.indexOf(":") + 1, withoutQuality.indexOf("%")));
        }
        else {
            isPercent = false;
            quality = QualityEnum.getQualityEnumFromString(data.substring(0, data.indexOf(":"))).getQuality();
            String withoutQuality = data.substring(data.indexOf(":") + 1);
            statType = StatEnum.getEnumFromString(withoutQuality.substring(0, withoutQuality.indexOf((":"))));
            statValue = Integer.parseInt(withoutQuality.substring(withoutQuality.indexOf(":") + 1));
        }
    }

    /**
     * Performs a 'reforge' on the statistic, re-rolling the values
     *
     * @return the updated stat value
     */
    public int reforge(GemEnum gemEnum) {
        // generates random values
        int percentVal = (int) (Math.random() * 100) + 1;
        int newStatVal = (int) (Math.random() * (quality.getMaxValue())) + 1;

        // determines if the statistic is a percentage or not
        isPercent = quality.getPercentChance() < percentVal;

        // determines new stat type
        statType = StatEnum.getRandStatEnum(gemEnum);

        statValue = newStatVal;
        return statValue;
    }

    /** @return if the statistic is a percentage */
    public boolean isPercent() {
        return isPercent;
    }

    /**
     * @return a string representation of the ReforgeStatistic
     */
    @Override
    public String toString() {
        if (isPercent)
            return "{" + quality.toString() + ":" + statType.toString() + ":" + statValue + "%" + "}";
        else
            return "{" + quality.toString() + ":" + statType.toString() + ":" + statValue + "}";
    }
}
