package com.forgefrontier.forgefrontier.shop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopCommandExecutor implements CommandExecutor {
    private ShopHolder sh;
    public ShopCommandExecutor(ShopHolder sh) {
        this.sh = sh;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player p = (Player) commandSender;
        p.sendMessage("Command Works.");
        p.openInventory(sh.getInventory());
        return true;
    }
}
