package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
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


    public boolean setItemSlot(ItemStack i, int idx) {
        return false;

    }


}
