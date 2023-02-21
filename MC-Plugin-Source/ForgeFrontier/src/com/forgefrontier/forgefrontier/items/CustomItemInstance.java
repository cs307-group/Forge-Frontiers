package com.forgefrontier.forgefrontier.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class CustomItemInstance {

    /** The base custom item the instance is an instance of */
    private CustomItem base;

    /** The amount of the item in the stack */
    private int amount;

    // Additional data for the item
    //JSONObject data;

    /**
     * asItemStack converts an instance into an ItemStack using the accumulator functions.
     *
     * @return the CustomItemInstance as an ItemStack
     */
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

    public int getAmount() {
        return this.amount;
    }

    protected void setBase(CustomItem base) {
        this.base = base;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /*
    public JSONObject getData() {
        return this.data;
    }
    */

}
