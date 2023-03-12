package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BazaarManager {
    private final ForgeFrontier plugin;
    private final int MIN_SLOT = 0;
    private final int MAX_SLOT = 28;

    HashMap<Integer, TreeMap<UUID, BazaarEntry>> bazaarData;
    ArrayList<ItemStack> displayItems;
    BazaarGUI bazaarGUI;
    public BazaarManager(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    public void init() {
        // Pull Bazaar items from database
        displayItems = plugin.getDBConnection().bazaarDB.loadLookup(MAX_SLOT);
        displayItems.replaceAll(x -> Objects.isNull(x) ?
                (new ItemStackBuilder(Material.BARRIER).setDisplayName("" + ChatColor.RED + "N/A").build()) : x);
        bazaarGUI = new BazaarGUI();
    }


    public boolean setItemSlot(ItemStack itm, int idx) {
        if (idx < MIN_SLOT || idx > MAX_SLOT) {
            return false;
        }
        if (itm == null) {
            if (plugin.getDBConnection().bazaarDB.deleteSlotID(idx)) {
                displayItems.set(idx, (new ItemStackBuilder(Material.BARRIER)
                        .setDisplayName("" + ChatColor.RED + "N/A").build()));
                return true;
            } else {
                return false;
            }
        }
        String name = ItemUtil.itemName(itm);
        String lore = ItemUtil.getStringLore(itm);
        String cdata = ItemUtil.getCustomData(itm);
        if (plugin.getDBConnection().bazaarDB.setLookup(
                idx, itm.getType().toString(), name, lore, cdata)) {
            displayItems.set(idx, (new ItemStackBuilder(itm).build()));
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ItemStack> getDisplayItems() { return displayItems; }


}
