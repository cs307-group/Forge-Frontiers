package com.forgefrontier.forgefrontier.generators.materials;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemMaterial extends CustomMaterial {

    CustomItem item;

    public ItemMaterial(CustomItem item) {
        super(item.asInstance(null).asItemStack(), item.asInstance(null).asItemStack().getItemMeta().getDisplayName());
        this.item = item;
    }

    @Override
    public int collect(Player p, int amt) {
        ItemStack itemStack = item.asInstance(null).asItemStack();
        int maxStackSize = itemStack.getMaxStackSize();
        int amtLeft = amt;
        int i = 0;
        ItemStack[] contents = p.getInventory().getContents();
        while(amtLeft > 0 && i < 36) {
            if(contents[i] == null || contents[i].getType() == Material.AIR) {
                int amtToGive = Math.min(amtLeft, maxStackSize);
                itemStack.setAmount(amtToGive);
                p.getInventory().setItem(i, itemStack.clone());
                amtLeft -= amtToGive;
            } else {
                CustomItem customItem = CustomItemManager.getCustomItem(contents[i]);
                if(customItem == item) {
                    int amtToGive = Math.min(amtLeft, maxStackSize - contents[i].getAmount());
                    contents[i].setAmount(contents[i].getAmount() + amtToGive);
                    amtLeft -= amtToGive;
                }
            }
            i += 1;
        }
        return amtLeft;
    }

    @Override
    public boolean hasBalance(Player p, int amt) {
        int totalAmt = 0;
        int i = 0;
        ItemStack[] contents = p.getInventory().getContents();
        while(totalAmt < amt && i < 36) {
            if(contents[i] == null) {i++; continue;}
            CustomItem customItem = CustomItemManager.getCustomItem(contents[i]);
            if(customItem == item) {
                totalAmt += contents[i].getAmount();
            }
            i += 1;
        }

        return totalAmt >= amt;
    }

    @Override
    public void take(Player p, int amt) {
        int amtLeft = amt;
        int i = 0;
        ItemStack[] contents = p.getInventory().getContents();
        while(amtLeft > 0 && i < 36) {
            if(contents[i] == null) {i++; continue;}
            CustomItem customItem = CustomItemManager.getCustomItem(contents[i]);
            if(customItem == item) {
                int amtToTake = Math.min(contents[i].getAmount(), amtLeft);
                contents[i].setAmount(contents[i].getAmount() - amtToTake);
                amtLeft -= amtToTake;
            }
            i += 1;
        }
    }
}
