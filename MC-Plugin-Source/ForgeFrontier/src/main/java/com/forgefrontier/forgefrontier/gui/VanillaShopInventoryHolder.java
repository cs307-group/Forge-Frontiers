package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.shop.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VanillaShopInventoryHolder extends BaseInventoryHolder {

    public VanillaShopInventoryHolder() {
        super(27, "Shop");

        this.fillPanes();

        this.setItem(9 + 3, new ItemStackBuilder(Material.GRASS_BLOCK)
            .setDisplayName("&bNormal Items")
            .build());

        this.addHandler(9+3, (e) -> {
            ((Player) e.getWhoClicked()).performCommand("shop");
        });

        this.setItem(9+5, new ItemStackBuilder(Material.IRON_SWORD)
            .setDisplayName("&bCustom Items")
            .build());

        this.addHandler(9+5, (e) -> {
            ((Player) e.getWhoClicked()).performCommand("gearshop");
        });

    }
}
