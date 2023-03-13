package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.TwoOptionHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BazaarGUI extends BaseInventoryHolder {
    private BazaarManager bazaarMgr;
    public BazaarGUI() {
        super(54, "" + ChatColor.GOLD + ChatColor.BOLD + "Bazaar");
        this.bazaarMgr = ForgeFrontier.getInstance().getBazaarManager();
        initGUI();
    }

    public void initGUI() {
        int idx = 0;
        fillBorders();
        ArrayList<ItemStack> displayItems = bazaarMgr.getLookupItems();
        ItemStack defaultPane = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName("").build();
        for (int i = 1; i < 5; i++) {
            int rbegin = 9 * i + 1;
            for (int j = 0; j < 7; j++) {
                ItemStack displayItem = displayItems.get(idx);
                if (displayItem == null)
                    this.setItem(rbegin + j, defaultPane);
                else {
                    this.setItem(rbegin + j, displayItems.get(idx));
                }
                // Add callbacks to relevant items
                if (displayItem != null && displayItem.getType() != Material.BARRIER &&
                        displayItem.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    int finalIdx = idx;
                    this.addHandler(rbegin + j, (e) -> {
                        Player p = (Player) e.getWhoClicked();
                        slotClick(p, finalIdx);
                    });
                }
                idx++;
            }
        }
    }

    public void slotClick(Player p, int idx) {
        p.openInventory(new TwoOptionHolder("Select Buy/Sell",() -> {}, () -> {}).getInventory());

    }

    public void updateGUI() {
        int idx = 0;
        fillBorders();
        ArrayList<ItemStack> displayItems = bazaarMgr.getLookupItems();
        for (int i = 1; i < 5; i++) {
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
