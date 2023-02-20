package com.forgefrontier.forgefrontier.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Implements functionality to buy/sell items through a GUI interface
 */
public class Shop {
    private Hashtable<UUID, ShopListing> listings;
    ShopHolder shopGUI;
    ShopCommandExecutor shopCommands;

    /** Basic Constructor. */
    public Shop() {
        listings = new Hashtable<>();
        shopGUI = new ShopHolder(this.listings);
        shopCommands = new ShopCommandExecutor(this);
    }

    /**
     * List an item
     * @param p     Player who is making listing
     * @param price Price of item
     * @param amt   Amount of items to list
     * @param i     Itemstack to list
     * @return      Success?
     */
    public Boolean createListing(Player p, double price, int amt, ItemStack i) {
        UUID id = UUID.randomUUID();
        if (listings.get(id) != null) return false;
        ShopListing sl = new ShopListing(p, i, price, amt, id);
        listings.put(sl.getID(), sl);
        return true;
    }

    public ShopListing removeListing(UUID listingID) {
        return listings.remove(listingID);
    }

    public ShopCommandExecutor getCommandExecutor() {
        return this.shopCommands;
    }

    /**
     *  Creates a new ShopHolder GUI for viewing
     * */
    public Inventory getGUI() {
        return new ShopHolder(listings).getInventory();
    }

    public Hashtable<UUID, ShopListing> getListings() {
        return this.listings;
    }





}
