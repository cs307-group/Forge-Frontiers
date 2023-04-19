package com.forgefrontier.forgefrontier.events;

import com.forgefrontier.forgefrontier.items.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PurchaseEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public enum PurchaseType {
        BAZAAR,
        MARKET,
        GENERATOR,
        STASH;
    }

    PurchaseType type;
    ItemStack item;
    CustomItem customItem;

    public PurchaseEvent(Player player, PurchaseType type, ItemStack item, CustomItem customItem) {
        super(player);
        this.type = type;
        this.item = item;
        this.customItem = customItem;
    }

    public PurchaseEvent(Player player, PurchaseType type, ItemStack item) {
        this(player, type, item, null);
    }

    public PurchaseType getType() {
        return type;
    }

    public ItemStack getItem() {
        return item;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
