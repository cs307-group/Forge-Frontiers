package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BazaarGUI extends BaseInventoryHolder {
    private BazaarManager bazaarMgr;
    public BazaarGUI() {
        super(54, "" + ChatColor.GOLD + ChatColor.BOLD + "Bazaar");
        this.bazaarMgr = ForgeFrontier.getInstance().getBazaarManager();
        updateGUI();
    }


    public void updateGUI() {
        int idx = 0;
        fillBorders();
        ArrayList<ItemStack> displayItems = bazaarMgr.getDisplayItems();
        for (int i = 0; i < 4; i++) {
            int rbegin = 9 * i + 1;
            for (int j = 0; j < 7; j++) {
                this.setItem(rbegin + j, displayItems.get(idx));
                idx++;
            }
        }
    }

    public void fillBorders() {
        ItemStack item = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName("").build();
        for (int i = 0; i < 9; i++) {
            this.setItem(i,item);
            this.setItem(5 * 9 + i,item);
        }
        for (int i = 0; i < 6; i++) {
            this.setItem(9 * i, item);
            this.setItem(9*i+8,item);
        }
    }


}
