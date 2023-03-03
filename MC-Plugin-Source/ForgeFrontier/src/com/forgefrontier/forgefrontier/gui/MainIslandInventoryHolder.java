package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainIslandInventoryHolder extends BaseInventoryHolder {


    public MainIslandInventoryHolder() {
        super(27, "Island");

        this.fillPanes();

        this.setItem(9 + 2, new ItemStackBuilder(Material.OAK_DOOR)
            .setDisplayName("&aGo to your island")
            .build());
        this.addHandler(9 + 2, (e) -> {
            Player p = (Player) e.getWhoClicked();
            p.performCommand("island home");
        });

        this.setItem(9 + 6, new ItemStackBuilder(Material.IRON_DOOR)
            .setDisplayName("&aGo to spawn")
            .build());
        this.addHandler(9 + 6, (e) -> {
            Player p = (Player) e.getWhoClicked();
            p.performCommand("spawn");
        });

    }
}
