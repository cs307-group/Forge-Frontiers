package com.forgefrontier.forgefrontier.items;

import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class UniqueCustomItem extends CustomItem {

    // CustomItemInstance for unique items. They will have their own unique id to identify them.
    public static abstract class UniqueCustomItemInstance extends CustomItemInstance {
        UUID uniqueId;
        public UniqueCustomItemInstance(ItemStack itemStack) {
            super(itemStack);
        }
    }

    public UniqueCustomItem(String code) {
        super(code);

        // Because this is an abstract class, there will be a subclass that creates the ItemStack to start off.
        // So we can safely assume itemStack is not null.
        // So all we have to do here is add the UniqueId attribute to the ItemStack based off the instance data.
        this.registerItemStackAccumulator(((instance, itemStack) -> {
            UniqueCustomItemInstance inst = (UniqueCustomItemInstance) instance;

            inst.getData().put("unique-code", inst.uniqueId.toString());

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
            uniqueInstance.uniqueId = UUID.fromString((String) uniqueInstance.getData().get("unique-code"));

            return uniqueInstance;
        }));

    }

}
