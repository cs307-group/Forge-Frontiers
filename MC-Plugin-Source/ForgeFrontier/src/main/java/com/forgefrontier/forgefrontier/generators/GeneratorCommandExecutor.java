package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GeneratorCommandExecutor implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        commandSender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Generator command is not current functional.");

        ForgeFrontier.getInstance().getLogger().info(ForgeFrontier.getInstance().getGeneratorManager().generatorInstanceTree.toString());

        return true;
    }
}
