package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItemInstance;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.gui.StashAddInventoryHolder;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StashInventoryHolder extends BaseInventoryHolder {

    public StashInventoryHolder(StashInstance stashInstance) {
        super(45, stashInstance.getStash().getFriendlyName());

        this.fillPanes();

        /*for(int x = 0; x < 7; x++) {
            for(int y = 0; y < 2; y++) {
                int slotId = (x + 1) + (y+1) * 9;
                this.setItem(slotId, new ItemStack(Material.AIR));
            }
        }*/


        int i = 0;
        for(StashItem stashItem: stashInstance.getStash().getStashItems()) {
            ItemStack itemStack = stashItem.getItem().asInstance(null).asItemStack();
            String displayName = itemStack.getItemMeta().getDisplayName();
            List<String> lore = itemStack.getItemMeta().getLore();
            this.setItem(i + 1 + 9, new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayName("&aAdd more " + ChatColor.stripColor(displayName) + "(s) into the stash")
                .build()
            );
            this.addHandler(i + 1 + 1 * 9, (e) -> {
                e.getWhoClicked().openInventory(new StashAddInventoryHolder(stashInstance, stashItem).getInventory());
            });
            this.setItem(i + 1 + 3 * 9, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
                    .setDisplayName("&cRemove " + ChatColor.stripColor(displayName) + "(s) from the stash")
                    .build()
            );
            this.addHandler(i + 1 + 3 * 9, (e) -> {
                e.getWhoClicked().openInventory(new StashRemoveInventoryHolder(stashInstance, stashItem).getInventory());
            });
            this.setItem(i + 1 + 2 * 9, new ItemStackBuilder(itemStack.getType())
                .setAmount(Math.max(1, Math.min(64, stashInstance.getAmount(stashItem))))
                .setDisplayName(displayName + "&7 - &f" + stashInstance.getAmount(stashItem) + " &7/ &f" + stashItem.getMaxAmount())
                .setFullLore(lore)
                .build()
            );
        }

        this.setItem(7 + 9 * 3, new ItemStackBuilder(Material.RED_WOOL)
            .setDisplayName(ChatColor.RED + "Remove Stash")
            .build()
        );

        this.addHandler(7 + 9 * 3, (e) -> {
            if(e.getWhoClicked().getInventory().firstEmpty() == -1) {
                this.replaceItemTemporarily(7 + 9 * 3, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE)
                        .setDisplayName("&cUnable to remove this generator. You do not have space in your inventory.")
                        .build());
                return;
            }
            ConfirmationHolder confirmHolder = new ConfirmationHolder("&cWould you like to pickup this stash?", this.getInventory(), () -> {
                ForgeFrontier.getInstance().getStashManager().removeStashInstance(stashInstance);
                PlaceStashItemInstance itemInstance = (PlaceStashItemInstance) CustomItemManager.getCustomItem("PlaceStashBlock").asInstance(null);
                itemInstance.stashId = stashInstance.getStash().getId();
                e.getWhoClicked().getInventory().addItem(itemInstance.asItemStack());
                e.getWhoClicked().closeInventory();
            }, false);
            e.getWhoClicked().openInventory(confirmHolder.getInventory());
        });

    }

}
