package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.items.gear.statistics.CustomStat;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;

public class PlayerStat extends CustomStat {

    /**
     * Constructor with specifications
     *
     * @param statValue to be assigned to corresponding attribute
     * @param statType to be assigned to corresponding attribute
     */
    public PlayerStat(int statValue, StatEnum statType) {
        super(statValue, statType);
    }
}
