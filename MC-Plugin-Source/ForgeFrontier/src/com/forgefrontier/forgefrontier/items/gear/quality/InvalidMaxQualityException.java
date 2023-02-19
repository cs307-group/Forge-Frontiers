package com.forgefrontier.forgefrontier.items.gear.quality;

/**
 * A custom exception class for when calling the getRandQualityEnum method that is thrown if the max quality specified
 * is -1 or too high
 */
public class InvalidMaxQualityException extends Exception {

    /**
     * Constructor for the InvalidMaxQualityException class
     */
    public InvalidMaxQualityException() {
        super ("Specified max quality value invalid");
    }
}
