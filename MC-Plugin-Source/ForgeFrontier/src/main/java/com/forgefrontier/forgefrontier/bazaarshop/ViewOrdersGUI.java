package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class ViewOrdersGUI extends BaseInventoryHolder {

    private UUID playerID;
    private BazaarManager bazaarManager;

    public ViewOrdersGUI(int size, String name, UUID player) {
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
        ArrayList<BazaarEntry> listings = bazaarManager.getPlayerListings(playerID);
        if (listings == null) return;
        listings.removeIf( (be) -> !be.getValid() );    // Remove invalid listings

        for (int i = 0; i < listings.size(); i++) {
            int slot = 1 + (i/7 + 1) * 9 + i % 7;
            if (slot > rows * 7) break;
            BazaarEntry be = listings.get(i);
            this.setItem(slot, getDisplayable(be));
            this.addHandler(slot, (e) -> openRemove(e, be));
        }
    }
    public void openRemove(InventoryClickEvent e, BazaarEntry listing) {
        Player p = (Player) e.getWhoClicked();
        p.openInventory(new ConfirmationHolder("Delete Listing?",
                null,() -> {
            p.closeInventory();
            bazaarManager.removeListingAsync(listing, (b) ->
                    p.sendMessage("" + BazaarManager.bazaarPrefix + "Refunded listing!"));
        }).getInventory());
    }
    public ItemStack getDisplayable(BazaarEntry be) {
        ItemStack realItem = bazaarManager.getRealItem(be.getSlotID());
        StringBuilder loreLines = new StringBuilder();
        if (!be.getBType()) {
            // sell order
            loreLines.append(ChatColor.RED).append("Sell\n");
        } else {
            loreLines.append(ChatColor.GREEN).append("Buy\n");
        }
        loreLines.append(ChatColor.GRAY).append("Price: ").append(ChatColor.GOLD)
                .append(be.getPrice()).append('g').append('\n');
        loreLines.append(ChatColor.GRAY).append("Amount: ").append(ChatColor.BLUE).append(be.getAmount()).append('\n');

        double avgprice = Math.round(100.0 * be.getPrice() / (double) be.getAmount()) / 100.0;
        loreLines.append(ChatColor.DARK_GRAY).append("P/I: ").append(ChatColor.DARK_GRAY)
                .append(avgprice).append('g').append("\n");
        ItemStack displayable = new ItemStackBuilder(realItem)
                .addLoreLine(loreLines.toString()).build();
        return displayable;
    }





}
