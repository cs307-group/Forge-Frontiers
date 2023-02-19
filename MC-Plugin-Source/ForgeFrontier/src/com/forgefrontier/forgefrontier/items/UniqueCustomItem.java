package com.forgefrontier.forgefrontier.items;

import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;

import java.util.UUID;

public abstract class UniqueCustomItem extends CustomItem {

    // CustomItemInstance for unique items. They will have their own unique id to identify them.
    public static abstract class UniqueCustomItemInstance extends CustomItemInstance {

        UUID uniqueId;

    }

    public UniqueCustomItem(String code) {
        super(code);

        // Because this is an abstract class, there will be a subclass that creates the ItemStack to start off.
        // So we can safely assume itemStack is not null.
        // So all we have to do here is add the UniqueId attribute to the ItemStack based off the instance data.
        this.registerItemStackAccumulator(((instance, itemStack) -> {
            UniqueCustomItemInstance inst = (UniqueCustomItemInstance) instance;

            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

            nmsItem.t().a("unique-code", inst.uniqueId.toString());

            itemStack = CraftItemStack.asBukkitCopy(nmsItem);

            return itemStack;
        }));

        // Because this is an abstract class, there will be a subclass that creates the instance to start off.
        // So we can safely assume instance is not null.
        // So all we have to do here is add the UniqueId attribute to the ItemStack based off the instance data.
        this.registerInstanceAccumulator(((customItemInstance, itemStack) -> {
            UniqueCustomItemInstance uniqueInstance = (UniqueCustomItemInstance) customItemInstance;

            // If the instance is brand new, not based off an item, give it a random id.
            if(itemStack == null) {
                uniqueInstance.uniqueId = UUID.randomUUID();
                return uniqueInstance;
            }

            // Otherwise, access the id of the existing item, and put it into the custom item's instance.
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

            uniqueInstance.uniqueId = UUID.fromString(nmsItem.t().l("unique-code"));

            return uniqueInstance;
        }));

    }

}
