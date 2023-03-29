package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
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
    boolean isloreModified = false;

    /** Begin Building w/ material of itemstack */
    public ItemStackBuilder(Material m) {
        this.m = m;
        this.amt = 1;
        copyBuild = false;
    }
    /** Copies some references from itemstack */
    public ItemStackBuilder(ItemStack m) {
        copy = m;
        this.displayName = ItemUtil.itemName(m);
        this.lore = m.getItemMeta().getLore();
        amt = m.getAmount();
        copyBuild = true;
    }

    public ItemStackBuilder(String material) {
        this.m = Material.matchMaterial(material);
        this.amt = 1;
        copyBuild = false;
    }

    /** Set the display name of the generated item */
    public ItemStackBuilder setDisplayName(String name) {
        if (name.equals(ItemUtil.simpleRename(m))) {
            displayName = null;
        } else {
            displayName = name;
        }
        return this;
    }

    /** Set the amount of the generated item stack */
    public ItemStackBuilder setAmount(int amt) {
        this.amt = amt;
        return this;
    }

    public ItemStackBuilder setFullLore(String newlineSepLore) {
        isloreModified = true;
        if (newlineSepLore.isEmpty()) {
            return this;
        }
        this.lore = (List.of(newlineSepLore.split("\n")));
        return this;
    }

    public ItemStackBuilder setFullLore(List<String> lore) {
        isloreModified = true;
        this.lore = new ArrayList<>(lore);
        return this;
    }

    /** Add a new line to the lore of the generated item */
    public ItemStackBuilder addLoreLine(String loreLine) {
        isloreModified = true;
        if(this.lore == null)
            this.lore = new ArrayList<>();
        ArrayList<String> str = new ArrayList<String>(Arrays.asList(loreLine.split("\n")));
        str.forEach((e) -> e = ChatColor.translateAlternateColorCodes('&', e));
        this.lore.addAll(str);
        //this.lore.add(ChatColor.translateAlternateColorCodes('&', loreLine));

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

        if (displayName != null && meta != null) {
            //meta.setDisplayName(displayName);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        }
        if(lore != null && meta != null)
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
        if (lore != null && isloreModified && i.getItemMeta() != null) {
            ItemMeta imeta = i.getItemMeta();
            imeta.setLore(lore);
            i.setItemMeta(imeta);
        }
        i.setAmount(amt);
        return i;
    }

    public ItemStack copy(ItemStack other) {
        ItemStack i = new ItemStack(other.getType());
        i.setItemMeta(other.getItemMeta());
        if (lore != null && isloreModified && i.getItemMeta() != null) {
            ItemMeta imeta = i.getItemMeta();
            imeta.setLore(lore);
            i.setItemMeta(imeta);
        }
        return i;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
