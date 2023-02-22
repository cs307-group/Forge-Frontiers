package com.forgefrontier.forgefrontier.shop;

import org.bukkit.entity.Player;

import java.util.UUID;

public class ShopPlayer {
    private final UUID uuid;
    private final String name;

    public ShopPlayer(UUID UUID, String name) {
        this.uuid = UUID;
        this.name = name;
    }
    public ShopPlayer(Player p) {
        this.uuid = p.getUniqueId();
        this.name = p.getName();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return name;
    }


}
