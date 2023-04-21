package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;

@Command({"cgive", "customgive"})
@Description("Command for spawning custom items.")
public class CustomGiveCommand {
    private final ForgeFrontier plugin;

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
    public void cgiveCmd(CommandSender sender, String item, @Optional Player target, @Optional String itemJson) {
        if(!sender.hasPermission("forgefrontier.cmd.customgive")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to use this command.");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to give items to a non-player");
            throw new SenderNotPlayerException();
        }
        if (target != null) {
            addItemToInv(item, target, itemJson, sender);
            return;
        }

        Player player = (Player) sender;
        addItemToInv(item, player, itemJson, sender);
    }

    @Command({"skullgive"})
    public void skullGive(CommandSender sender, String item) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to give items to a non-player");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;
        ItemStack skullItm = ForgeFrontier.getInstance().getCustomSkullManager().getSkullItem(item);
        if (skullItm != null) {
            ItemGiver.giveItem(p,skullItm);
        }
    }


    /**
     * Attempts to add the itemType specified to the player's inventory
     *
     * @param itemType the class of the item to be added
     * @param player the player whose inventory the item is to be added
     * @param sender the sender of the command, used to send back error/success messages
     */
    private boolean addItemToInv(String itemType, Player player, String itemJson, CommandSender sender) {
        if(itemJson == null || itemJson.isEmpty()) {
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
        JSONWrapper wrapper = new JSONWrapper(itemJson);
        if(wrapper == null) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "An error occurred in parsing the JSON.");
            return true;
        }
        wrapper.setString("base-code", itemType);
        CustomItemInstance cii = CustomItemManager.getInstanceFromData(wrapper.toJSONString());
        player.getInventory().addItem(cii.asItemStack());

        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + itemType + " item added to " +
                player.getDisplayName() + "'s inventory with the specified data.");
        return true;
    }
}
