package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.CustomStat;

public class GemValues {

    /** the type of gear the gem can be applied to */
    GemEnum gemType;

    /** the statistic which the gem applies to the armor */
    BaseStatistic stat;

    /** the quality associated with the gem */
    Quality quality;

    /** Empty constructor for base instance generation */
    public GemValues() {}

    /**
     * Constructor sets attributes based off of parameters
     *
     * @param gemType the specified gemType
     * @param stat the specified stat values
     * @param quality the specified quality
     */
    public GemValues(GemEnum gemType, BaseStatistic stat, Quality quality) {
        this.gemType = gemType;
        this.stat = stat;
        this.quality = quality;
    }

    /**
     * Constructor which sets attributes from string data
     *
     * @param data the string specifying attribute values.
     */
    public GemValues(String data) {
        // removes curly brackets
        data = data.substring(1, data.length() - 1);

        this.quality = QualityEnum.getQualityEnumFromString(data.substring(0, data.indexOf(":"))).getQuality();
        data = data.substring(data.indexOf(":") + 1);
        String baseStats = data.substring(data.indexOf("{") + 1, data.lastIndexOf("}"));
        this.stat = new BaseStatistic(baseStats);
        data = data.replace(baseStats, "");
        data = data.substring(data.indexOf(":") + 1);
        this.gemType = GemEnum.getGemEnumFromString(data);
    }

    /**
     * @return the quality of the gem values instance
     */
    public Quality getQuality() {
        return quality;
    }

    /** @return the stat related to the gem values instance */
    public BaseStatistic getStat() {
        return stat;
    }

    /**
     * @return the string representation of a GemValues instance
     */
    @Override
    public String toString() {
        return "{" + quality.toString() + ":" + stat.toString() + ":" + gemType.toString() + "}";
    }
}