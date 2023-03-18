package com.forgefrontier.forgefrontier.bazaarshop;


import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.UUID;




public class BazaarEntry {
    /**
     * Comparator for comparing bazaar entries by price
     */
    static class BazaarEntryComparator implements Comparator<BazaarEntry> {
        @Override
        public int compare(BazaarEntry e1, BazaarEntry e2) {
            return Double.compare(e2.getPrice(), e1.getPrice());
        }
    }

    public enum EntryType {
        BUY, SELL
    }
    private EntryType type;
    private int amount;
    // price per
    private  double price;
    private  int slotID;
    private Timestamp listdate;
    private UUID listerID;
    private UUID entryID;
    public BazaarEntry(boolean buy, int slot, int amount, double price, UUID lister) {
        if (buy) this.type = EntryType.BUY;
        else this.type = EntryType.SELL;
        this.slotID = slot;
        this.amount = amount;
        this.price = price;

        this.entryID = UUID.randomUUID();
        this.listerID = lister;
        this.listdate = new Timestamp(System.currentTimeMillis());
    }

    public BazaarEntry(UUID entryID, EntryType type, int slotID, int amount, double price, UUID listerID, Timestamp creationTime) {
        this.type = type;
        this.entryID = entryID;
        this.slotID = slotID;
        this.amount = amount;
        this.price = price;
        listdate = creationTime;
        this.listerID = listerID;
    }
    public UUID getEntryID() {
        return entryID;
    }
    public void setEntryID(UUID id) {
        this.entryID = id;
    }
    public UUID getListerID() {
        return listerID;
    }

    public void setListerID(UUID playerID) {
        this.listerID = playerID;
    }

    public EntryType getType() {
        return type;
    }
    public boolean getBType() {
        // 1 == Buy
        // 0 == Sell
        return (type == EntryType.BUY);
    }



    public void setType(EntryType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public Timestamp getListdate() {
        return listdate;
    }

    public void setListdate(Timestamp listdate) {
        this.listdate = listdate;
    }
}
