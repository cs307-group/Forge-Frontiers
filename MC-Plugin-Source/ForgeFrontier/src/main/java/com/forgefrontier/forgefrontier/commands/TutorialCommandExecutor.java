package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.tutorial.TutorialInventoryHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TutorialCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Only a player can use this command.");
            return true;
        }
        Player p = (Player) sender;
        p.openInventory(new TutorialInventoryHolder(p).getInventory());
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Opening the Tutorial GUI.");
        return true;
    }
}
