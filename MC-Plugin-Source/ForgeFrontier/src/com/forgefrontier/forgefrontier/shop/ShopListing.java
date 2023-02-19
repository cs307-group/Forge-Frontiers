package com.forgefrontier.forgefrontier.shop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        this.shopifyItem(item);
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

    /**
     * Edits itemstack's display name to have price amount
     */
    public void shopifyItem(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        String s = im.getDisplayName();
        if (s == null) {
            System.err.print("[ShopListing.shopifyItem] NULL Display Name");
            return;
        }
        s = s + " §e(" + price + ")";
        im.setDisplayName(s);
        itemStack.setItemMeta(im);
    }

    /**
     * Removes shopify item rename.
     */
    public void unshopifyItem(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        String s = im.getDisplayName();
        if (s == null) {
            System.err.print("[ShopListing.unshopifyItem] NULL Display Name");
            return;
        }
        int split = s.indexOf("§e");
        if (split == -1) {
            System.err.print("[ShopListing.unshopifyItem] Failure to restore item's display name");
        }
        s = s.substring(0, split);
        im.setDisplayName(s);
        itemStack.setItemMeta(im);
    }

}
