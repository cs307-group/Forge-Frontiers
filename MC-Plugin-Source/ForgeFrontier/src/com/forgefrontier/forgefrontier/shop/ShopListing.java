package com.forgefrontier.forgefrontier.shop;
import com.forgefrontier.forgefrontier.utils.ItemRename;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
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
     * Edits ItemStack's display name to have price amount
     */
    public void shopifyItem(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        assert im != null;

        String s = (im.hasDisplayName()) ? im.getDisplayName() : ItemRename.getFriendlyName(itemStack,true);

        System.out.println("Shopifying Item: " + s);
        s = s + " §§§e(" + price + ")";
        im.setDisplayName(s);
        itemStack.setItemMeta(im);
    }

    /**
     * Removes shopify item rename.
     */
    public void unshopifyItem(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        assert im != null;
        String s = im.getDisplayName();
        int split = s.indexOf("§§");
        if (split == -1) {
            System.err.print("[ShopListing.unshopifyItem] Failure to restore item's display name");
        }
        s = s.substring(0, split);
        im.setDisplayName(s);
        itemStack.setItemMeta(im);
    }

}
