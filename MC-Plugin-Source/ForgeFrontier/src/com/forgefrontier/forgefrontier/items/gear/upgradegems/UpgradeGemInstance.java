package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem.UniqueCustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;

/**
 * A class representing the UpgradeGems, which can be applied to special gear in their inventory. Once applied to an
 * item the gem can not be removed
 */
public class UpgradeGemInstance extends UniqueCustomItemInstance {

    /** the type of gear the gem can be applied to */
    GemEnum gemType;

    /** the statistic which the gem applies to the armor */
    BaseStatistic stat;

    /** the quality associated with the gem */
    Quality quality;

    @Override
    public String toString() {
        return quality.getColor() + stat.toString();
    }
}
