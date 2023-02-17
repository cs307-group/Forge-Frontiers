package com.forgefrontier.forgefrontier.items;

import jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;

public interface CustomItemInstanceAccumulator {

    CustomItemInstance accumulate(@Nullable CustomItemInstance instance, ItemStack itemStack);

}
