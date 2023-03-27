package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.inventory.ItemStack;

public class PlaceStashItemInstance extends UniqueCustomItem.UniqueCustomItemInstance {

    String stashId;

    public PlaceStashItemInstance(ItemStack itemStack) {
        super(itemStack);

    }

    public void setStashId(String stashId) {
        this.stashId = stashId;
    }
}
