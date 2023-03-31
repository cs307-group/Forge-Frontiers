package com.forgefrontier.forgefrontier.fishing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.FishDB;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import com.forgefrontier.forgefrontier.utils.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;


public class FishingManager extends Manager implements Listener {
    public FishingManager(ForgeFrontier plugin) {
        super(plugin);
    }
    public ArrayList<String> rarities;
    public ArrayList<Double> chances;
    public HashMap<Integer, ArrayList<FishingDrop>> drops;
    public FishConfigUtil configUtil;
    public FishModifier rollModifier;
    public HashMap<UUID, PlayerFishStat> playerFishStats;
    public FishDB fishDB;
    public long lastSave;
    Random rand;
    @Override
    public void init() {
        rand = new Random();
        clearLoad();
    }

    public void clearLoad() {
        drops = new HashMap<>();
        rarities = new ArrayList<>();
        chances = new ArrayList<>();
        configUtil = new FishConfigUtil(this);
        configUtil.loadRarities();
        configUtil.loadFishingDrops();
        configUtil.loadFishChanceDrop();
        fishDB = plugin.getDatabaseManager().getFishDB();
        playerFishStats = fishDB.loadFishingStats();
    }

    @Override
    public void disable() {
        fishDB.saveFishJob(playerFishStats);
        try {
            this.plugin.getConfig("fishing").save(new File(plugin.getDataFolder(), "fishing.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE,"Failed saving fishing config");
        }
        int wait = 0;
        while (fishDB.stillUpdating() && wait < 40) {
            try {
                Thread.sleep(100);
                wait++;
            } catch (Exception e) {
                break;
            }
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
            givePlayerFishingDrop(p, event);
        }
    }


    public FishConfigUtil getConfigUtil() {
        return configUtil;
    }


    public FishingDrop rollFish(double rodmulti, double statmulti) {
        int rarity = calculateRarity(rodmulti,statmulti);
        ArrayList<FishingDrop> poss = drops.get(rarity);
        return poss.get(Math.abs(rand.nextInt()) % poss.size());
    }

    public int calculateRarity(double rod_rate, double skill_rate) {
        int roll = Math.abs(rand.nextInt()) % 1000;
        roll *= rod_rate * skill_rate;
        roll = Math.min(roll, 1000);
        int out = 0;
        int accum = 0;
        for (int i = 0; i < chances.size(); i++) {
            accum += (chances.get(i) * 1000.0);
            if (roll <= accum) {
                out = i;
                break;
            }
        }
        plugin.getLogger().log(Level.INFO,"ROLL: " + roll + " GOT: " + out);
        return out;
    }

    public FishModifier getRollModifier() {
        return rollModifier;
    }

    public void givePlayerFishingDrop(Player p, PlayerFishEvent e) {

        // Update stats
        UUID pid = p.getUniqueId();
        boolean first = false;
        if (!playerFishStats.containsKey(pid)) {
            first = true;
            PlayerFishStat pfs = new PlayerFishStat(pid,1,1, true );
            fishDB.insertNewPlayer(pfs);
            playerFishStats.put(pid, pfs);
        }

        PlayerFishStat pfs = playerFishStats.get(pid);

        pfs.setModified(true);

        // Calculate roll

        double rodMulti = rollModifier.getRodModifier(ItemUtil.getEnchantmentLevelInHand(p, Enchantment.LUCK));
        double levelMulti = rollModifier.getLevelModifier(pfs.getLevel());

        FishingDrop fd = rollFish(rodMulti, levelMulti);
        if (fd.getItem().getType() == Material.YELLOW_STAINED_GLASS) {
            int amount = 100 * fd.rollNumber(rand);
            plugin.getEconomy().depositPlayer(p,100 * fd.rollNumber(rand));
            p.sendMessage("Fished up " + amount + "g!");
            return;
        }
        pfs.incrementFishCaught(fd.getRarity());
        ItemGiver.giveItem(p, fd.roll(rand));
        p.sendMessage("Fished up " + rarities.get(fd.getRarity()) + " item!");
    }







}
