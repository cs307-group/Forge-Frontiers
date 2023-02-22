package com.forgefrontier.forgefrontier.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Build Basic ItemStacks
 */
public class ItemStackBuilder {

    Material m;
    String displayName;
    int amt;
    List<String> lore;

    ItemStack copy;
    boolean copyBuild;

    /** Begin Building w/ material of itemstack */
    public ItemStackBuilder(Material m) {
        this.m = m;
        this.amt = 1;
        copyBuild = false;
    }
    /** Copies some references from itemstack */
    public ItemStackBuilder(ItemStack m) {
        copy = m;
        amt = m.getAmount();
        copyBuild = true;
    }

    /** Set the display name of the generated item */
    public ItemStackBuilder setDisplayName(String name) {
        displayName = name;
        return this;
    }

    /** Set the amount of the generated item stack */
    public ItemStackBuilder setAmount(int amt) {
        this.amt = amt;
        return this;
    }

    /** Add a new line to the lore of the generated item */
    public ItemStackBuilder addLoreLine(String loreLine) {
        if(this.lore == null)
            this.lore = new ArrayList<>();
        this.lore.add(loreLine);

        return this;
    }

    /**
     * Add all build attributes to a newly instantiated itemstack class
     *
     */
    public ItemStack build() {
        if (copyBuild) return copy(copy, amt);

        ItemStack itm = new ItemStack(m);
        itm.setAmount(amt);
        ItemMeta meta = itm.getItemMeta();

        if (displayName != null && meta != null)
            meta.setDisplayName(displayName);
        if(lore != null)
            meta.setLore(lore);
        itm.setItemMeta(meta);
        return itm;
    }

    /**
     * Used to copy build a itemstack
     * @param other Itemstack to copy
     * @param amt   Amount of items of this item stack
     * @return  New Itemstack
     */
    public ItemStack copy(ItemStack other, int amt) {
        ItemStack i = new ItemStack(other.getType());
        i.setItemMeta(other.getItemMeta());
        i.setAmount(amt);
        return i;
    }

    public ItemStack copy(ItemStack other) {
        ItemStack i = new ItemStack(other.getType());
        i.setItemMeta(other.getItemMeta());
        return i;
    }

}
