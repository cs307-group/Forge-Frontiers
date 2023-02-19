package com.forgefrontier.forgefrontier.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Shop {
    private Hashtable<UUID, ShopListing> listings;
    ShopHolder shopGUI;
    ShopCommandExecutor shopCommands;
    public Shop() {
        listings = new Hashtable<>();
        ItemStack item = new ItemStack(Material.DIAMOND_ORE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Epic Item");
        item.setItemMeta(meta);
        createListing(null,99.99,5,item);
        shopGUI = new ShopHolder(this.listings);
        shopCommands = new ShopCommandExecutor(shopGUI);
        this.updateGUI();
    }

    public void createListing(Player p, double price, int amt, ItemStack i) {
        int k = (int) (price * 10) + amt + i.hashCode();
        ShopListing sl = new ShopListing(p, i, price, amt, new UUID(k, i.hashCode()));
        listings.put(sl.getID(), sl);
    }

    public ShopListing deleteListing(UUID listingID) {
        return listings.remove(listingID);
    }

    public void updateGUI() {
        Set<UUID> keys = listings.keySet();
        int i = 0;
        for (UUID k : keys) {
            if (i > 9) break;
            shopGUI.setItem(i, listings.get(k).getItem());
            i++;
        }
    }

    public ShopCommandExecutor getCommandExecutor() {
        return this.shopCommands;
    }





}
