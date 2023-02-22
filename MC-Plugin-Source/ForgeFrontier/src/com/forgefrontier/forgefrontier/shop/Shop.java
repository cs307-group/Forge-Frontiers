package com.forgefrontier.forgefrontier.shop;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Implements functionality to buy/sell items through a GUI interface
 */
public class Shop {
    private static final String INSUFF_BALANCE = ChatColor.DARK_RED + "You don't have enough money to buy this item!";
    private static final String GEN_SHOP_ERR = ChatColor.RED + "An error occurred with the shop. Please try again.";

    private Hashtable<UUID, ShopListing> listings;
    ShopHolder shopGUI;
    ShopCommandExecutor shopCommands;
    Economy econ;
    /** Basic Constructor. */
    public Shop(Economy e) {
        listings = new Hashtable<>();
        shopGUI = new ShopHolder(this);
        shopCommands = new ShopCommandExecutor(this);
        this.econ = e;
    }

    /**
     * List an item
     * @param p     Player who is making listing
     * @param price Price of item
     * @param amt   Amount of items to list
     * @param i     Itemstack to list
     * @return      Success?
     */
    public Boolean createListing(Player p, double price, int amt, ItemStack i) {
        UUID id = UUID.randomUUID();
        if (listings.get(id) != null) return false;
        ShopListing sl = new ShopListing(p, i, price, amt, id);
        listings.put(sl.getID(), sl);
        return true;
    }

    public ShopListing removeListing(UUID listingID) {
        return listings.remove(listingID);
    }

    public ShopCommandExecutor getCommandExecutor() {
        return this.shopCommands;
    }

    /**
     *  Creates a new ShopHolder GUI for viewing
     * */
    public Inventory getGUI() {
        return new ShopHolder(this).getInventory();
    }

    public Hashtable<UUID, ShopListing> getListings() {
        return this.listings;
    }


    public double executeBuy(Player p, ShopListing l) {
        if (!econ.has(p,l.getPrice())) {
            p.sendMessage(INSUFF_BALANCE);
            return -1;
        }
        if (!listings.containsKey(l.getID())) {
            p.sendMessage(GEN_SHOP_ERR);
            return -1;
        }
        EconomyResponse er = econ.withdrawPlayer(p,l.getPrice());
        if (er.type == EconomyResponse.ResponseType.FAILURE) {
            p.sendMessage(GEN_SHOP_ERR);
            return -1;
        }
        removeListing(l.getID());
        ItemStack origItem = l.getItem();
        ItemGiver.giveItem(p, origItem);
        return l.getPrice();
    }
    public double executeBuy(Player p, UUID l) {
        return this.executeBuy(p,listings.get(l));
    }

    public Boolean addItem(Player p, ItemStack itm, int amount, double price) {
        ItemStack shopitm = new ItemStackBuilder(itm).copy(itm, amount);
        ItemStack original = new ItemStackBuilder(itm).copy(itm);
        p.getInventory().setItemInMainHand(new ItemStackBuilder(Material.AIR).build());
        if (createListing(p, price, amount, shopitm)) {
            itm.setAmount(itm.getAmount() - amount);
            p.getInventory().setItemInMainHand(itm);
            return true;
        } else {
            p.getInventory().setItemInMainHand(original);
            p.sendMessage(GEN_SHOP_ERR);
            return false;
        }
    }

}
