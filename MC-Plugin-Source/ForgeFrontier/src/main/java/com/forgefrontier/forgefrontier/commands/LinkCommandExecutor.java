package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.DBConnection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class LinkCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to link a non-player to the website.");
            return true;
        }

        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Generating a code to link your Minecraft Account with your ForgeFrontier Account");

        Player p = (Player) sender;

        DBConnection db = ForgeFrontier.getInstance().getDBConnection();

        db.getExistingPlayerLink(p.getUniqueId(), (Map<String, Object> result) -> {
            if(result != null) {
                if((boolean) result.get("bool_used")) {
                    sendMessage(sender, ForgeFrontier.CHAT_PREFIX + "Unable to link a new account. You have already linked your account.");
                    return;
                }
                sendMessage(sender, ForgeFrontier.CHAT_PREFIX + "Go to the website and enter the following code to link your account. Code: " + result.get("link_code"));
                return;
            }

            UUID linkCode = UUID.randomUUID();

            db.createPlayerLink(p.getUniqueId(), linkCode, (success) -> {
                if(success) {
                    sendMessage(sender, ForgeFrontier.CHAT_PREFIX + "Go to the website and enter the following code to link your account. Code: " + linkCode);
                } else {
                    sendMessage(sender, ForgeFrontier.CHAT_PREFIX + "Unable to create a link code for right now. Sorry. Please try again later.");
                }
            });

        });

        return true;
    }

    public void sendMessage(CommandSender sender, String message) {
        Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
            sender.sendMessage(message);
        });
    }

}
