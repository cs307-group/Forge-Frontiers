package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;

/**
 * Enum to represent the different types of statistics
 */
public enum StatEnum {
    HP("HP", ""),
    ATK("Attack", ""),
    STR("Strength", ""),
    DEX("Dexterity", ""),
    CRATE("Crit Chance", "%"),
    CDMG("Crit Damage", ""),
    DEF("Defense", "");

    String friendlyName;
    String suffix;

    StatEnum(String friendlyName, String suffix) {
        this.friendlyName = friendlyName;
        this.suffix = suffix;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getSuffix() {
        return suffix;
    }

    public static final StatEnum[] ORDERED_STAT_ENUMS = {
        StatEnum.HP,
        StatEnum.ATK,
        StatEnum.STR,
        StatEnum.DEX,
        StatEnum.CRATE,
        StatEnum.CDMG,
        StatEnum.DEF
    };

    /**
     * Returns an Enum type based on an integer value
     *
     * @return the StatEnum returned
     */
    public static StatEnum getRandStatEnum(GemEnum gemEnum) {
        // System.out.println(gemEnum.toString());
        int randValue = (int)(Math.random() * 7);
        if (gemEnum == GemEnum.WEAPON) {
            // System.out.println("regen");
            randValue = (int)(Math.random() * 5) + 1;
        }
        return switch (randValue) {
            case 0 -> HP;
            case 1 -> ATK;
            case 2 -> STR;
            case 3 -> DEX;
            case 4 -> CRATE;
            case 5 -> CDMG;
            case 6 -> DEF;
            default -> null;
        };
    }

    /**
     * Returns the correct enum based upon specified string
     *
     * @param sEnum string specifying the correct enum
     * @return the correct enum value
     */
    public static StatEnum getEnumFromString(String sEnum) {
        return switch (sEnum) {
            case "HP" -> HP;
            case "ATK" -> ATK;
            case "STR" -> STR;
            case "DEX" -> DEX;
            case "CRATE" -> CRATE;
            case "CDMG" -> CDMG;
            case "DEF" -> DEF;
            default -> null;
        };
    }

}
