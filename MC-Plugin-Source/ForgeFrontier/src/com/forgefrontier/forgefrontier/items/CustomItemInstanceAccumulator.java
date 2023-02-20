package com.forgefrontier.forgefrontier.items;

import jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public interface CustomItemInstanceAccumulator {

    CustomItemInstance accumulate(@Nullable CustomItemInstance instance, ItemStack itemStack) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

}
