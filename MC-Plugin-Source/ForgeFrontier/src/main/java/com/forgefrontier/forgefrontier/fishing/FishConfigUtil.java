package com.forgefrontier.forgefrontier.fishing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FishConfigUtil {
    FishingManager fm;
    ForgeFrontier plugin;
    public FishConfigUtil(FishingManager fm) {
        this.fm = fm;
        plugin = ForgeFrontier.getInstance();
    }

    public void loadRarities() {
        FileConfiguration config = plugin.getConfig("fishing");
        List<String> r = config.getStringList("fishing-rarities");
        fm.rarities = new ArrayList<>(r);
        List<Integer> chance = config.getIntegerList("base-rate");
        fm.chances = new ArrayList<>(chance);
    }

    public void loadFishingDrops() {
        FileConfiguration config = plugin.getConfig("fishing");
        ConfigurationSection csec;
        int rarityInd = 0;
        while ((csec = config.getConfigurationSection("items." + rarityInd)) != null) {
            int itemInd = 0;
            ConfigurationSection isec;
            while ((isec = csec.getConfigurationSection("" + itemInd)) != null) {
                int rarity = isec.getInt("rarity");
                int min = isec.getInt("min");
                int max = isec.getInt("max");
                String material = isec.getString("material");
                String customData = isec.getString("custom_data");
                String name = isec.getString("name");
                //plugin.getLogger().log(Level.INFO,"[" + rarity + "] - " +
                //        "(" + min + ", " + max +")" + " - " + material);
                FishingDrop drop = new FishingDrop(material, "", name, rarity, min, max);
                fm.drops.computeIfAbsent(rarity, k -> new ArrayList<>());
                fm.drops.get(rarity).add(drop);
                itemInd++;
            }
            rarityInd++;
        }
    }

    public void addFishingDrop(ItemStack item, int rarity, int min, int max) {
        int count = 0;
        ConfigurationSection csec;

        while ((csec = this.plugin.getConfig("fishing")
                .getConfigurationSection("items." + rarity + "." + count)) != null) {
            count++;
        }
        String path = "items." + rarity + "." + count + ".";
        this.plugin.getConfig("fishing").set("items." + rarity + "." + count, null);
        this.plugin.getConfig("fishing").set(path + "rarity", rarity);
        this.plugin.getConfig("fishing").set(path + "min", min);
        this.plugin.getConfig("fishing").set(path + "max", max);
        this.plugin.getConfig("fishing").set(path + "material", item.getType().toString());
        this.plugin.getConfig("fishing").set(path + "custom_data", "");
        String displayName = null;
        ItemMeta im = item.getItemMeta();
        if (im != null)
            displayName = im.getDisplayName();
        this.plugin.getConfig("fishing").set(path + "name", displayName);

        plugin.getLogger().log(Level.INFO,"Updated fish config: " + path);
        try {
            this.plugin.getConfig("fishing").save(new File(plugin.getDataFolder(), "fishing.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE,"Failed updating fish config: " + path);
        }
    }



    public void loadFishChanceDrop() {
        List<Double> rodChance = this.plugin.getConfig("fishing").getDoubleList("rod-rate.sealuck");
        List<Double> statChance = this.plugin.getConfig("fishing").getDoubleList("stat-rate.fishlevel");
        fm.rollModifier = new FishModifier(new ArrayList<>(rodChance), new ArrayList<>(statChance));
    }



}
