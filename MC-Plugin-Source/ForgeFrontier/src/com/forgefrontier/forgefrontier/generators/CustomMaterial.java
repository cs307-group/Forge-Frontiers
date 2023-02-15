package com.forgefrontier.forgefrontier.generators;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomMaterial {

    ItemStack representation;
    String name;

    public CustomMaterial(ItemStack representation, String name) {
        this.representation = representation;
        this.name = name;
    }

    public void collect(Player p, int amt) {

    }

    public boolean hasBalance(Player p, int amt) {
        return false;
    }

    public void take(Player p, int amt) {

    }

}
