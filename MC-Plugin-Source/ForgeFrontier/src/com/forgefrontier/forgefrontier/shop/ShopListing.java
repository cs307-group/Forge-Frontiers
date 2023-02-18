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
    // SHOULD NOT NEED TO USE THIS
    void _set_player(Player p) { lister = p; }

    void set_price(double p) { price = p; }
    void set_amt(int n) { amt = n; }

    ItemStack getItem() { return this.item; }
    void setItem(ItemStack ci) { item = ci; }

}
