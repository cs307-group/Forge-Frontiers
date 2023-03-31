package com.forgefrontier.forgefrontier.fishing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;


public class FishingManager extends Manager implements Listener {
    public FishingManager(ForgeFrontier plugin) {
        super(plugin);
    }
    public ArrayList<String> rarities;
    public ArrayList<Double> chances;
    public HashMap<Integer, ArrayList<FishingDrop>> drops;
    Random rand;
    @Override
    public void init() {
        rand = new Random();
        drops = new HashMap<>();
        loadRarities();
        loadFishingDrops();



    }

    public void clearLoad() {
        drops = new HashMap<>();
        rarities = new ArrayList<>();
        chances = new ArrayList<>();
        loadRarities();
        loadFishingDrops();
    }

    @Override
    public void disable() {
        try {
            this.plugin.getConfig("fishing").save(new File(plugin.getDataFolder(), "fishing.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE,"Failed saving fishing config");
        }
    }
    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        PlayerFishEvent.State s = event.getState();
        if (s == PlayerFishEvent.State.CAUGHT_FISH) {
            Entity caught = event.getCaught();
            if (caught != null)
                caught.remove();
            Player p = event.getPlayer();
            int rodMulti = 1;
            int statMulti = 1;
            // TODO: Use config value instead
            rodMulti += ((double) ItemUtil.getEnchantmentLevelInHand(p, Enchantment.LUCK) / 4.0);
            FishingDrop fd = rollFish(rodMulti, statMulti);
            ItemGiver.dropItem(p, fd.roll(rand));
            p.sendMessage("Caught" + rarities.get(fd.getRarity()) + " fish!");
        }




    }



    public FishingDrop rollFish(int rodmulti, int statmulti) {
        int rarity = calculateRarity(rodmulti,statmulti);
        ArrayList<FishingDrop> poss = drops.get(rarity);
        return poss.get(Math.abs(rand.nextInt()) % poss.size());
    }

    public void loadRarities() {
        FileConfiguration config = plugin.getConfig("fishing");
        List<String> r = (List<String>) config.getStringList("fishing-rarities");
        rarities = new ArrayList<>(r);
        rarities.forEach((ra) -> {
            plugin.getLogger().log(Level.INFO,"Loaded Rarity: " + ra);
        });
        List<Double> chance = config.getDoubleList("base-rate");
        chances = new ArrayList<>(chance);
    }

    public void loadFishingDrops() {
        plugin.getLogger().log(Level.INFO,"LOAD FISH DROPS");
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
                plugin.getLogger().log(Level.INFO,"[" + rarity + "] - " +
                        "(" + min + ", " + max +")" + " - " + material);
                FishingDrop drop = new FishingDrop(material, "", name, rarity, min, max);
                drops.computeIfAbsent(rarity, k -> new ArrayList<>());
                drops.get(rarity).add(drop);
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

    public int calculateRarity(double rod_rate, double skill_rate) {
        rod_rate = 1;
        skill_rate = 1;
        int roll = rand.nextInt() % 1000;
        roll *= rod_rate * skill_rate;
        roll = Math.min(roll, 1000);
        int out = 0;
        for (int i = 0; i < chances.size(); i++) {
            if (roll <= (chances.get(i) * 1000.0)) {
                out = i;
            }
        }
        return out;
    }








}
