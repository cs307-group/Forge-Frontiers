package com.forgefrontier.forgefrontier.items.gear.statistics;

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
    public static StatEnum getRandStatEnum() {
        int randValue = (int)(Math.random() * 5);
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
}
