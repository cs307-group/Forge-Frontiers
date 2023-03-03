package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ForgeFrontierCommandExecutor implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("forgefrontier.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to access this command.");
            return true;
        }
        if(args.length == 1) {
            if(args[0].equals("reload")) {
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Attempting to reload the config.");
                ForgeFrontier.getInstance().reloadConfig();
                ForgeFrontier.getInstance().onDisable();
                ForgeFrontier.getInstance().onEnable();
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully reloaded the config.");
            }
        }
        return true;
    }
}
