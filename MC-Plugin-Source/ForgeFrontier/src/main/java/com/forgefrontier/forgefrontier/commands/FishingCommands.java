package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarManager;
import com.forgefrontier.forgefrontier.fishing.FishingManager;
import com.forgefrontier.forgefrontier.fishing.PlayerFishStat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.EquipmentSlot;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;

@Command({"fish"})
public class FishingCommands {
    private ForgeFrontier plugin;
    private FishingManager fishingManager;
    public FishingCommands(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.fishingManager = plugin.getFishingManager();
    }

    @DefaultFor({"fish"})
    public void fishCmd(CommandSender sender) {
        sender.sendMessage("" + ChatColor.AQUA + "Available Commands: \n" +
                ChatColor.AQUA + "/fish add {rarity} {min} {max}" +
                ChatColor.WHITE + " - Adds item to drop table");
    }

    @Subcommand({"add"})
    public void fishAdd(CommandSender sender, @Range(min=0,max=6)int rarity,
                        @Optional Integer min, @Optional Integer max) {
        if (min == null) min = 1;
        if (max == null) max = 1;
        if(!(sender instanceof Player)) {
            sender.sendMessage("Must be logged in to add fishing drops!");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;
        fishingManager.getConfigUtil().addFishingDrop(p.getInventory().getItem(EquipmentSlot.HAND), rarity, min, max);
        sender.sendMessage("Added item to drop table!");
    }

    @Subcommand({"reload"})
    public void fishReload(CommandSender sender) {
        fishingManager.clearLoad();
    }


    @Subcommand({"save"})
    public void fishSave(CommandSender sender) {
        fishingManager.fishDB.saveFishJob(fishingManager.playerFishStats);
        sender.sendMessage("Began save fish job");
    }

    @Subcommand({"stats"})
    public void fishStats(CommandSender sender) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to view stats for non-players.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;
        PlayerFishStat pfs = fishingManager.playerFishStats.get(p.getUniqueId());
        if (pfs == null) {
            pfs = new PlayerFishStat(p.getUniqueId(),0,0,true);
            fishingManager.fishDB.insertNewPlayer(pfs);
        }
        p.sendMessage("" + ChatColor.AQUA + "--- Fish Stats ---");
        p.sendMessage("" + ChatColor.AQUA + "Level: " + pfs.getLevel());
        p.sendMessage("" + ChatColor.AQUA + "Fish Caught: " + pfs.getFishCaught());
        p.sendMessage("" + ChatColor.AQUA + "----------------");
    }




}
