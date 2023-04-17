package com.forgefrontier.forgefrontier.mobs;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles commands used to spawn in custom mobs manually
 */
public class EntityCommandExecutor implements CommandExecutor {

    /**
     * Mob command interface
     *
     * @param sender represents the initiator of the command
     * @param command info about the command
     * @param label the label of the command
     * @param args the command arguments
     * @return if the command was executed successfully
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to initiate this command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Invalid Usage: /customspawn <custom-mob-name>");
        } else {
            spawnMob(args[0], player, sender);
        }
        return true;
    }

    /** executes the spawning of the mob specified by the command sender) */
    private void spawnMob(String mobName, Player player, CommandSender sender) {
        if (ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity(mobName, player)) {
            sender.sendMessage("Successfully spawned the " + mobName);
        } else {
            sender.sendMessage("Failed to spawn entity - likely " + mobName + " does not exist");
        }
    }
}
