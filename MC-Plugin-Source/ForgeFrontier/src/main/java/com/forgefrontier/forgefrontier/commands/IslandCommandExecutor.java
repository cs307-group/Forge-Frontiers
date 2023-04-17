package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.gui.CreateIslandInventoryHolder;
import com.forgefrontier.forgefrontier.gui.MainIslandInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
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

        if(args.length == 0) {
            Island island = BentoBox.getInstance().getIslands().getIsland(Bukkit.getWorld("bskyblock_world"), p.getUniqueId());

            if(island == null) {
                p.openInventory(new CreateIslandInventoryHolder().getInventory());
            } else {
                p.openInventory(new MainIslandInventoryHolder().getInventory());
            }
        } else if(args.length == 1) {
            if(args[0].equals("home")) {
                p.performCommand("bsb home");
            } else {
                p.sendMessage("Unknown subcommand: " + args[0]);
            }
        }

        return true;
    }
}
