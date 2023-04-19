package com.forgefrontier.forgefrontier.shop;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Stores relevent information about a shop listing, and modifies input items to be of a valid listing type.
 */
public class ShopListing {

    /** Original Item **/
    private ItemStack item;
    private final ItemStack displayItem;
    private double price;
    private int amt;
    private ShopPlayer lister;
    UUID listingID;
    Boolean uniqueName = false;

    public ShopListing(Player p, ItemStack item, double price, int amt, UUID listingID) {
        this.lister = new ShopPlayer(p);
        this.price = price;
        this.amt = item.getAmount();
        this.item = item;
        this.listingID = listingID;
        displayItem = shopifyItem(item, lister, price);
    }
    public ShopListing(ShopPlayer p, ItemStack item, double price, UUID listingID) {
        this.lister = p;
        this.price = price;
        this.amt = item.getAmount();
        this.item = item;
        this.listingID = listingID;
        displayItem = shopifyItem(item, lister, price);
    }

    /** Getters **/
    ItemStack getItem() { return this.item; }
    ItemStack getDisplayItem() { return this.displayItem; }

    double getPrice() { return this.price; }
    int getAmt() { return this.amt; }
    UUID getID() { return this.listingID; }
    ShopPlayer getLister() { return this.lister; }

    /** Setters **/
    void setPrice(double p) { price = p; }
    void setAmt(int n) { amt = n; }
    void setItem(ItemStack ci) { item = ci; }
    @Deprecated
    void setPlayer(ShopPlayer p) { lister = p; }



    /**
     * Edits ItemStack's display name to have price amount
     */
    public static ItemStack shopifyItem(ItemStack itemStack, double price) {
        ItemStack i2 = new ItemStackBuilder(itemStack).build();
        ItemMeta im = itemStack.getItemMeta();
        final String invisibleChar = "" + ChatColor.COLOR_CHAR + ChatColor.COLOR_CHAR;

        String s = (im.hasDisplayName()) ? im.getDisplayName() :
                                            ItemUtil.simpleRename(itemStack.getType());
        s = ChatColor.RESET + s +  " " + invisibleChar + ChatColor.GOLD  + "(" + price + "g)";

        im.setDisplayName(s);
        i2.setItemMeta(im);
        return i2;
    }

    /**
     * Edits ItemStack's display name to have price amount
     */
    public static ItemStack shopifyItem(ItemStack itemStack, ShopPlayer p, double price) {
        ItemStack i2 = new ItemStackBuilder(itemStack).build();
        ItemMeta im = itemStack.getItemMeta();
        if (im == null) {
            return i2;
        }
        if (im.hasLore()) {
            List<String> lore = new ArrayList<>(im.getLore());
            lore.add( "" + ChatColor.DARK_GRAY + p.getName());
            im.setLore(lore);
        }
        else {
            List<String> lore = new ArrayList<>();
            lore.add("" + ChatColor.DARK_GRAY + p.getName());
            im.setLore(lore);
        }

        final String invisibleChar = "" + ChatColor.COLOR_CHAR + ChatColor.COLOR_CHAR;

        String s = (im.hasDisplayName()) ? im.getDisplayName() :
                ItemUtil.simpleRename(itemStack.getType());
        s = ChatColor.RESET + s +  " " + invisibleChar + ChatColor.GOLD  + "(" + price + "g)";

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
            String invisible = "" + ChatColor.COLOR_CHAR + ChatColor.COLOR_CHAR;
            int split = s.indexOf(invisible);
            s = s.substring(0, split);
        }
        im.setDisplayName(s);
        itemStack.setItemMeta(im);
    }


}
