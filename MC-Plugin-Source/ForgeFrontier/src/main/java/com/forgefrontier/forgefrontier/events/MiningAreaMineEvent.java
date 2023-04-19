package com.forgefrontier.forgefrontier.events;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.mining.MiningArea;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class MiningAreaMineEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    MiningArea area;
    CustomItem resource;

    public MiningAreaMineEvent(Player player, MiningArea area, CustomItem resource) {
        super(player);
        this.area = area;
        this.resource = resource;
    }

    public MiningArea getArea() {
        return area;
    }

    public CustomItem getResource() {
        return resource;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
