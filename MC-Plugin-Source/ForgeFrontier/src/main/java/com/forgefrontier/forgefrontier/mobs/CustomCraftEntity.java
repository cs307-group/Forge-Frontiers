package com.forgefrontier.forgefrontier.mobs;

import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public interface CustomCraftEntity {
    String getCode();
    void setDefaultHealth(int maxHealth);
    void updateNamePlate(String name);
    BossBar initBossBar(String name);
    void defineDropTable();
    void initDropTable();
    void registerDropItem(String item, int chance);
    void registerDropItem(Material item, int chance);
    void saveMetaData();
}
