package com.forgefrontier.forgefrontier.shop;
import com.forgefrontier.forgefrontier.utils.ItemRename;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * Stores relevent information about a shop listing, and modifies input items to be of a valid listing type.
 */
public class ShopListing {

    /** Original Item **/
    private ItemStack item;
    private ItemStack displayItem;
    private double price;
    private int amt;
    private Player lister;
    UUID listingID;
    Boolean uniqueName = false;

    public ShopListing(Player p, ItemStack item, double price, int amt, UUID listingID) {
        this.lister = p;
        this.price = price;
        this.amt = item.getAmount();
        this.item = item;
        this.listingID = listingID;
        displayItem = this.shopifyItem(item);
    }

    /** Getters **/
    ItemStack getItem() { return this.item; }
    ItemStack getDisplayItem() { return this.displayItem; }

    double getPrice() { return this.price; }
    int getAmt() { return this.amt; }
    UUID getID() { return this.listingID; }
    Player getLister() { return this.lister; }

    /** Setters **/
    void setPrice(double p) { price = p; }
    void setAmt(int n) { amt = n; }
    void setItem(ItemStack ci) { item = ci; }
    @Deprecated
    void setPlayer(Player p) { lister = p; }
    /**
     * Edits ItemStack's display name to have price amount
     */
    public ItemStack shopifyItem(ItemStack itemStack) {
        ItemStack i2 = new ItemStack(itemStack);
        ItemMeta im = itemStack.getItemMeta();


        String s = (im.hasDisplayName()) ? im.getDisplayName() : ItemRename.getFriendlyName(itemStack,true);

        s = s + " §§§e(" + price + "g)";
        im.setDisplayName(s);
        i2.setItemMeta(im);
        return i2;
    }


    /**
     * Removes shopify item rename
     */
    @Deprecated
    public void unshopifyItem(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        assert im != null;
        String s = "";
        if (!uniqueName) {
            s = im.getDisplayName();
            int split = s.indexOf("§§");
            if (split == -1) {
                System.err.print("[ShopListing.unshopifyItem] Failure to restore item's display name");
            }
            s = s.substring(0, split);
        }
        im.setDisplayName(s);
        itemStack.setItemMeta(im);
    }

}
