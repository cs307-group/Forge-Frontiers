package com.forgefrontier.forgefrontier.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(e.getInventory().getHolder() instanceof BaseInventoryHolder) {
            BaseInventoryHolder holder = (BaseInventoryHolder) e.getInventory().getHolder();
            holder.onOpen(e);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof BaseInventoryHolder) {
            BaseInventoryHolder holder = (BaseInventoryHolder) e.getInventory().getHolder();
            holder.onClick(e);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getInventory().getHolder() instanceof BaseInventoryHolder) {
            BaseInventoryHolder holder = (BaseInventoryHolder) e.getInventory().getHolder();
            holder.onClose(e);
        }
    }

}
