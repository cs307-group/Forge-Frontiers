package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.items.gear.statistics.CustomStat;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;

import java.util.HashMap;
import java.util.Map;

public class StatHolder {

    private Map<StatEnum, Integer> stats;
    public StatHolder() {
        this.stats = new HashMap<>();
        for(StatEnum value: StatEnum.values()) {
            this.stats.put(value, 0);
        }
    }
    public void addStat(CustomStat stat) {
        stats.put(stat.getStatType(), stat.getStatValue() + stats.get(stat.getStatType()));
    }

    public void clear() {
        for(StatEnum value: StatEnum.values()) {
            this.stats.put(value, 0);
        }
    }

    public int get(StatEnum statEnum) {
        return this.stats.get(statEnum);
    }

    @Override
    public String toString() {
        return this.stats.toString();
    }

}
