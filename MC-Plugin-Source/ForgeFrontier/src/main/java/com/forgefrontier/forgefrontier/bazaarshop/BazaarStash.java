package com.forgefrontier.forgefrontier.bazaarshop;

import java.util.UUID;

public class BazaarStash {
    private UUID orderID;
    private UUID playerID;
    private int amount;
    private int itemID;

    public BazaarStash(BazaarEntry be, int amt) {
        this.orderID = be.getEntryID();
        this.playerID = be.getListerID();
        this.amount = amt;
        this.itemID = be.getSlotID();
    }
    public BazaarStash(UUID orderID, UUID playerID, int amount, int itemID) {
        this.orderID = orderID;
        this.playerID = playerID;
        this.amount = amount;
        this.itemID = itemID;
    }

    public BazaarStash(BazaarStash bs) {
        this.orderID = bs.orderID;
        this.playerID = bs.playerID;
        this.amount = bs.amount;
        this.itemID = bs.itemID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
