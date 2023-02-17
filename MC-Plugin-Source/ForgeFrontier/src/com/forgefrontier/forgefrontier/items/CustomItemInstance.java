package com.forgefrontier.forgefrontier.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class CustomItemInstance {

    // The base custom item the instance is an instance of
    CustomItem base;
    // The amount of the item in the stack
    int amount;
    // Additional data for the item
    //JSONObject data;

    // Convert an instance into an ItemStack using the accumulator functions.
    public ItemStack asItemStack() {
        ItemStack itemStack = null;
        for(ItemStackAccumulator accumulator: base.getItemStackAccumulators()) {
            itemStack = accumulator.accumulate(this, itemStack);
        }
        return itemStack;
    }

    public CustomItem getBaseItem() {
        return base;
    }

    /*
    public JSONObject getData() {
        return this.data;
    }
    */

}
