package com.forgefrontier.forgefrontier.fishing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.FishDB;
import com.forgefrontier.forgefrontier.events.CustomCraftEvent;
import com.forgefrontier.forgefrontier.events.FishEvent;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.particles.gameparticles.FishingDropParticle;
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
    public ArrayList<Integer> chances;
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
            int rarity = givePlayerFishingDrop(p, event);
            new FishingDropParticle(rarity, p.getWorld(),
                    event.getHook().getLocation().add(0,1,0),0, 20).spawnParticle();
        }
    }


    public FishConfigUtil getConfigUtil() {
        return configUtil;
    }


    public FishingDrop rollFish(double rodmulti, double statmulti) {

        int rarity = calculateRarity(rodmulti,statmulti);
        if (rodmulti == 11) rarity = 0;
        else if (rodmulti == 12) rarity = 1;
        else if (rodmulti == 13) rarity = 2;
        else if (rodmulti == 14) rarity = 3;
        else if (rodmulti == 15) rarity = 4;
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
            accum += (chances.get(i));
            if (roll <= accum) {
                out = i;
                break;
            }
        }
        if (roll == 1000) {
            out = chances.size() - 1;
        }
        return out;
    }

    public FishModifier getRollModifier() {
        return rollModifier;
    }

    public int givePlayerFishingDrop(Player p, PlayerFishEvent e) {

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

        int enchantLevel = ItemUtil.getEnchantmentLevelInHand(p, Enchantment.LUCK);
        double rodMulti = rollModifier.getRodModifier(enchantLevel);
        if (enchantLevel == 11) rodMulti = 11;
        else if (enchantLevel == 12) rodMulti = 12;
        else if (enchantLevel == 13) rodMulti = 13;
        else if (enchantLevel == 14) rodMulti = 14;
        else if (enchantLevel == 15) rodMulti = 15;

        double levelMulti = rollModifier.getLevelModifier(pfs.getLevel());

        FishingDrop fd = rollFish(rodMulti, levelMulti);

        FishEvent event = new FishEvent(p, fd);
        Bukkit.getPluginManager().callEvent(event);

        if (fd.getItem().getType() == Material.YELLOW_STAINED_GLASS) {
            int amount = 100 * fd.rollNumber(rand);
            plugin.getEconomy().depositPlayer(p,100 * fd.rollNumber(rand));
            p.sendMessage("Fished up " + amount + "g!");
            return fd.getRarity();
        }
        pfs.incrementFishCaught(fd.getRarity());
        ItemGiver.giveItem(p, fd.roll(rand));
        p.sendMessage("Fished up " + rarities.get(fd.getRarity()) + " item!");
        return fd.getRarity();
    }







}
