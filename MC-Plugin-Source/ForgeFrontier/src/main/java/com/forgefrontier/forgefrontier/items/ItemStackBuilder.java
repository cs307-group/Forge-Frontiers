package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Build Basic ItemStacks
 */
public class ItemStackBuilder {

    Material m;
    String displayName;
    int amt;
    List<String> lore;
    boolean unbreakable;

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
        copy = m.clone();
        this.displayName = ItemUtil.itemName(m);
        this.lore = m.getItemMeta().getLore();
        if (lore == null) lore = new ArrayList<>();
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
        if (copyBuild) {
            ItemMeta m = copy.getItemMeta();
            m.setDisplayName(name);
            copy.setItemMeta(m);
            return this;
        }
        if (name.equals(ItemUtil.simpleRename(m))) {
            displayName = null;
        } else {
            displayName = name;
        }
        return this;
    }

    /** Set the amount of the generated item stack */
    public ItemStackBuilder setAmount(int amt) {
        if (copyBuild) {copy.setAmount(amt);}
        this.amt = amt;
        return this;
    }

    public ItemStackBuilder setFullLore(String newlineSepLore) {
//        if (copyBuild) {
//            ItemMeta m = copy.getItemMeta();
//            m.setLore((List.of(newlineSepLore.split("\n"))));
//            copy.setItemMeta(m);
//            return this;
//        }
        isloreModified = true;
        if (newlineSepLore.isEmpty()) {
            return this;
        }
        this.lore = (List.of(newlineSepLore.split("\n")));
        return this;
    }

    public ItemStackBuilder setFullLore(List<String> lore) {
//        if (copyBuild) {
//            ItemMeta m = copy.getItemMeta();
//            m.setLore(lore);
//            copy.setItemMeta(m);
//            return this;
//        }
        isloreModified = true;
        this.lore = new ArrayList<>(lore);
        return this;
    }

    /** Add a new line to the lore of the generated item */
    public ItemStackBuilder addLoreLine(String loreLine) {
//        if (copyBuild) {
//            ItemMeta m = copy.getItemMeta();
//            List<String> lore = m.getLore();
//            if (lore == null) lore = new ArrayList<>();
//            lore.add(loreLine);
//            m.setLore(lore);
//            return this;
//        }
        isloreModified = true;
        if(this.lore == null) {
            this.lore = new ArrayList<>();
        }
        List<String> str = Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', loreLine).split("\n")
        );
        this.lore.addAll(str);

        return this;
    }

    /**
     * Add all build attributes to a newly instantiated itemstack class
     *
     */
    public ItemStack build() {
        if (copyBuild) return copy(copy);

        ItemStack itm = new ItemStack(m);
        itm.setAmount(amt);
        ItemMeta meta = itm.getItemMeta();

        if (displayName != null && meta != null) {
            //meta.setDisplayName(displayName);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        }
        if(lore != null && meta != null)
            meta.setLore(lore);
        if(meta != null)
            meta.setUnbreakable(this.unbreakable);
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
        if (isloreModified) {
            copy.getItemMeta().setLore(lore);
        }
        copy.setAmount(amt);
        return copy;
//        ItemStack i = new ItemStack(other.getType());
//        i.setItemMeta(other.getItemMeta());
//        if (lore != null && isloreModified && i.getItemMeta() != null) {
//            ItemMeta imeta = i.getItemMeta();
//            imeta.setLore(lore);
//            i.setItemMeta(imeta);
//        }
//        i.setAmount(amt);
//        return i;
    }

    public ItemStack copy(ItemStack other) {
        if (isloreModified) {
            ItemMeta m = copy.getItemMeta();
            m.setLore(lore);
            copy.setItemMeta(m);
        }
        return copy;
//        ItemStack i = new ItemStack(other.getType());
//        i.setItemMeta(other.getItemMeta());
//        if (lore != null && isloreModified && i.getItemMeta() != null) {
//            ItemMeta imeta = i.getItemMeta();
//            imeta.setLore(lore);
//            i.setItemMeta(imeta);
//        }
//        return i;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }
}
