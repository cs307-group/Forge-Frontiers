package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.RerollInventoryHolder;
import com.forgefrontier.forgefrontier.shop.GearShopInventoryHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RerollCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to open the reroll GUI for a non-player.");
            return true;
        }
        Player p = (Player) sender;

        p.sendMessage(ForgeFrontier.CHAT_PREFIX + "Opening the Reroll GUI.");
        p.openInventory(new RerollInventoryHolder().getInventory());

        return true;
    }
}
