package com.forgefrontier.forgefrontier.custommobs;

import org.bukkit.inventory.ItemStack;

public interface CustomMobInterface {

    void doTick();

    /**
     * Executed on death
     * Call rollDrops in here
     */

    void onDeath();

    void onSpawn();

    /**
     *
     */
    ItemStack[] rollDrops();


}
