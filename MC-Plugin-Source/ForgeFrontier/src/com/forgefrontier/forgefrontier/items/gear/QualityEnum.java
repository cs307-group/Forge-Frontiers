package com.forgefrontier.forgefrontier.items.gear;

/**
 * Quality Enum
 *
 * This enum holds values for the different qualities an item can be set as.
 */
public enum QualityEnum {
    UNASSIGNED {
        final Quality quality = new Quality();

    }, COMMON {
        final Quality quality = new Quality(5, 50, "COMMON");

    }, RARE {
        final Quality quality = new Quality(10, 75,"RARE");

    }, UNIQUE {
        final Quality quality = new Quality(20, 100, "UNIQUE");

    }, MYTHIC {
        final Quality quality = new Quality(30, 100, "MYTHIC");

    };
    Quality quality;

    /**
     * @return the quality object for this enum
     */
    public Quality getQuality() {
        return quality;
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
}
