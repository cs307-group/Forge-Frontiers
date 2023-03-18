package com.forgefrontier.forgefrontier.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class PreviousInventoryRunnable extends BukkitRunnable {
    private Player p;
    private Inventory inv;
    public PreviousInventoryRunnable(Player p, Inventory i) {
        this.p = p;
        this.inv = i;
    }
    @Override
    public void run() {
        p.openInventory(inv);
    }
}
