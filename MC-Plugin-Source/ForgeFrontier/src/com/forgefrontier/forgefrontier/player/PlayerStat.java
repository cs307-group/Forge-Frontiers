package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.items.gear.statistics.*;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerStat extends CustomStat {

    /** The value of the stat before percentages are applied */
    int baseValue;

    /** All percentages which affect the final statvalue */
    ArrayList<Double> percentages;

    /**
     * Constructor with specifications
     *
     * @param statValue to be assigned to corresponding attribute
     * @param statType to be assigned to corresponding attribute
     */
    public PlayerStat(int statValue, StatEnum statType) {
        super(statValue, statType);
        baseValue = statValue;
        percentages = new ArrayList<>();
    }

    /**
     * Constructor given a string to specify data
     *
     * @param data the string specifying the attributes of the object
     */
    public PlayerStat(String data) {
        super("{" + data.substring(data.indexOf(":", data.indexOf(":") + 1)));
        data = data.substring(1, data.length() - 1);
        baseValue = Integer.parseInt(data.substring(0, data.indexOf(":")));

        String percentagesStr = data.substring(data.indexOf(":") + 1, data.indexOf("]") + 1);
        percentages = new ArrayList<>();
        String[] strArr = percentagesStr.substring(1, percentagesStr.length() - 1).split(", ");

        for (String s : strArr) {
            percentages.add(Double.parseDouble(s));
        }
    }

    /** @return a string representation of the PlayerStat instance */
    public String toString() {
        String customStatString = super.toString();
        return "{" + baseValue + ":" + percentages.toString() + ":" + customStatString.substring(1);
    }

    /** adds a new stat modifier to the PlayerStat */
    public void addStat(CustomStat stat) {
        if (stat instanceof ReforgeStatistic reforgeStatistic) {
            if (reforgeStatistic.isPercent()) {
                percentages.add(((double) stat.getStatValue()) / 100);
            } else {
                baseValue += stat.getStatValue();
            }
        } else if (stat instanceof BaseStatistic) {
            baseValue += stat.getStatValue();
        }

        this.setStatValue((int) StatCalc.calculateTotalStat(this));
    }

    /** removes a stat modifier from the PlayerStat */
    public void removeStat(CustomStat stat) {
        if (stat instanceof ReforgeStatistic reforgeStatistic) {
            if (reforgeStatistic.isPercent()) {
                percentages.remove(((double) stat.getStatValue()) / 100);
            } else {
                baseValue -= stat.getStatValue();
            }
        } else if (stat instanceof BaseStatistic) {
            baseValue -= stat.getStatValue();
        }

        this.setStatValue((int) StatCalc.calculateTotalStat(this));

    }

    /** returns the percentages array */
    public ArrayList<Double> getPercentages() {
        return percentages;
    }

    /** returns the base value (before percentages are applied) */
    public int getBaseValue() {
        return baseValue;
    }
}
