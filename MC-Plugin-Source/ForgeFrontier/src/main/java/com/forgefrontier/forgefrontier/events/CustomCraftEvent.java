package com.forgefrontier.forgefrontier.events;

import com.forgefrontier.forgefrontier.items.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class CustomCraftEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    CustomItem result;

    public CustomCraftEvent(Player player, CustomItem result) {
        super(player);
        this.result = result;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public CustomItem getResult() {
        return result;
    }

}
