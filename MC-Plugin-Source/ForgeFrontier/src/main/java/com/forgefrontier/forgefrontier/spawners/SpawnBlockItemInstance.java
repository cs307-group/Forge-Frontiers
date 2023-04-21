package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.inventory.ItemStack;

public class SpawnBlockItemInstance extends UniqueCustomItem.UniqueCustomItemInstance {
    String spawnerID;
    String entityCode;

    public SpawnBlockItemInstance(ItemStack itemStack) {
        super(itemStack);
    }

    public String getSpawnerID() {
        return spawnerID;
    }

    public void setSpawnerID(String id) {
        this.spawnerID = id;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String code) {
        this.entityCode = code;
    }

    public SpawnBlockItemInstance setSpawnerData(String id, String code) {
        this.entityCode = code;
        this.spawnerID = id;

        return this;
    }
}
