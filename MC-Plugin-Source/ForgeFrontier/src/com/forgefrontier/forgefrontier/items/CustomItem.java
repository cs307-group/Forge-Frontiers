package com.forgefrontier.forgefrontier.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomItem {

    String code;

    public CustomItem(String code) {
        this.code = code;
    }

    public abstract void onInteract(PlayerInteractEvent e);

    protected abstract ItemStack asItemStack();

    public String getCode() {
        return this.code;
    }
}
