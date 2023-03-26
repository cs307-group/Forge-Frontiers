package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class StashRemoveInventoryHolder extends BaseInventoryHolder {

    StashItem stashItem;
    StashInstance stashInstance;
    boolean inventoryWasFull;

    public StashRemoveInventoryHolder(StashInstance stashInstance, StashItem stashItem) {
        super(45, "Remove items from stash");
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
                //if(CustomItemManager.getCustomItem(e.getCurrentItem()) == stashItem.getItem()) {
                    e.setCancelled(false);
                //}
            });
        }
        int amt = stashInstance.getAmount(stashItem);
        int i = 0;
        CustomItemInstance itemInstance = stashItem.getItem().asInstance(null);
        while(i < 36 && amt > 0) {
            int stackAmt = Math.min(64, amt);
            amt -= stackAmt;
            itemInstance.setAmount(stackAmt);
            ItemStack itemStack = itemInstance.asItemStack();
            this.setItem(i, itemStack);
            i++;
        }
        if(amt > 0) this.inventoryWasFull = true;
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        int runningAmt = 0;
        if(inventoryWasFull) {
            int amt = 36 * 64;
            int totalAmt = stashInstance.getAmount(stashItem);
            runningAmt = totalAmt - amt;
        }
        for(int i = 0; i < 36; i++) {
            ItemStack item = e.getInventory().getItem(i);
            if(item == null) continue;
            runningAmt += item.getAmount();
        }
        if(runningAmt > stashItem.getMaxAmount()) {
            CustomItemInstance inst = this.stashItem.getItem().asInstance(null);
            while(e.getPlayer().getInventory().firstEmpty() != -1 && runningAmt > stashItem.getMaxAmount()) {
                int amt = Math.min(64, runningAmt - stashItem.getMaxAmount());
                inst.setAmount(amt);
                runningAmt -= amt;
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().firstEmpty(), inst.asItemStack());
            }
            while(runningAmt > stashItem.getMaxAmount()) {
                int amt = Math.min(64, runningAmt - stashItem.getMaxAmount());
                inst.setAmount(amt);
                runningAmt -= amt;
                e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), inst.asItemStack());
            }
        }
        stashInstance.setAmount(stashItem, runningAmt);
        Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
            e.getPlayer().openInventory(new StashInventoryHolder(this.stashInstance).getInventory());
        });
    }

}
