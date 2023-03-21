package com.forgefrontier.forgefrontier.shop;


import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.items.ItemSetRunnable;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class ShopHolder extends BaseInventoryHolder {
    ConcurrentHashMap<UUID, ShopListing> listings;
    Boolean remove;
    UUID pID;
    Shop shop;
    ShopHolder(Shop s) {
        super(54, "Shop");
        this.fillPanes();
        this.listings = s.getListings();
        this.shop = s;
        remove = false;
        this.updateGUI();

    }

    ShopHolder(Shop s, UUID playerID, Boolean remove) {
        super(54, "Shop");
        this.fillPanes();
        this.listings = s.getListings();
        this.remove = remove;
        this.shop = s;
        this.pID = playerID;
        this.removeGUI();
    }

    public void removeGUI() {
        Set<UUID> keys = listings.keySet();
        int i = 0;
        this.fillPanes();
        for (UUID k : keys) {
            if (listings.get(k).getLister().getUniqueId().compareTo(pID) != 0) {
                ForgeFrontier.getInstance().getLogger().log(Level.INFO,"UUID COMPARE REMOVE: " + listings.get(k).getLister().getUniqueId().toString() + " - " + pID.toString());
                continue;
            }
            if (i > 9) break;
            ItemStack displayItem = listings.get(k).getDisplayItem();
            this.setItem(i, displayItem);
            int i2 = i;
            this.addHandler(i, (e) -> {
                Player p = (Player) e.getWhoClicked();
                p.openInventory(new ConfirmationHolder("Remove Listing?",this.getInventory(), displayItem, ()->{
                    this.setItem(i2,new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("").build());
                    if (shop.executeRemoveListing(p,listings.get(k))) {
                        this.removeHandler(i2);
                    }
                    removeGUI();
                }).getInventory());
            });
            i++;
        }
    }

    public void updateGUI() {

        if (this.remove) {
            this.removeGUI();
            return;
        }
        Set<UUID> keys = listings.keySet();
        int nitems = 0;
        int i = 1;
        int j = 0;
        this.fillPanes();
        for (UUID k : keys) {
            // TODO: Fill GUI
            if (nitems > 28) break;
            ShopListing listing = listings.get(k);
            ItemStack displayItem = listings.get(k).getDisplayItem();

            if (j == 7) { i++; j = 0; }

            int i2 = (9 * i) + 1 + j;
            this.setItem(i2, displayItem);
            this.addHandler(i2, (e) -> {
                Player p = (Player) e.getWhoClicked();
                if (p.getUniqueId() == listing.getLister().getUniqueId()) {
                    this.setItem(i2,new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
                            .setDisplayName("" + ChatColor.RED + "Cannot buy your own item!").build());
                    ForgeFrontier.getInstance().getServer().getScheduler()
                            .runTaskLater(ForgeFrontier.getInstance(), new ItemSetRunnable(this,i2,displayItem),60);
                    return;
                }
                p.openInventory(new ConfirmationHolder("Buy Item?",this.getInventory(), displayItem, ()->{
                    buyListingCallback(e, i2, k);
                }).getInventory());
            });
            j++;
            nitems++;
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
    public void buyListingCallback(InventoryClickEvent e, int i, UUID k) {
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
        double result = shop.executeBuy(p, k);
        this.setItem(i,new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("").build());
        if (result != -1) {
                p.sendMessage(ChatColor.GOLD + "You bought " + ItemUtil.itemName(sl.getItem()) +
                        ChatColor.GOLD + " for " + ChatColor.BOLD + (Math.round(result * 100) / 100) + "g" +
                        ChatColor.RESET + ChatColor.GOLD + "!");
                this.removeHandler(i);
            }
        this.updateGUI();
    }






}