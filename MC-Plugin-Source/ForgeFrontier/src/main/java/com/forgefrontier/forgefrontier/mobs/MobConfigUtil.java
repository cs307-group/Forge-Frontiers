package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class MobConfigUtil {
    public static void getMaterialDropsFromCode(HashMap<Material, Integer> dropTable, ArrayList<Material> dropKeys, String code) {
        FileConfiguration configuration = ForgeFrontier.getInstance().getConfig("entity-drops");
        ForgeFrontier.getInstance().getLogger().log(Level.WARNING, code + "| " + configuration.getKeys(false));
        ConfigurationSection codeSection = configuration.getConfigurationSection(code);
        if(codeSection == null)
            return;
        ConfigurationSection matSection;
        if ((matSection = codeSection.getConfigurationSection("materials.")) != null) {
            int itemInd = 0;
            ConfigurationSection isec;
            while ((isec = matSection.getConfigurationSection("" + itemInd)) != null) {
                int chance = isec.getInt("chance");
                Material material = Material.getMaterial(isec.getString("code"));
                ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "Chance: " + chance + " | Material: " + material.toString());
                dropTable.put(material, chance);
                dropKeys.add(material);
                itemInd++;
            }
        }
    }

    public static void getItemDropsFromCode(HashMap<String, Integer> dropTable, ArrayList<String> dropKeys, String code) {
        FileConfiguration configuration = ForgeFrontier.getInstance().getConfig("entity-drops");
        ConfigurationSection codeSection = configuration.getConfigurationSection(code);
        if(codeSection == null)
            return;
        ConfigurationSection matSection;
        if ((matSection = codeSection.getConfigurationSection("items.")) != null) {
            int itemInd = 0;
            ConfigurationSection isec;
            while ((isec = matSection.getConfigurationSection("" + itemInd)) != null) {
                int chance = isec.getInt("chance");
                String item = isec.getString("code");
                ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "Chance: " + chance + " | Item: " + item);
                dropTable.put(item, chance);
                dropKeys.add(item);
                itemInd++;
            }
        }
    }
}
