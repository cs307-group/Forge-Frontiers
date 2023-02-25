package com.forgefrontier.forgefrontier.items.gear.quality;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.ChatColor;

/**
 * Quality Enum
 *
 * This enum holds values for the different qualities an item can be set as.
 */
public enum QualityEnum {
    UNASSIGNED (new Quality()),
    COMMON (new Quality(5, 50, "COMMON", ChatColor.GRAY)),
    RARE (new Quality(10, 75,"RARE", ChatColor.DARK_AQUA)),
    UNIQUE (new Quality(20, 100, "UNIQUE", ChatColor.GOLD)),
    MYTHIC (new Quality(30, 100, "MYTHIC", ChatColor.DARK_RED));

    /** the Quality object that defines the enum */
    private final Quality quality;

    QualityEnum(Quality quality) {
        this.quality = quality;
    }

    /**
     * @return the quality object for this enum
     */
    public Quality getQuality() {
        return this.quality;
    }

    /**
     * @return a random QualityEnum type
     */
    public static QualityEnum getRandQualityEnum() {
        int randVal = (int) (Math.random() * 4);
        return switch (randVal) {
            case 0 -> COMMON;
            case 1 -> RARE;
            case 2 -> UNIQUE;
            case 3 -> MYTHIC;
            default -> UNASSIGNED;
        };
    }

    /**
     * @param max the integer value for the maximum possible quality which can be generated
     * @return a random QualityEnum type
     */
    public static QualityEnum getRandQualityEnum(int max) {
        System.out.println("max" + max);
        try {
            if (max < 0 || max > 4) {
                throw new InvalidMaxQualityException();
            }
            int randVal = (int) (Math.random() * max);
            System.out.println("randVal " + randVal);
            return switch (randVal) {
                case 0 -> COMMON;
                case 1 -> RARE;
                case 2 -> UNIQUE;
                case 3 -> MYTHIC;
                default -> UNASSIGNED;
            };
        } catch (InvalidMaxQualityException e) {
            e.printStackTrace();
        }
        return UNASSIGNED;
    }

    public static QualityEnum getQualityEnumFromString(String sEnum) {
        return switch (sEnum) {
            case "COMMON" -> COMMON;
            case "RARE" -> RARE;
            case "UNIQUE" -> UNIQUE;
            case "MYTHIC" -> MYTHIC;
            default -> UNASSIGNED;
        };
    }
}
