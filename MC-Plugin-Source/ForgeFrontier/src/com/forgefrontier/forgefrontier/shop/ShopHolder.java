package com.forgefrontier.forgefrontier.shop;


import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemRename;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import net.milkbowl.vault.economy.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

public class ShopHolder extends BaseInventoryHolder {
    Hashtable<UUID, ShopListing> listings;
    Boolean remove = false;
    Shop shop;
    ShopHolder(Shop s) {
        super(27);
        this.fillPanes();
        this.listings = s.getListings();
        this.updateGUI();
        this.shop = s;
    }

    ShopHolder(Shop s, Boolean remove) {
        super(27);
        this.fillPanes();
        this.listings = s.getListings();
        this.remove = remove;
        this.updateGUI();
        this.shop = s;
    }

    public void updateGUI() {
        Set<UUID> keys = listings.keySet();
        int i = 0;
        for (UUID k : keys) {
            if (i > 9) break;
            ItemStack displayItem = listings.get(k).getDisplayItem();
            this.setItem(i, displayItem);
            int i2 = i;
            this.addHandler(i, (e) -> {
                Player p = (Player) e.getWhoClicked();
                p.openInventory(new ConfirmationHolder("Confirm?",this.getInventory(), displayItem, ()->{
                    removeListingCallback(e, i2, k);
                }).getInventory());
            });
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
        ShopListing sl = this.listings.get(k);
        if (sl == null) {
            this.updateGUI();
            return;
        }
        ItemStack itm = this.getInventory().getItem(i);
        if (itm == null || itm.getType().equals(Material.AIR) || itm.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        this.setItem(i,new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("").build());
        double result = shop.executeBuy(p,k);
        if (result != -1) {
            p.sendMessage(ChatColor.GOLD + "You bought " + ItemRename.itemName(sl.getItem()) +
                    ChatColor.GOLD + " for " + ChatColor.BOLD  + (Math.round(result * 100) / 100) + "g" +
                    ChatColor.RESET + ChatColor.GOLD + "!");
        }
        this.updateGUI();
    }






}
