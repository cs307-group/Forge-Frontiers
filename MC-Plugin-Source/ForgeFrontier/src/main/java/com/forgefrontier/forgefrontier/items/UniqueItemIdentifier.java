package com.forgefrontier.forgefrontier.items;

import java.util.UUID;

/**
 * UniqueItemIdentifier
 *
 * Used to distinguish between different item types and give each item a unique ID
 */
public class UniqueItemIdentifier {

    /**
     * The value of the item's universal ID
     */
    String baseItemID;

    /**
     * The unique value specific to this particular item
     */
    UUID uniqueIdentifier;

    /**
     * Default constructor for a UniqueItemIdentifier
     *
     * @param baseItemID the baseItemID to be set for this identifier
     */
    public UniqueItemIdentifier(String baseItemID) {
        this.baseItemID = baseItemID;
        this.uniqueIdentifier = UUID.randomUUID();
    }

    /**
     * @return the BaseItemID
     */
    public String getBaseItemID() {
        return baseItemID;
    }

    /**
     * @return the UniqueIdentifier
     */
    public UUID getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * @return a string representation of the UniqueItemIdentifier
     */
    @Override
    public String toString() {
        return "" + baseItemID + "." + uniqueIdentifier;
    }
}
