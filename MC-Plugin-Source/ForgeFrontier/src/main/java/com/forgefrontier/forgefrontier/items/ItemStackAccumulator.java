package com.forgefrontier.forgefrontier.items;

import jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;

public interface ItemStackAccumulator {

    ItemStack accumulate(CustomItemInstance instance, @Nullable ItemStack itemStack);

}
