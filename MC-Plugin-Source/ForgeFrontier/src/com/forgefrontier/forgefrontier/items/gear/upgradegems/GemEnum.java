package com.forgefrontier.forgefrontier.items.gear.upgradegems;

/**
 * An enum which represents the type of gear an UpgradeGem can be applied to
 */
public enum GemEnum {
    UNASSIGNED ("Unassigned"),
    WEAPON ("Weapon"),
    ARMOR ("Armor");

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
}