package com.forgefrontier.forgefrontier.shop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Implements functionality to buy/sell items through a GUI interface
 */
public class Shop {
    private static final String INSUFF_BALANCE = ChatColor.DARK_RED +
            "[SHOP] You don't have enough money to buy this item!";
    private static final String SELF_BUY_ERR = ChatColor.DARK_RED +
            "[SHOP] You cannot buy your own listings!";
    private static final String GEN_SHOP_ERR = ChatColor.RED +
            "[SHOP] An error occurred with the shop. Please try again.";

    private ConcurrentHashMap<UUID, ShopListing> listings;
    private TreeMap<UUID, ShopListing> listingsSorted;

    ShopHolder shopGUI;
    ShopCommandExecutor shopCommands;
    Economy econ;
    /** Basic Constructor. */
    public Shop() {
        listings = new ConcurrentHashMap<>();
        listingsSorted = new TreeMap<>();
        shopGUI = new ShopHolder(this);
        shopCommands = new ShopCommandExecutor(this);
        this.econ = ForgeFrontier.getInstance().getEconomy();
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
        addListingToDatabase(sl);
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

    public ConcurrentHashMap<UUID, ShopListing> getListings() {
        return this.listings;
    }

    public boolean executeRemoveListing(Player p, ShopListing l) {
        if (l.getLister().getUniqueId().compareTo(p.getUniqueId()) != 0) {
            p.sendMessage(GEN_SHOP_ERR);
            return false;
        }
        if (!listings.containsKey(l.getID())) {
            p.sendMessage(GEN_SHOP_ERR);
            return false;
        }
        if (ForgeFrontier.getInstance().getDatabaseManager().getShopDB().removeListing(l.getID())) {
            ItemGiver.giveItem(p, l.getItem());
            removeListing(l.getID());
            return true;
        }
        return false;
    }
    public double executeBuy(Player p, ShopListing l) {

        if (l.getLister().getUniqueId().compareTo(p.getUniqueId()) == 0) {
            if (!listings.containsKey(l.getID())) {
                p.sendMessage(GEN_SHOP_ERR);
                return -1;
            }
            p.sendMessage(SELF_BUY_ERR);
            return -1;
        }
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

        OfflinePlayer offlinePlayer = Bukkit.getPlayer(l.getLister().getUniqueId());
        if (offlinePlayer == null) offlinePlayer = Bukkit.getOfflinePlayer(l.getLister().getUniqueId());

        removeListing(l.getID());
        ItemStack origItem = l.getItem();
        ItemGiver.giveItem(p, origItem);
        setSoldDB(l.getID(),p.getUniqueId());
        econ.depositPlayer(offlinePlayer, l.getPrice());
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

    public void addListingToDatabase(ShopListing listing) {
        String listingId = listing.getID().toString();
        String itemMaterial = listing.getItem().getType().toString();
        String itemID = itemMaterial;
        ItemMeta im = listing.getItem().getItemMeta();
        String itemname = ItemUtil.itemName(listing.getItem());
        String lore = "";
        if (im != null) {
            if (im.hasLore()) {
                List<String> lorelist = im.getLore();
                if (lorelist != null)
                    lore = String.join("\n", lorelist);
            }
        }
        String price = Double.toString(listing.getPrice());
        int amount = listing.getItem().getAmount();
        String listerID = listing.getLister().getUniqueId().toString();
        String listerName = listing.getLister().getName();
        long dateListed = System.currentTimeMillis();
        String customData = "";
        CustomItemInstance ci = CustomItemManager.asCustomItemInstance(listing.getItem());
        if (ci != null) {
            customData = ci.getData().toString();
            itemID = ci.getBaseItem().getCode();
        }
        StringBuilder info = new StringBuilder();
        info.append("ID: ").append(listerID).append("\tMaterial: ").append(itemMaterial)
                .append("\nLore: ").append(lore).append("\tPrice: ").append(price).append("\tAmount: ").append(amount)
                .append("\nLister: ").append(listerID).append("\tDate: ").append(dateListed)
                .append("\nCustomData: ").append(customData);
        ForgeFrontier.getInstance().getLogger().log(Level.INFO,"ADDING TO DB: \n" + info);

        ForgeFrontier.getInstance().getDatabaseManager().getShopDB().insertShopListing
                (listingId,itemID,itemMaterial,itemname,lore,price,amount
                ,listerID,null,-1,dateListed,customData,listerName);
    }

    public boolean setSoldDB(UUID listing_id, UUID player) {
        return ForgeFrontier.getInstance().getDatabaseManager().getShopDB()
                .setListingSold(listing_id, player, System.currentTimeMillis());
    }

    public boolean loadListing(UUID listing_id, String itemId, String material, String name,
                            String lore, Double price, int amount, UUID listerID, String customData, String playerName) {
        ItemStack itm;
        if (!customData.equals("")) {
            CustomItemInstance cii = CustomItemManager.getInstanceFromData(customData);
            if (cii == null) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                        "Shop ERROR while loading custom item: " + customData);
                return false;
            }
            itm = cii.asItemStack();
        }
        else {
            Material m = Material.matchMaterial(material);
            if (m == null) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                        "Shop ERROR: loading shop: " + material + " from database.");
                return false;
            }

            itm = new ItemStack(m);
            itm.setAmount(amount);
            ItemMeta im = itm.getItemMeta();
            if (im == null && !lore.equals("")) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                        "Shop ERROR: loading item meta of " + name + " from database.");
                return false;
            } else if (im != null) {
                if (!lore.equals(""))
                    im.setLore(List.of(lore.split("\n")));
                if (!name.equals(ItemUtil.simpleRename(itm.getType())))
                    im.setDisplayName(name);
                itm.setItemMeta(im);
            }


        }
        ShopPlayer sp = new ShopPlayer(listerID, playerName);
        ShopListing sl = new ShopListing(sp,itm,price,listing_id);
        if (this.listings.put(listing_id,sl) != null) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "Shop WARNING: uuid(" + listing_id + ") loaded multiple times!");
        }
        return true;
    }



}
