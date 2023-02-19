package com.forgefrontier.forgefrontier.shop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopListing {
    private ItemStack item;
    private double price;
    private int amt;
    private Player lister;
    UUID listingID;


    public ShopListing(Player p, ItemStack item, double price, int amt, UUID listingID) {
        this.lister = p;
        this.price = price;
        this.amt = item.getAmount();
        this.item = item;
        this.listingID = listingID;
    }

    Player getLister() { return this.lister; }
    double getPrice() { return this.price; }
    int getAmt() { return this.amt; }
    UUID getID() { return this.listingID; }


    @Deprecated
    void setPlayer(Player p) { lister = p; }

    void setPrice(double p) { price = p; }
    void setAmt(int n) { amt = n; }

    ItemStack getItem() { return this.item; }
    void setItem(ItemStack ci) { item = ci; }



}
