package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.RankInventoryHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankCommandExecutor implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("forgefrontier.rank.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to use this command.");
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Opening the rank GUI.");
        }
        Player p = (Player) sender;
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Opening the rank GUI.");
        p.openInventory(new RankInventoryHolder(p.getUniqueId()).getInventory());
        return true;
    }
}
