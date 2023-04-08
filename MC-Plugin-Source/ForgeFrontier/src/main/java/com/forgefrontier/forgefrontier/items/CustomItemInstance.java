package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class CustomItemInstance {

    /** The base custom item the instance is an instance of */
    private CustomItem base;

    /** The amount of the item in the stack */
    private int amount;

    // Additional data for the item
    JSONObject data;

    public CustomItemInstance(@Nullable ItemStack itemStack) {
        this.amount = 1;

        if(itemStack == null) {
            this.data = new JSONObject();
            return;
        }

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        this.data = JSONWrapper.parse(nmsItem.getTag().getString("custom-data"));


    }

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

    public JSONObject getData() {
        return this.data;
    }

}
