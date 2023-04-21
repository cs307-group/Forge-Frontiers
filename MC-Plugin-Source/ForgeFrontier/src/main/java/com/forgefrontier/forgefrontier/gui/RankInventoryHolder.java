package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.listeners.ChatManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RankInventoryHolder extends BaseInventoryHolder {


    public RankInventoryHolder(UUID playerId) {
        super(45, "Ranks");

        this.fillPanes();

        this.setItem(9 * 2 + 4, new ItemStackBuilder(Material.PAPER)
                .setDisplayName("&bLoading...")
                .build());

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().getRanks(playerId, (purchasedRanks) -> {
            this.fillPanes();
            if(purchasedRanks.contains("VIP Rank")) {
                this.setItem(9 + 1, new ItemStackBuilder(Material.GREEN_TERRACOTTA)
                    .setDisplayName("&aYou have VIP Rank")
                    .addLoreLine("&7This gives you access to the following features:")
                    .addLoreLine("&7- VIP Chat Prefix")
                    .build());
                this.setItem(9 + 2, new ItemStackBuilder(Material.PAPER)
                    .setDisplayName("&aUse VIP Chat Prefix")
                    .build());
                this.addHandler(9 + 2, (e) -> {
                    ForgeFrontier.getInstance().getChatManager().putFormat(e.getWhoClicked().getUniqueId(), ChatManager.ChatFormat.VIP);
                });
            } else {
                this.setItem(9 + 1, new ItemStackBuilder(Material.RED_TERRACOTTA)
                    .setDisplayName("&aYou do not have VIP Rank")
                    .addLoreLine("&7This gives you access to the following features:")
                    .addLoreLine("&7- VIP Chat Prefix")
                    .build());
            }
            if(purchasedRanks.contains("Elite Rank")) {
                this.setItem(9*2 + 1, new ItemStackBuilder(Material.BLUE_TERRACOTTA)
                    .setDisplayName("&9You have Elite Rank")
                    .addLoreLine("&7This gives you access to the following features:")
                    .addLoreLine("&7- Elite Chat Prefix")
                    .build());
                this.setItem(9*2 + 2, new ItemStackBuilder(Material.PAPER)
                    .setDisplayName("&9Use Elite Chat Prefix")
                    .build());
                this.addHandler(9*2 + 2, (e) -> {
                    ForgeFrontier.getInstance().getChatManager().putFormat(e.getWhoClicked().getUniqueId(), ChatManager.ChatFormat.ELITE);
                });
            } else {
                this.setItem(9*2 + 1, new ItemStackBuilder(Material.RED_TERRACOTTA)
                    .setDisplayName("&aYou do not have Elite Rank")
                    .addLoreLine("&7This gives you access to the following features:")
                    .addLoreLine("&7- Elite Chat Prefix")
                    .build());
            }
            if(purchasedRanks.contains("Legend Rank")) {
                this.setItem(9*3 + 1, new ItemStackBuilder(Material.MAGENTA_TERRACOTTA)
                    .setDisplayName("&dYou have Legend Rank")
                    .addLoreLine("&7This gives you access to the following features:")
                    .addLoreLine("&7- Elite Chat Prefix")
                    .build());
                this.setItem(9*3 + 2, new ItemStackBuilder(Material.PAPER)
                    .setDisplayName("&dUse Legend Chat Prefix")
                    .build());
                this.addHandler(9*3 + 2, (e) -> {
                    ForgeFrontier.getInstance().getChatManager().putFormat(e.getWhoClicked().getUniqueId(), ChatManager.ChatFormat.LEGEND);
                });
            } else {
                this.setItem(9*3 + 1, new ItemStackBuilder(Material.RED_TERRACOTTA)
                    .setDisplayName("&aYou do not have Legend Rank")
                    .addLoreLine("&7This gives you access to the following features:")
                    .addLoreLine("&7- Legend Chat Prefix")
                    .build());
            }
        });

    }

}
