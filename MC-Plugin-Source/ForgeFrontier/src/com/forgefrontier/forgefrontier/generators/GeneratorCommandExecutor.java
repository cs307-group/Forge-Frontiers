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

        Player p = (Player) commandSender;
        if(strings.length > 2) {
            ItemStack item = CustomItemManager.getCustomItem("ZombieSword").asInstance(null).asItemStack();
            p.getInventory().addItem(item);
            commandSender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Zombie sword item added to your inventory.");
            return true;
        }
        ItemStack item = CustomItemManager.getCustomItem("GenPlace-Diamond").asInstance(null).asItemStack();
        p.getInventory().addItem(item);
        commandSender.sendMessage(ForgeFrontier.CHAT_PREFIX + "PlaceGen item added to your inventory.");

        return true;
    }
}
