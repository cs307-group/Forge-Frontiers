package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class BazaarCommand implements CommandExecutor {

    private ForgeFrontier plugin;
    private BazaarManager bazaarManager;
    public BazaarCommand(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.bazaarManager = plugin.getBazaarManager();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to open the bazaar GUI for a non-player.");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            // OPEN BAZAAR GUI
            return true;
        }

        if (args[0].equals("set")) {
             if (args.length < 2) {
                 sender.sendMessage("" + ChatColor.RED + "Specify slot!");
                 return false;
             }
             int slot = 0;
             try {
                 slot = Integer.parseInt(args[1]);
             } catch (NumberFormatException e) {
                 sender.sendMessage("" + ChatColor.RED + "Slot must be a integer!");
                 return false;
             }
             ItemStack itm = p.getInventory().getItemInMainHand();
             if (itm.getType() == Material.AIR) {
                 if (bazaarManager.setItemSlot(null, slot)) {
                     sender.sendMessage("" + ChatColor.GREEN + "Successfully updated Bazaar Slot!");
                 } else {
                     sender.sendMessage("" + ChatColor.RED + "Failed to update slot.");
                 }
                 return true;
             } else {
                 if (bazaarManager.setItemSlot(itm, slot)) {
                     sender.sendMessage("" + ChatColor.GREEN + "Successfully updated Bazaar Slot!");
                 } else {
                     sender.sendMessage("" + ChatColor.RED + "Failed to update slot.");
                 }
                 return true;
             }
        }




        return false;
    }
}
