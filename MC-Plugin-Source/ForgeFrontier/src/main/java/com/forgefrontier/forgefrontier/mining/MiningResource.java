package com.forgefrontier.forgefrontier.mining;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class MiningResource {

    CustomItem item;
    Material blockMaterial;
    int time;

    int lastTime;

    //List<DropChance> dropChanceList;

    public MiningResource(String itemCode, String blockCode, int time) {
        this.item = CustomItemManager.getCustomItem(itemCode);
        if(this.item == null)
            throw new RuntimeException("Unable to find custom item: " + itemCode);
        this.blockMaterial = Material.matchMaterial(blockCode);
        if(this.blockMaterial == null)
            throw new RuntimeException("Unable to find block material: " + blockCode);
        this.time = time;
    }

    public MiningResource(ConfigurationSection config) {
        this.item = CustomItemManager.getCustomItem(config.getString("item_id"));
        this.blockMaterial = Material.matchMaterial(config.getString("block"));
        this.time = config.getInt("time");
    }

    public CustomItem getItem() {
        return item;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public int getTime() {
        return time;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }

    public void save(ConfigurationSection config) {
        config.set("item_id", item.getCode());
        config.set("block", blockMaterial.toString());
        config.set("time", time);
    }

}
