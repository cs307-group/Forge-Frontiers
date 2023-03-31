package com.forgefrontier.forgefrontier.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemGiver {

    /**
     * Adds item to players inventory, drops if no space.
     * @param p Player
     * @param itm item to give
     */
    public static void giveItem(Player p, ItemStack itm) {
        HashMap<Integer, ItemStack> left = p.getInventory().addItem(itm);
        if (left.size() == 0) return;
        for (Integer lk : left.keySet()) {
            p.getWorld().dropItem(p.getLocation().add(0,1,0),left.get(lk));
        }
    }

    public static void giveItem(Player p, ItemStack itm, int amt) {
        while (amt > 0) {
            int toGive = Math.min(amt, 64);
            itm.setAmount(toGive);
            giveItem(p,itm.clone());
            amt -= toGive;
        }
    }

    // limited to 64
    public static void dropItem(Player p, ItemStack itm) {
        p.getWorld().dropItem(p.getLocation().add(0,1,0), itm);
    }


}
