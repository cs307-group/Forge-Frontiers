package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;

import java.util.ArrayList;


@Command({"bazaar"})
public class BazaarCommand {

    private ForgeFrontier plugin;
    private BazaarManager bazaarManager;
    public BazaarCommand(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.bazaarManager = plugin.getBazaarManager();
    }


    @DefaultFor({"bazaar"})
    public void bazaarCmd(CommandSender sender) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to open the bazaar GUI for a non-player.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;

        // OPEN GUI
        p.openInventory(new BazaarGUI().getInventory());


    }

    /**
     * [ADMIN ONLY] Clear out a bazaar slot
     * Usage: /bazaar set {slot #} -- ITEM IN HAND
     * AIR = Remove
     * Anything else = Add
     * To update, remove then add.
     */
    @Subcommand({"set"})
    public void bazaarSet(CommandSender sender, Integer slot) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to open the bazaar GUI for a non-player.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;

        ItemStack itm = p.getInventory().getItemInMainHand();
        if (itm.getType() == Material.AIR) {
            p.openInventory(new ConfirmationHolder("Confirm Removal?", null, () -> {
                if (bazaarManager.setItemSlot(null, slot)) {
                    sender.sendMessage("" + ChatColor.GREEN + "Successfully cleared Bazaar Slot!");
                } else {
                    sender.sendMessage("" + ChatColor.RED + "Failed to cleared slot.");
                }
            }).getInventory());

        } else {
            p.openInventory(new ConfirmationHolder("Confirm Set Bazaar Item?", null, itm, () -> {
                if (bazaarManager.setItemSlot(itm, slot)) {
                    sender.sendMessage("" + ChatColor.GREEN + "Successfully updated Bazaar Slot!");
                } else {
                    sender.sendMessage("" + ChatColor.RED + "Failed to update slot.");
                }
            }).getInventory());
        }
    }

    @Subcommand({"list buy"})
    public void testBuy(CommandSender sender, @Range(min=0, max=1e9) Double price) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to create buy listings for a non-player.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;


    }

    @Subcommand({"list sell"})
    public void testSell(CommandSender sender, @Range(min=1, max=1728) int amount, @Range(min=0, max=1e9) Double price) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to create sell listings for a non-player.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;

        p.sendMessage("boop");
        ItemStack itm = p.getInventory().getItemInMainHand();
        ArrayList<ItemStack> displayItems = bazaarManager.getLookupItems();
        int idx = 0;
        for (idx = 0; idx < displayItems.size(); idx++) {
            if (ItemUtil.customCompare(itm,displayItems.get(idx)))
                break;
        }
        if (idx == displayItems.size()) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Item is not listable on the bazaar.");
            return;
        }
        if (ItemUtil.hasItem(p, displayItems.get(idx),amount)) {
            BazaarEntry entry = new BazaarEntry(false, idx, amount, price, p.getUniqueId());
            if (plugin.getDBConnection().bazaarDB.insertListing(entry)) {
                ItemUtil.take(p,displayItems.get(idx),amount);
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.GOLD + "Successfully created listing!");
            } else {
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.RED +
                        "Unexpected error while creating listing..");
            }
        } else {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough items to list.");
        }

    }



}
