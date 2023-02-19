package com.forgefrontier.forgefrontier.shop;


import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

public class ShopHolder extends BaseInventoryHolder {
    Hashtable<UUID, ShopListing> listings;
    Boolean remove = false;
    ShopHolder(Hashtable<UUID, ShopListing> listings) {
        super(27);
        this.fillPanes();
        this.listings = listings;
        this.updateGUI();
    }

    ShopHolder(Hashtable<UUID, ShopListing> listings, Boolean remove) {
        super(27);
        this.fillPanes();
        this.listings = listings;
        this.remove = remove;
        this.updateGUI();
    }

    public void updateGUI() {
        Set<UUID> keys = listings.keySet();
        int i = 0;
        for (UUID k : keys) {
            if (i > 9) break;
            this.setItem(i, listings.get(k).getItem());
            if (remove) {
                System.out.println("Setting Remove");
                int finalI = i;
                this.addHandler(i, (e) -> {
                    this.setItem(finalI,new ItemStackBuilder(Material.AIR).build());
                    System.out.println("Attempting to remove from index: " + finalI);
                    this.listings.remove(k);
                });
            }
            i++;
        }
    }




}
