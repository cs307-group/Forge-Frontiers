package com.forgefrontier.forgefrontier.mining;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.events.FishEvent;
import com.forgefrontier.forgefrontier.events.MiningAreaMineEvent;
import com.forgefrontier.forgefrontier.stashes.Stash;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class MiningManager extends Manager implements Listener {

    String miningWorld;
    HashMap<String, MiningArea> miningAreas;

    public MiningManager(ForgeFrontier plugin) {
        super(plugin);
        this.miningAreas = new HashMap<>();
    }

    @Override
    public void init() {
        plugin.getCustomItemManager().registerCustomItem(new MiningWandItem());

        String world = plugin.getConfig("mining").getString("world");
        this.miningWorld = world;

        int areaInd = 0;
        ConfigurationSection configSection;
        while((configSection = this.plugin.getConfig("mining").getConfigurationSection("mining_areas." + areaInd)) != null) {
            MiningArea miningArea = new MiningArea(configSection);
            this.miningAreas.put(miningArea.getAreaName(), miningArea);
            areaInd += 1;
        }

        MiningRunnable mainMiningRunnable = new MiningRunnable();
        mainMiningRunnable.runTaskTimer(this.plugin, 0, 20);

    }

    @Override
    public void disable() {
        FileConfiguration config = this.plugin.getConfig("mining");
        config.set("mining_areas", null);
        int areaInd = 0;
        for(String name: this.miningAreas.keySet()) {
            MiningArea area = this.miningAreas.get(name);
            area.save(config.createSection("mining_areas." + areaInd));
            areaInd += 1;
        }
        try {
            config.save(new File(plugin.getDataFolder(), "mining.yml"));
        } catch (IOException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to save mining configuration.");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(e.isCancelled())
            return;
        if(!e.getBlock().getLocation().getWorld().getName().equals(this.miningWorld)) {
            return;
        }
        for(MiningArea area: this.miningAreas.values()) {
            if(area.contains(e.getBlock().getLocation().toVector())) {
                for(MiningResource resource: area.getResources()) {
                    if(resource.getBlockMaterial() == e.getBlock().getType()) {
                        e.getBlock().getWorld().dropItem(
                            e.getBlock().getLocation().add(0.5, 0.5, 0.5),
                            resource.getItem().asInstance(null).asItemStack()
                        );
                        e.setCancelled(true);
                        e.setDropItems(false);
                        e.getBlock().setType(area.getReplacementMaterial());

                        MiningAreaMineEvent event = new MiningAreaMineEvent(e.getPlayer(), area, resource.getItem());
                        Bukkit.getPluginManager().callEvent(event);
                        return;
                    }
                }
            }
        }
    }

    public String getMiningWorld() {
        return this.miningWorld;
    }

    public void registerNewMiningArea(MiningArea miningArea) {
        this.miningAreas.put(miningArea.getAreaName(), miningArea);
    }

    public MiningArea getMiningArea(String areaName) {
        return this.miningAreas.get(areaName);
    }

    public void removeMiningArea(String areaName) {
        this.miningAreas.remove(areaName);
    }

    public class MiningRunnable extends BukkitRunnable {

        Random random = new Random();
        int time;

        @Override
        public void run() {
            this.time++;
            for(MiningArea area: miningAreas.values()) {
                for(MiningResource resource: area.getResources()) {
                    int neededTime = resource.getLastTime() + resource.getTime();
                    if(neededTime <= time) {
                        this.placeResourceInArea(area, resource);
                        resource.setLastTime(time);
                    }
                }
            }
        }

        private void placeResourceInArea(MiningArea area, MiningResource resource) {
            int tries = 0;
            while(tries < 100) {
                tries++;
                int dx = random.nextInt(area.getPos2().getBlockX() - area.getPos1().getBlockX() + 1);
                int dy = random.nextInt(area.getPos2().getBlockY() - area.getPos1().getBlockY() + 1);
                int dz = random.nextInt(area.getPos2().getBlockZ() - area.getPos1().getBlockZ() + 1);
                int x = dx + area.getPos1().getBlockX();
                int y = dy + area.getPos1().getBlockY();
                int z = dz + area.getPos1().getBlockZ();
                Block block = Bukkit.getWorld(miningWorld).getBlockAt(x, y, z);
                if (block.getType() == area.replacementMaterial || block.getType() == resource.getBlockMaterial()) {
                    block.setType(resource.getBlockMaterial());
                    return;
                }
            }
        }
    }

}
