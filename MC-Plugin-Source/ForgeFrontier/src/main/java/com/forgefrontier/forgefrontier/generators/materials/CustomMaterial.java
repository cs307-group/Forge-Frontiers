package com.forgefrontier.forgefrontier.generators.materials;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CustomMaterial {

    ItemStack representation;
    String name;

    public CustomMaterial(ItemStack representation, String name) {
        this.representation = representation;
        this.name = name;
    }

    public ItemStack getRepresentation() {
        return this.representation;
    }

    public abstract int collect(Player p, int amt);

    public abstract boolean hasBalance(Player p, int amt);

    public abstract void take(Player p, int amt);

    @Override
    public String toString() {
        return "(name: " +  this.name + ")";
    }

}
