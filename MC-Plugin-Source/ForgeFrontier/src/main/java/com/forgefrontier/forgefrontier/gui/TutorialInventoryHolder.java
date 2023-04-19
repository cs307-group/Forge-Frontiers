package com.forgefrontier.forgefrontier.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TutorialInventoryHolder extends BaseInventoryHolder {


    public TutorialInventoryHolder() {
        super(54, "Tutorial");
        this.fillPanes();
        for(int i = 0; i < 7 * 4; i++) {
            int x = i % 7;
            int y = i / 7;
            int slot = x + y * 9;
            this.setItem(slot, new ItemStack(Material.AIR));
        }
    }
}
