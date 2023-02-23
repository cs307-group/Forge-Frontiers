package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemSetRunnable implements Runnable {

    BaseInventoryHolder bih;
    int itemslot;
    ItemStack itm;

    public ItemSetRunnable(BaseInventoryHolder bih, int i, ItemStack itm) {
        this.bih = bih;
        itemslot = i;
        this.itm = itm;
    }
    @Override
    public void run() {
        bih.setItem(itemslot,itm);
    }
}
