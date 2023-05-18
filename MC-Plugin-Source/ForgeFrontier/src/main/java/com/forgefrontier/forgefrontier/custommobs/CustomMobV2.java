package com.forgefrontier.forgefrontier.custommobs;

import org.bukkit.inventory.ItemStack;

public abstract class CustomMobV2 implements CustomMobInterface {

    @Override
    public abstract void doTick();

    @Override
    public abstract void onDeath();

    @Override
    public abstract void onSpawn();

    @Override
    public abstract ItemStack[] rollDrops();



}
