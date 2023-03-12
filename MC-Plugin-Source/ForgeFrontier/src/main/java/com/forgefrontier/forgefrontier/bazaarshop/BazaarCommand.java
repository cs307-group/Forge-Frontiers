package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;


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
    public void testSell(CommandSender sender, @Range(min=0, max=1e9) Double price) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Unable to create sell listings for a non-player.");
            throw new SenderNotPlayerException();
        }
        Player p = (Player) sender;

        p.sendMessage("boop");

    }



}
