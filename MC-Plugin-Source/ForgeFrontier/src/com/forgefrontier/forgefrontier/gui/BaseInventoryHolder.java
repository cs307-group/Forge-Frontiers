package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
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
import org.bukkit.scheduler.BukkitRunnable;

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
    public BaseInventoryHolder(int size, String name) {
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', name));
        this.handlers = new InventoryClickHandler[size];
    }


    public BaseInventoryHolder addHandler(int slot, InventoryClickHandler handler) {
        this.handlers[slot] = handler;

        return this;
    }
    public BaseInventoryHolder removeHandler(int slot) {
        this.handlers[slot] = null;
        return this;
    }
    public BaseInventoryHolder setItem(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);

        return this;
    }

    public BaseInventoryHolder replaceItemTemporarily(int slotId, ItemStack itemStack) {
        ItemStack oldItem = this.inventory.getItem(slotId);
        InventoryClickHandler oldHandler = this.handlers[slotId];

        this.inventory.setItem(slotId, itemStack);
        this.handlers[slotId] = null;

        Bukkit.getScheduler().runTaskLater(ForgeFrontier.getInstance(), () -> {
            this.inventory.setItem(slotId, oldItem);
            this.handlers[slotId] = oldHandler;
        }, 5 * 20);

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
        if (e.getSlot() < 0 || e.getSlot() > size) return;
        if(handlers[e.getSlot()] != null)
            handlers[e.getSlot()].onClick(e);
    }

    public void onClose(InventoryCloseEvent e) {

    }

    public void onOpen(InventoryOpenEvent e) {

    }

}
