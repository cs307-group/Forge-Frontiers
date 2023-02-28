package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class CustomItem {

    /** The base code for determining the type of item it is. */
    private final String code;

    /** Accumulator functions that create an ItemStack incrementally through data in a CustomItemInstance. */
    private final List<ItemStackAccumulator> itemStackAccumulators;

    /** Accumulator functions that create a CustomItemInstance incrementally through data in an ItemStack. */
    private final List<CustomItemInstanceAccumulator> instanceAccumulators;

    /**
     * CustomItem constructor
     *
     * Defines the attributes of a base custom item
     * and registers the appropriate accumulators to set those attributes.
     *
     * @param code A string that is used to identify the type of CustomItem an item is
     */
    public CustomItem(String code) {
        this.code = code;
        this.itemStackAccumulators = new ArrayList<>();
        this.instanceAccumulators = new ArrayList<>();

        // Every custom item will have a base code that needs to be used to identify it.
        // This function inserts that code into the ItemStack.
        this.registerItemStackAccumulator((itemInstance, itemStack) -> {
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

            assert(nmsItem.t() != null);

            nmsItem.t().a("base-code", code);
            nmsItem.t().a("custom-data", itemInstance.data.toJSONString());
            System.out.println("custom-data:\n" + itemInstance.data.toJSONString());

            itemStack = CraftItemStack.asBukkitCopy(nmsItem);

            itemStack.setAmount(itemInstance.getAmount());

            return itemStack;
        });

        // Set the CustomItemInstance values for general custom items.
        this.registerInstanceAccumulator(((instance, itemStack) -> {
            instance.setBase(this);

            if(itemStack == null) instance.setAmount(1);
            else instance.setAmount(itemStack.getAmount());
            return instance;
        }));
    }

    /** Ran whenever a player interacts with the custom item in their hand. */
    public abstract void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance);

    /** Ran whenever a player attacks an entity with the custom item. */
    public abstract void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance, FFPlayer ffPlayer);

    // TODO: Add more events, whenever necessary

    /** Convert an ItemStack to a CustomItemInstance to access the data of the custom item. */
    public CustomItemInstance asInstance(ItemStack item) {
        CustomItemInstance instance = null;
        for(CustomItemInstanceAccumulator accumulator: this.instanceAccumulators) {
            try {
                instance = accumulator.accumulate(instance, item);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
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
     * The later it is added, the earlier it will run. At the start, the ItemStack passed in will be null.
     *
     * @param accumulator the accumulator to be added to the list
     */
    public void registerItemStackAccumulator(ItemStackAccumulator accumulator) {
        try {
            this.itemStackAccumulators.add(0, accumulator);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register a new item stack accumulator to add a new layer to create the instance from the item stack.
     * The later it is added, the earlier it will run. At the start, the instance passed in will be null.
     *
     * @param accumulator the accumulator to be added to the list
     */
    public void registerInstanceAccumulator(CustomItemInstanceAccumulator accumulator) {
        try {
            this.instanceAccumulators.add(0, accumulator);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
