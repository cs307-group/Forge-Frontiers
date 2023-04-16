package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import world.bentobox.bentobox.BentoBox;

public class CreateIslandInventoryHolder extends BaseInventoryHolder {

    public CreateIslandInventoryHolder() {
        super(27, "Create Island");

        this.fillPanes();

        this.setItem(9 + 4, new ItemStackBuilder(Material.GRASS_BLOCK)
                .setDisplayName("&bYou do not current have an island.")
                .addLoreLine("&aClick here to create a new island.")
                .build());

        this.addHandler(9 + 4, (e) -> {
            ((Player) e.getWhoClicked()).performCommand("bsb create");
            e.getWhoClicked().closeInventory();
        });

    }

}
