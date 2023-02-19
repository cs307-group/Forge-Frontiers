package com.forgefrontier.forgefrontier.items;

import net.minecraft.nbt.NBTBase;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CustomItem {

    /** The base code for determining the type of item it is. */
    String code;

    /** Accumulator functions that create an ItemStack incrementally through data in a CustomItemInstance. */
    List<ItemStackAccumulator> itemStackAccumulators;

    /** Accumulator functions that create a CustomItemInstance incrementally through data in an ItemStack. */
    List<CustomItemInstanceAccumulator> instanceAccumulators;

    /**
     * CustomItem constructor
     *
     * Defines the attributes and registers the ItemStackAccumulator
     *
     * @param code the value to be set to the 'code' attribute. Defines the type of item the CustomItem is
     */
    public CustomItem(String code) {
        this.code = code;
        this.itemStackAccumulators = new ArrayList<>();
        this.instanceAccumulators = new ArrayList<>();

        // Every custom item will have a base code that needs to be used to identify it.
        // This function inserts that code into the ItemStack.
        this.registerItemStackAccumulator((itemInstance, itemStack) -> {
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

            nmsItem.t().a("base-code", code);
            //nmsItem.t().a("custom-data", itemInstance.data.toJSONString());

            itemStack = CraftItemStack.asBukkitCopy(nmsItem);

            itemStack.setAmount(itemInstance.amount);

            return itemStack;
        });

        // Set the CustomItemInstance values for general custom items.
        this.registerInstanceAccumulator(((instance, itemStack) -> {
            instance.base = this;
            //net.minecraft.world.item.ItemStack item2 = CraftItemStack.asNMSCopy(itemStack);
            //instance.data = (JSONObject) JSONValue.parse(item2.t().l("custom-data"));
            if(itemStack == null) instance.amount = 1;
            else instance.amount = itemStack.getAmount();
            return instance;
        }));
    }

    // Ran whenever a player interacts with the custom item in their hand.
    public abstract void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance);

    // Ran whenever a player attacks an entity with the custom item.
    public abstract void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance);

    // TODO: Add more events, whenever necessary

    // Convert an ItemStack to a CustomItemInstance to access the data of the custom item.
    public CustomItemInstance asInstance(ItemStack item) {
        CustomItemInstance instance = null;
        for(CustomItemInstanceAccumulator accumulator: this.instanceAccumulators) {
            instance = accumulator.accumulate(instance, item);
        }
        return instance;
    }

    public String getCode() {
        return this.code;
    }

    public List<ItemStackAccumulator> getItemStackAccumulators() {
        return this.itemStackAccumulators;
    }

    public List<CustomItemInstanceAccumulator> getInstanceAccumulators() {
        return this.instanceAccumulators;
    }

    /**
     * Register a new item stack accumulator to add a new layer to create the item stack from the instance.
     * The later it is added, the earlier it will run. At the start, the itemstack passed in will be null.
     *
     * @param accumulator
     */
    public void registerItemStackAccumulator(ItemStackAccumulator accumulator) {
        this.itemStackAccumulators.add(0, accumulator);
    }

    // Register a new item stack accumulator to add a new layer to create the instance from the item stack.
    // The later it is added, the earlier it will run. At the start, the instance passed in will be null.
    public void registerInstanceAccumulator(CustomItemInstanceAccumulator accumulator) {
        this.instanceAccumulators.add(0, accumulator);
    }

}
