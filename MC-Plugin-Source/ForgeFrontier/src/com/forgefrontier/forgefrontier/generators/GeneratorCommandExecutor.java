package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeneratorCommandExecutor implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player p = (Player) commandSender;
        p.getInventory().addItem(CustomItemManager.asItemStack(CustomItemManager.getCustomItem("GenPlace-Diamond"), 1));
        commandSender.sendMessage(ForgeFrontier.CHAT_PREFIX + "PlaceGen item added to your inventory.");

        return true;
    }
}
