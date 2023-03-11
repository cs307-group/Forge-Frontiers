package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;

/**
 * Enum to represent the different types of statistics
 */
public enum StatEnum {
    UNASSIGNED, HP, ATK, STR, DEX, CRATE, CDMG, DEF;

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
            default -> UNASSIGNED;
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
            default ->UNASSIGNED;
        };
    }

    /**
     * returns an integer representation of the enum
     *
     * @param value the stat enum
     * @return the integer representation
     */
    public static int getIntFromEnum(StatEnum value) {
        return switch (value) {
            case HP -> 0;
            case ATK -> 1;
            case STR -> 2;
            case DEX -> 3;
            case CRATE -> 4;
            case CDMG -> 5;
            case DEF -> 6;
            default -> -1;
        };
    }
}
