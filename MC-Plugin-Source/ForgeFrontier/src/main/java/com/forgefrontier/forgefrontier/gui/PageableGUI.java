package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Enumeration;

public class PageableGUI extends BaseInventoryHolder{
    private Enumeration<ItemStack> items;
    private static final int N_ITEMS_PAGE = 9 * 6 - 9 - 9 - 4 - 4;
    ItemStack prev;
    ItemStack next;

    private final int pageno = 0;
    public PageableGUI(Enumeration<ItemStack> itms) {
        super(54);
        this.items = itms;

    }
    public PageableGUI(Enumeration<ItemStack> itms, String name) {
        super(54, name);
        prev = new ItemStackBuilder(Material.PAPER)
                .setDisplayName("Previous").build();
        next = new ItemStackBuilder(Material.PAPER)
                .setDisplayName("Next").build();
        this.items = itms;
    }
    public void setItems(Enumeration<ItemStack> itms) {
        this.items = itms;
        prev = new ItemStackBuilder(Material.PAPER)
                .setDisplayName("Previous").build();
        next = new ItemStackBuilder(Material.PAPER)
                .setDisplayName("Next").build();
    }
    public void fillBorders() {
        ItemStack item = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName("").build();
        for (int i = 0; i < 9; i++) {
            this.setItem(i,item);
        }
        for (int i = 0; i < 6; i++) {
            this.setItem(9 * i, item);
            this.setItem(9*i+8,item);
        }
        this.setItem(9 * 5 + 3, prev);
        this.setItem(9 * 5 + 5, next);
    }

    public void update() {

    }


}
