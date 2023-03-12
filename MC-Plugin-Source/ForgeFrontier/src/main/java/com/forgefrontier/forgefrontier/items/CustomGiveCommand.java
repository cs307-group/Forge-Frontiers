package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;

@Command({"cgive", "customgive"})
@Description("Command for spawning custom items.")
public class CustomGiveCommand {
    private ForgeFrontier plugin;

    public CustomGiveCommand(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    /**
     * Usage: /cgive {Player} {Itemname}
     * @param sender Must be player
     * @param target Optional, user to get item
     * @param item name of custom item
     */
    @DefaultFor({"cgive", "customgive"})
    @AutoComplete("@cgive")
    public void cgiveCmd(CommandSender sender, @Optional Player target, String item) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to give items to a non-player");
            throw new SenderNotPlayerException();
        }
        if (target != null) {
            addItemToInv(item, target, sender);
            return;
        }

        Player player = (Player) sender;
        addItemToInv(item, player, sender);
    }


    /**
     * Attempts to add the itemType specified to the player's inventory
     *
     * @param itemType the class of the item to be added
     * @param player the player whose inventory the item is to be added
     * @param sender the sender of the command, used to send back error/success messages
     */
    private boolean addItemToInv(String itemType, Player player, CommandSender sender) {
        CustomItem citem = CustomItemManager.getCustomItem(itemType);
        if (citem == null) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.RED + "Failed to retrieve custom item.");
            return false;
        }


        ItemStack item = citem.asInstance(null).asItemStack();
        player.getInventory().addItem(item);
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + itemType + " item added to " +
                player.getDisplayName() + "'s inventory");
        return true;
    }





}
