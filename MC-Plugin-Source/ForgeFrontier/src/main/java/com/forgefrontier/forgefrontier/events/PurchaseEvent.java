package com.forgefrontier.forgefrontier.events;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PurchaseEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public enum PurchaseType {
        BAZAAR_ORDER,
        BAZAAR_DIRECT,
        MARKET,
        GEAR,
        GENERATOR,
        STASH;
    }

    PurchaseType type;
    ItemStack item;
    CustomItemInstance customItemInstance;

    public PurchaseEvent(Player player, PurchaseType type, ItemStack item, CustomItemInstance customItem) {
        super(player);
        this.type = type;
        this.item = item;
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

    public CustomItemInstance getCustomItemInstance() {
        return customItemInstance;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
