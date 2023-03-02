package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.gui.CreateIslandInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
//import world.bentobox.bentobox.BentoBox;
//import world.bentobox.bentobox.database.objects.Island;

public class IslandCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to open the island GUI for a non-player.");
            return true;
        }
        Player p = (Player) sender;
        //Island island = BentoBox.getInstance().getIslands().getIsland(Bukkit.getWorld("bskyblock_world"), p.getUniqueId());

        //if(island == null) {
            p.openInventory(new CreateIslandInventoryHolder().getInventory());
        //}

        return true;
    }
}
