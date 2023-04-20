package com.forgefrontier.forgefrontier.events;

import com.forgefrontier.forgefrontier.fishing.FishingDrop;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FishEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    FishingDrop drop;

    public FishEvent(Player player, FishingDrop drop) {
        super(player);
        this.drop = drop;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public FishingDrop getDrop() {
        return drop;
    }

}
