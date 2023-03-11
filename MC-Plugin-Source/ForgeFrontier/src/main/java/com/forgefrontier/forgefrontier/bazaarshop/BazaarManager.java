package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

public class BazaarManager {
    private ForgeFrontier plugin;
    private final int MIN_SLOT = 0;
    private final int MAX_SLOT = 20;

    HashMap<Integer, TreeMap<UUID, BazaarEntry>> bazaarData;
    ItemStack[] displayItems;

    public BazaarManager(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    public void init() {
        // Pull Bazaar items from database

    }


    public boolean setItemSlot(ItemStack itm, int idx) {
        if (itm == null) {
            return plugin.getDBConnection().bazaarDB.deleteSlotID(idx);
        }
        String name = ItemUtil.itemName(itm);
        String lore = ItemUtil.getStringLore(itm);
        String cdata = ItemUtil.getCustomData(itm);
        return plugin.getDBConnection().bazaarDB.setLookup(
                idx, itm.getType().toString(), name, lore, cdata);
    }


}
