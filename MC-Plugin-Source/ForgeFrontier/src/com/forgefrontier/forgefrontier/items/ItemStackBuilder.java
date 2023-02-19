package com.forgefrontier.forgefrontier.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Build Basic ItemStacks
 */
public class ItemStackBuilder {

    Material m;
    String displayName;

    /** Begin Building w/ material of itemstack */
    public ItemStackBuilder(Material m) {
        this.m = m;
    }
    public ItemStackBuilder(ItemStack m) {}

    /** Setter */
    public void setDisplayName(String name) {
        displayName = name;
    }

    /**
     * Add all build attributes to a newly instantiated itemstack class
     *
     * */
    public ItemStack build() {
        ItemStack itm = new ItemStack(m);
        ItemMeta meta = itm.getItemMeta();

        if (displayName != null && meta != null)
            meta.setDisplayName(displayName);
        itm.setItemMeta(meta);
        return itm;
    }

    public ItemStack build(ItemStack other, int amt) {
        ItemStack i = new ItemStack(other.getType());
        i.setItemMeta(other.getItemMeta());
        i.setAmount(amt);
        return i;
    }




}
