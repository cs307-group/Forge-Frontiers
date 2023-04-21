package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.inventory.ItemStack;

public class SpawnBlockItemInstance extends UniqueCustomItem.UniqueCustomItemInstance {

    String entityCode;
    String block;

    public SpawnBlockItemInstance(ItemStack itemStack) {
        super(itemStack);
    }

    public SpawnBlockItemInstance setSpawnerData(String code, String block) {
        this.entityCode = code;
        this.block = block;

        return this;
    }
}
