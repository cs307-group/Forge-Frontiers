package com.forgefrontier.forgefrontier.bazaarshop;


public class BazaarEntry {
    public enum EntryType {
        BUY, SELL
    }
    EntryType type;
    int amount;
    double price;
    int slotID;
}
