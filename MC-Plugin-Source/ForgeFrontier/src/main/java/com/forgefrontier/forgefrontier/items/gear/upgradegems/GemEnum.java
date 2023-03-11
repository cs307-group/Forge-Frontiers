package com.forgefrontier.forgefrontier.items.gear.upgradegems;

/**
 * An enum which represents the type of gear an UpgradeGem can be applied to
 */
public enum GemEnum {
    UNASSIGNED ("UNASSIGNED"),
    WEAPON ("WEAPON"),
    ARMOR ("ARMOR");

    /** the string representation of the enum */
    private final String str;

    GemEnum(String str) {
        this.str = str;
    }

    /**
     * @return the string representation of the enum
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * @return a random GemEnum
     */
    public static GemEnum getRandGemEnum() {
        int randVal = (int) (Math.random() * 2);
        return switch (randVal) {
            case 0 -> WEAPON;
            case 1 -> ARMOR;
            default -> UNASSIGNED;
        };
    }

    /**
     * Returns the specified GemEnum based on the string param
     *
     * @param sEnum String specifying the correct GemEnum
     * @return the correct GemEnum
     */
    public static GemEnum getGemEnumFromString(String sEnum) {
        return switch(sEnum) {
            case "WEAPON" -> WEAPON;
            case "ARMOR" -> ARMOR;
            default -> UNASSIGNED;
        };
    }
}