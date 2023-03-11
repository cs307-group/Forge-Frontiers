package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.inventory.ItemStack;

public class PlaceGeneratorItemInstance extends UniqueCustomItem.UniqueCustomItemInstance {

    String generatorId;
    int level;

    public PlaceGeneratorItemInstance(ItemStack itemStack) {
        super(itemStack);
    }

    public String getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(String generatorId) {
        this.generatorId = generatorId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PlaceGeneratorItemInstance setGeneratorData(String id, int level) {

        this.generatorId = id;
        this.level = level;

        return this;
    }

}
