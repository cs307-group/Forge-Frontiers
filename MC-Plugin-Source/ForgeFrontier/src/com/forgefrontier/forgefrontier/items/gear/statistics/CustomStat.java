package com.forgefrontier.forgefrontier.items.gear.statistics;

/**
 * Class to form skeleton for statistics, which are modifications to attack/defense values in game
 */
public abstract class CustomStat {
    /** Defines the type of statistic */
    StatEnum statType;

    /** Defines the value of the statistic*/
    int statValue;

    /** Empty constructor */
    public CustomStat() {}

    /**
     * Default Constructor with Random values
     *
     * @param minValue minimum possible value for the statValue
     * @param maxValue maximum possible value for the statValue
     */
    public CustomStat(int minValue, int maxValue) {
        this.statType = StatEnum.getRandStatEnum();
        while (statType == StatEnum.CRATE || statType == StatEnum.CDMG) {
            this.statType = StatEnum.getRandStatEnum();
        }
        this.statValue = (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
    }

    /**
     * Default Constructor with Random values
     *
     * @param minValue minimum possible value for the statValue
     * @param maxValue maximum possible value for the statValue
     * @param statType to be assigned to corresponding attribute
     */
    public CustomStat(int minValue, int maxValue, StatEnum statType) {
        this.statType = statType;
        this.statValue = (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
    }

    /**
     * Stat constructor when given string data most likely from NMS
     *
     * @param data specifies the attributes of the statistic
     */
    public CustomStat(String data) {
        // removes curly brackets
        data = data.substring(1, data.length() - 1);

        this.statType = StatEnum.getEnumFromString(data.substring(0, data.indexOf(":")));
        this.statValue = Integer.parseInt(data.substring(data.indexOf(":") + 1));
    }

    /**
     * Constructor with specifications
     *
     * @param statValue to be assigned to corresponding attribute
     * @param statType to be assigned to corresponding attribute
     */
    public CustomStat(int statValue, StatEnum statType) {
        this.statValue = statValue;
        this.statType = statType;
    }

    /**
     * @return the stat value stored
     */
    public int getStatValue() {
        return statValue;
    }

    /**
     * @return the stat type of the statistic
     */
    public StatEnum getStatType() {
        return statType;
    }

    /**
     * sets the value of statValue to the parameter's value
     *
     * @param statValue the updated value for statValue
     */
    public void setStatValue(int statValue) {
        this.statValue = statValue;
    }

    /**
     * @return a string representation of a statistic
     */
    @Override
    public String toString() {
        return "{" + statType.toString() + ":" + statValue + "}";
    }
}
