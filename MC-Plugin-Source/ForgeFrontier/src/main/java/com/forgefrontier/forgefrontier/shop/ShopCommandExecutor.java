package com.forgefrontier.forgefrontier.shop;

import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class ShopCommandExecutor implements CommandExecutor {
    private Shop shop;
    private static String helpMessage = "Valid Shop Commands: help, view";
    private static String invalidPrice = "Valid Shop Commands: help, view";

    public ShopCommandExecutor(Shop shop) {
        this.shop = shop;
    }

    /**
     *
     * Shop command interface
     *
     * @param commandSender Sender
     * @param command CommandInfo
     * @param s cmdstring
     * @param args args
     * @return True/False dependent on success
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player p = (Player) commandSender;

        if (args.length == 0) {
            p.openInventory(shop.getGUI());
            return true;
        }
        switch (args[0]) {
            case "add": {
                ItemStack itm = p.getInventory().getItemInMainHand();
                if (itm.getType().equals(Material.AIR)) {
                    p.sendMessage("Can't sell nothing!");
                    return true;
                }
                double price = 10;
                int amt = itm.getAmount();
                if (args.length >= 2) {
                    try {
                        price = Double.parseDouble(args[1]);
                    } catch (NumberFormatException nfe){p.sendMessage("Enter valid price!");}
                }
                if (args.length >= 3) {
                    try {
                        amt = Integer.parseInt(args[2]);
                        if (itm.getAmount() < amt) {
                            p.sendMessage("You do not have enough of this item in your hand!");
                            return true;
                        }
                    } catch (NumberFormatException nfe){p.sendMessage("Enter Valid Amount!");}
                }

                final int finalAmt = amt;
                final double finalPrice = price;


                ItemStack viewItem = ShopListing.shopifyItem(new ItemStackBuilder(itm).copy(itm, finalAmt), price);
                p.openInventory(new ConfirmationHolder("Confirm?",null, viewItem, ()->{
                    shop.addItem(p,itm, finalAmt, finalPrice);
                }).getInventory());
                break;
            }
            case "view": {
                p.openInventory(shop.getGUI());
                return true;
            }
            case "remove": {
                p.openInventory(new ShopHolder(shop,p.getUniqueId(), true).getInventory());
                return true;
            }
            default: {
                p.sendMessage(helpMessage);
            }
        }
        return true;
    }



}