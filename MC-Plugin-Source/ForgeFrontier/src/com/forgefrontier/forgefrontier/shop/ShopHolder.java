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

import java.util.HashMap;
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
                int i2 = i;
                this.addHandler(i, (e) -> {
                    removeListingCallback(e, i2, k);
                });
            }
            i++;
        }
    }

    /**
     * GUI click callback the removes item from GUI and Shop
     * Intended to be called when item is clicked in GUI
     * If the listing is null, we do not do anything.
     * @param e event
     * @param i slot index
     * @param k item uuid
     */
    public void removeListingCallback(InventoryClickEvent e, int i, UUID k) {
        if (this.listings.get(k) == null) {
            this.updateGUI();
            return;
        }
        ItemStack itm = this.getInventory().getItem(i);
        Player p = (Player) e.getWhoClicked();
        this.setItem(i,new ItemStackBuilder(Material.AIR).build());
        ShopListing l = this.listings.remove(k);

        if (itm == null || l == null) return;
        l.unshopifyItem(l.getItem());
        itm = l.getItem();



        HashMap<Integer, ItemStack> left = p.getInventory().addItem(itm);
        if (left.size() == 0) return;
        for (Integer lk : left.keySet()) {
            System.out.println("Player Overflow! Dropping Item");
            p.getWorld().dropItem(p.getLocation().add(0,1,0),left.get(lk));
        }

    }




}
