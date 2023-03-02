package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * ItemCommandExecutor
 *
 * Executes the commands from in-game which spawn custom items into a player's inventory
 */
public class InspectCommandExecutor implements CommandExecutor {

    PlayerManager playerManager;

    public InspectCommandExecutor(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {

                FFPlayer ffPlayer = playerManager.getFFPlayerFromID(player.getUniqueId());
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + ffPlayer.getStatsString());

            } else if (args.length == 1) {
                if (playerManager.hasPlayerWithName(args[0])) {
                    Player otherPlayer = playerManager.getPlayerByName(args[0]);
                    FFPlayer ffPlayer = playerManager.getFFPlayerFromID(otherPlayer.getUniqueId());
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + ffPlayer.getStatsString());
                } else {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid usage: Player Name does not exist or player is not online");
                }
            }
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid usage: /inspect <player-name>");
            return true;
        }
        return true;
    }
}
