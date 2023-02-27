package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.GeneratorShopInventoryHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GeneratorShopCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to open the generator shop for an executor that is not a player.");
            return true;
        }
        Player p = (Player) sender;
        p.sendMessage(ForgeFrontier.CHAT_PREFIX + "Opening the generator shop.");
        p.openInventory(new GeneratorShopInventoryHolder().getInventory());
        return true;
    }
}
