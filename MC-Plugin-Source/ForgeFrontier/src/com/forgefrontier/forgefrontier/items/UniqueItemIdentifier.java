package com.forgefrontier.forgefrontier.items;

/**
 * UniqueItemIdentifier
 *
 * Used to distinguish between different item types and give each item a unique ID
 */
public class UniqueItemIdentifier {

    /**
     * The value of the item's universal ID
     */
    int baseItemID;

    /**
     * The unique value specific to this particular item
     */
    int uniqueIdentifier;

    /**
     * Default constructor for a UniqueItemIdentifier
     */
    public UniqueItemIdentifier(int baseItemID) {
        this.baseItemID = baseItemID;
        this.uniqueIdentifier = generateIdentification();
    }

    /**
     * @return a unique ID to store in the uniqueIdentifier attribute
     */
    public static int generateIdentification() {
        //TODO: Implement unique code generation
        return 0;
    }

    /**
     * @return the BaseItemID
     */
    public int getBaseItemID() {
        return baseItemID;
    }

    /**
     * @return the UniqueIdentifier
     */
    public int getUniqueIdentifier() {
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
