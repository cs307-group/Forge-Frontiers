package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarManager;
import com.forgefrontier.forgefrontier.fishing.FishingManager;
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
            sender.sendMessage("Unable to create buy listings for a non-player.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;
        fishingManager.addFishingDrop(p.getInventory().getItem(EquipmentSlot.HAND), rarity, min, max);
        sender.sendMessage("Added item to drop table!");
    }

    @Subcommand({"reload"})
    public void fishReload(CommandSender sender) {
        fishingManager.clearLoad();
    }







}
