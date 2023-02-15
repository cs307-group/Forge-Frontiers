package com.forgefrontier.forgefrontier.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BaseInventoryHolder implements InventoryHolder {

    public void fillPanes() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLACK.toString());
        item.setItemMeta(meta);
        this.fill(item);
    }

    public interface InventoryClickHandler {
        void onClick(InventoryClickEvent e);
    }

    int size;
    Inventory inventory;

    InventoryClickHandler[] handlers;

    public BaseInventoryHolder(int size) {
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size);
        this.handlers = new InventoryClickHandler[size];
    }

    public BaseInventoryHolder addHandler(int slot, InventoryClickHandler handler) {
        this.handlers[slot] = handler;

        return this;
    }

    public BaseInventoryHolder setItem(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);

        return this;
    }

    public BaseInventoryHolder fill(ItemStack item) {
        for(int i = 0; i < this.size; i++) {
            this.setItem(i, item);
        }
        return this;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if(handlers[e.getSlot()] != null)
            handlers[e.getSlot()].onClick(e);
    }

    public void onClose(InventoryCloseEvent e) {

    }

    public void onOpen(InventoryOpenEvent e) {

    }

}
