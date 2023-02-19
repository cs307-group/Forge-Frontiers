package com.forgefrontier.forgefrontier.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Shop {
    private Hashtable<UUID, ShopListing> listings;
    ShopHolder shopGUI;
    ShopCommandExecutor shopCommands;
    public Shop() {
        listings = new Hashtable<>();
        shopGUI = new ShopHolder(this.listings);
        shopCommands = new ShopCommandExecutor(this);
    }

    public Boolean createListing(Player p, double price, int amt, ItemStack i) {
        int k = (int) (price * 10) + amt + i.hashCode();
        UUID id = new UUID(k, i.hashCode());
        if (listings.get(id) != null) return false;
        ShopListing sl = new ShopListing(p, i, price, amt, id);
        listings.put(sl.getID(), sl);
        return true;
    }

    public ShopListing deleteListing(UUID listingID) {
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
