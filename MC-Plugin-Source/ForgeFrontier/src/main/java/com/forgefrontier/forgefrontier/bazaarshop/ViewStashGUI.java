package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class ViewStashGUI extends BaseInventoryHolder {

    BazaarManager bazaarManager;
    UUID playerID;
    public ViewStashGUI(int size, String name, UUID player) {
        super(size, name);
        bazaarManager = ForgeFrontier.getInstance().getBazaarManager();
        this.playerID = player;
        this.fillBorders();
        this.setItems();
    }
    public void fillBorders() {
        ItemStack item = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName("").build();
        int rows = this.size / 9 - 1;

        for (int i = 0; i < 9; i++) {
            this.setItem(i,item);
            this.setItem(rows * 9 + i,item);
        }
        for (int i = 0; i <= rows; i++) {
            this.setItem(9 * i, item);
            this.setItem(9*i+8,item);
        }
    }

    public void setItems() {
        int rows = this.size / 9 - 1;
        ArrayList<BazaarStash> listings = bazaarManager.getBazaarStash(playerID);
        if (listings == null) return;

        for (int i = 0; i < listings.size(); i++) {
            int slot = 1 + (i/7 + 1) * 9 + i % 7;
            if (slot > rows * 7) break;
            BazaarStash bs = listings.get(i);
            this.setItem(slot, getDisplayable(bs));

            this.addHandler(slot, (e) -> clickRedeem(e, bs, slot));
        }
    }

    public void clickRedeem(InventoryClickEvent e, BazaarStash bs, int slot) {
        Player p = (Player) e.getWhoClicked();
        bazaarManager.doStashRedeem(p,bs);
        setItem(slot,new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE)
                            .setDisplayName("Redeemed!").build());
        this.removeHandler(slot);
    }
    public ItemStack getDisplayable(BazaarStash bs) {
        ItemStack realItem = bazaarManager.getRealItem(bs.getItemID());
        String loreLines = ChatColor.WHITE + "Amount: " +
                ChatColor.GREEN + bs.getAmount() + "x";
        return new ItemStackBuilder(realItem)
                .addLoreLine(loreLines).build();
    }



}
