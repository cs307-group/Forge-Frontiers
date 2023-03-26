package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.stashes.StashInstance;
import com.forgefrontier.forgefrontier.stashes.StashInventoryHolder;
import com.forgefrontier.forgefrontier.stashes.StashItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class StashAddInventoryHolder extends BaseInventoryHolder {

    StashItem stashItem;
    StashInstance stashInstance;

    public StashAddInventoryHolder(StashInstance stashInstance, StashItem stashItem) {
        super(45, "Add items to stash");

        this.stashInstance = stashInstance;
        this.stashItem = stashItem;

        ItemStack pane = this.getPaneItem();
        for(int i = 1; i < 9; i++) {
            this.setItem(i + 9 * 4, pane);
        }
        this.setItem(9 * 4, new ItemStackBuilder(Material.PAPER)
                .setDisplayName("&fSave & Go Back")
                .build()
        );
        this.addHandler(9 * 4, (e) -> {
            e.getWhoClicked().closeInventory();
        });
        for(int i = 0; i < 36; i++) {
            final int ind = i;
            this.addHandler(i, (e) -> {
                ItemStack item = e.getCursor();
                if(CustomItemManager.getCustomItem(item) == stashItem.getItem()) {
                    e.setCancelled(false);
                }
                item = e.getCurrentItem();
                if(CustomItemManager.getCustomItem(item) == stashItem.getItem()) {
                    e.setCancelled(false);
                }
            });
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        int inventoryTotal = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = e.getInventory().getItem(i);
            if (item == null) continue;
            inventoryTotal += item.getAmount();
        }
        int total = stashInstance.getAmount(stashItem) + inventoryTotal;
        if(total > stashItem.getMaxAmount()) {
            stashInstance.setAmount(stashItem, total);
            total -= stashItem.getMaxAmount();
            CustomItemInstance inst = this.stashItem.getItem().asInstance(null);
            while(e.getPlayer().getInventory().firstEmpty() != -1 && total > 0) {
                int amt = Math.min(64, total);
                inst.setAmount(amt);
                total -= amt;
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().firstEmpty(), inst.asItemStack());
            }
            while(total > 0) {
                int amt = Math.min(64, total);
                inst.setAmount(amt);
                total -= amt;
                e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), inst.asItemStack());
            }
        } else {
            stashInstance.setAmount(stashItem, total);
        }
        Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
            e.getPlayer().openInventory(new StashInventoryHolder(this.stashInstance).getInventory());
        });
    }

}
