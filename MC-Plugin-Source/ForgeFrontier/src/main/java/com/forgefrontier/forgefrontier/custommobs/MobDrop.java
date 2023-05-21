package com.forgefrontier.forgefrontier.custommobs;

import org.bukkit.inventory.ItemStack;

public class MobDrop {

    ItemStack itemStack;
    double chance;

    public MobDrop(ItemStack itemStack, double chance) {
        this.itemStack = itemStack;
        this.chance = chance;
    }

}
