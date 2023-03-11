package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * ItemCommandExecutor
 *
 * Executes the commands from in-game which spawn custom items into a player's inventory
 */
public class ItemCommandExecutor implements CommandExecutor {

    /**
     * Constructor which initializes the values of the hashmap
     */
    public ItemCommandExecutor() {

    }

    /**
     * Handles the command sent via in-game or the terminal
     *
     * @param sender represents the sender of the command
     * @param command represents the command sent
     * @param label the command label that the sender typed
     * @param args the different arguments included after the command label
     * @return a boolean which indicates whether the command was successful or not?
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String itemType;

        if (args.length == 0) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid usage: /customgive <item type> or /customgive <player name> <item type>" +
                    " or /customgive <player name> <item type> <number of items>");
            return true;
        }

        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                itemType = args[0];

                if (itemType == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid item type: " + args[0]);
                    return true;
                }

                return addItemToInv(args[0], player, sender);
            } else if (args.length == 2) {
                //TODO: /customgive <player-name> <item-name> || /customgive <item-name> <num-items>
            } else if (args.length == 3) {
                //TODO: /customgive <player-name> <item-name> <num-items>
            }
        }
        return true;
    }

    /**
     * Attempts to add the itemType specified to the player's inventory
     *
     * @param itemType the class of the item to be added
     * @param player the player whose inventory the item is to be added
     * @param sender the sender of the command, used to send back error/success messages
     */
    private boolean addItemToInv(String itemType, Player player, CommandSender sender) {
        ItemStack item = CustomItemManager.getCustomItem(itemType).asInstance(null).asItemStack();
        player.getInventory().addItem(item);
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + itemType + " item added to " +
                player.getDisplayName() + "'s inventory");
        return true;
    }
}
