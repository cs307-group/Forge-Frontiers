package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.BazaarDB;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.awt.desktop.OpenFilesEvent;
import java.util.*;
import java.util.logging.Level;

public class BazaarManager {
    private final ForgeFrontier plugin;
    private BazaarDB bazaarDB;
    private final int MIN_SLOT = 0;
    private final int MAX_SLOT = 28;

    // Lookup Index -> { Listing ID: Bazaar Entry }
    private HashMap<Integer, PriorityQueue<BazaarEntry>> buyLookup;
    private HashMap<Integer, PriorityQueue<BazaarEntry>> sellLookup;

    // { PlayerID -> Bazaar Entry }
    private HashMap<UUID, BazaarEntry> playerListings;


    // Items to be compared to ("looked up")
    private ArrayList<ItemStack> lookupItems;

    // Items to be displayed
    private ArrayList<ItemStack> displayItems;
    public static BazaarManager bazaarManager = null;

    private static ItemStack DEFAULT_NULL_ITEM;
    public static String bazaarPrefix = "" + ChatColor.GOLD + "[Bazaar] " + ChatColor.WHITE;
    public static boolean enabled = false;
    public static Economy econ;
    private final int REFRESH_AMT = 128;
    public BazaarManager(ForgeFrontier plugin) {
        this.plugin = plugin;
        econ = plugin.getEconomy();
        DEFAULT_NULL_ITEM = (new ItemStackBuilder(Material.BARRIER).setDisplayName("" + ChatColor.RED + "N/A").build());
        enabled = false;
        if (bazaarManager == null)
            bazaarManager = this;
    }


    public void init() {
        enabled = false;
        this.bazaarDB = plugin.getDatabaseManager().getBazaarDB();
        // Pull Bazaar items from database
        lookupItems = bazaarDB.loadLookup(MAX_SLOT);

        // Replace nulls in lookup with unreachable items
        lookupItems.replaceAll(x -> Objects.isNull(x) ? DEFAULT_NULL_ITEM : x);
        displayItems = new ArrayList<>(Collections.nCopies(MAX_SLOT, DEFAULT_NULL_ITEM));
        // Load entries
        int loaded = loadListings();

        plugin.getLogger().log(Level.INFO,
                """
                        Bazaar DB Initialized
                        \tListings: %d
                        """.formatted(loaded));
        refreshListingDisplay(REFRESH_AMT);
        enabled = true;
    }

    public int loadListings() {
        ArrayList<BazaarEntry> listings = bazaarDB.loadListings();

        // Load Lookup Table
        buyLookup = new HashMap<>();
        sellLookup = new HashMap<>();
        // Load TreeSets
        for (int i = 0; i < MAX_SLOT; i++) {
            buyLookup.put(i, new PriorityQueue<>(new BazaarEntry.BazaarEntryComparator()));
            sellLookup.put(i, new PriorityQueue<>(new BazaarEntry.BazaarEntryRevComparator()));
        }
        playerListings = new HashMap<>();

        // Store listings in data structures
        for (BazaarEntry entry : listings) {
            playerListings.put(entry.getListerID(), entry);
            if (entry.getBType())
                buyLookup.get(entry.getSlotID()).add(entry);
            else
                sellLookup.get(entry.getSlotID()).add(entry);
        }

        return listings.size();
    }

    public void refreshListingDisplay() { this.refreshListingDisplay(REFRESH_AMT); }

    /**
     * Update buy/sell display items to show the minimum cost for first n items.
     * @param cheapestN n items to show preview for
     */
    public void refreshListingDisplay(int cheapestN) {
        for (int i = 0; i < lookupItems.size(); i++) {
            if (lookupItems.get(i).getType() == DEFAULT_NULL_ITEM.getType()) {
                continue;
            }

            PriorityQueue<BazaarEntry> bItems = sellLookup.get(i);

            String buyStr = "";
            String sellStr = "";
            // Get price to buy N items
            int numSeen = 0;
            double price = 0;
            Iterator<BazaarEntry> itr = bItems.iterator();
            while (itr.hasNext() && numSeen < cheapestN) {
                BazaarEntry be = itr.next();
                int amt = be.getAmount();
                amt = (amt + numSeen > cheapestN) ? cheapestN - numSeen : amt;
                numSeen += amt;
                price += amt * be.getPrice();
            }
            buyStr = "" + ChatColor.GREEN + "Buy " + numSeen + "x: " + ChatColor.GOLD + "%.2fg".formatted(price);
            // Get price to sell N items
            numSeen = 0;
            price = 0;
            PriorityQueue<BazaarEntry> sItems = buyLookup.get(i);
            itr = sItems.iterator();
            while (itr.hasNext() && numSeen < cheapestN) {
                BazaarEntry be = itr.next();
                int amt = be.getAmount();
                amt = (amt + numSeen > cheapestN) ? cheapestN - numSeen : amt;
                numSeen += amt;
                price += amt * be.getPrice();
            }
            sellStr = "" + ChatColor.RED + "Sell " + numSeen + "x: " + ChatColor.GOLD + "%.2fg".formatted(price);

            // Set display item
            String priceLore = "" + buyStr + "\n" + sellStr;
            ItemStack lookupItm = lookupItems.get(i);
            String lore = ItemUtil.getStringLore(lookupItm);
            lore += priceLore;
            ItemStack displayItem = new ItemStackBuilder(lookupItm.getType())
                    .setDisplayName(ItemUtil.itemName(lookupItm)).setFullLore(lore).build();
            this.displayItems.set(i,displayItem);
        }
    }

    public void clear() {
        this.lookupItems.clear();
        this.displayItems.clear();
        this.playerListings.clear();
        this.buyLookup.clear();
        this.sellLookup.clear();
    }

    public static void reload() {
        bazaarManager.clear();
        bazaarManager.init();
    }

    public boolean setItemSlot(ItemStack itm, int idx) {
        if (idx < MIN_SLOT || idx > MAX_SLOT) {
            return false;
        }
        if (itm == null) {
            if (plugin.getDatabaseManager().getBazaarDB().deleteSlotID(idx)) {
                lookupItems.set(idx, (new ItemStackBuilder(Material.BARRIER)
                        .setDisplayName("" + ChatColor.RED + "N/A").build()));
                return true;
            } else {
                return false;
            }
        }
        String name = ItemUtil.itemName(itm);
        String lore = ItemUtil.getStringLore(itm);
        String cdata = ItemUtil.getCustomData(itm);
        if (plugin.getDatabaseManager().getBazaarDB().setLookup(
                idx, itm.getType().toString(), name, lore, cdata)) {
            lookupItems.set(idx, (new ItemStackBuilder(itm).build()));
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ItemStack> getLookupItems() { return lookupItems; }
    public ArrayList<ItemStack> getDisplayItems() { return displayItems; }


    /**
     * Player p RECIEVES items from this
     * @param p     Player buying from sell order
     * @return      Success or not
     */
    public boolean execSellOrder(Player p, int item, int amount) {
        double pbal = econ.getBalance(p);
        // amt > top -> exec, reduce
        // amt < top -> exec, modify
        // amt == top -> exec, remove
        double cost = walkSellPrice(item, amount, pbal);
        if (cost == -1) return false;   // cannot afford
        PriorityQueue<BazaarEntry> lkup = sellLookup.get(item);
        int amt = amount;
        ArrayList<BazaarEntry> removed = new ArrayList<>();
        double total = 0;
        BazaarEntry modified = null;
        BazaarEntry modifiedOrig = null;

        while (amt > 0) {
            BazaarEntry be = lkup.peek();

            if (be == null) break;
            if (amt >= be.getAmount()) {
                total += (double)be.getAmount() * be.getPrice();
                amt -= be.getAmount();
                be.setValid(false);
                removed.add(be);
                lkup.poll();
            } else {
                modifiedOrig = new BazaarEntry(be);
                be.setValid(false);
                modified = be;
                total += amt * be.getPrice();
                be.setAmount(be.getAmount() - amt);
                amt = 0;
            }
        }
        // Not enough amount, total, or failure to delete from DB
        // Recovery
        if (amt > 0 || total > pbal || !bazaarDB.massDeleteOrders(removed) ||
                (modified != null && !bazaarDB.updateOrder(modified.getEntryID(),modified))) {
            for (BazaarEntry be : removed) {
                be.setValid(true);
                lkup.add(be);
            }
            if (modified != null)
                modified.copy_content(modifiedOrig);
            return false;
        }


        // perform transactions
        econ.withdrawPlayer(p, total);
        for (BazaarEntry be : removed) {
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(be.getListerID());
            if (!op.hasPlayedBefore()) { plugin.getLogger().log(Level.SEVERE,"Unknown player listing fulfilled"); }
            else {
                econ.depositPlayer(p,be.getPrice() * be.getAmount());
            }
        }
        if (modified != null) {
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(modified.getListerID());
            if (!op.hasPlayedBefore()) {
                plugin.getLogger().log(Level.SEVERE, "Unknown player listing fulfilled");
            } else {
                econ.depositPlayer(p, modified.getPrice() * modified.getAmount());
            }
        }
        ItemStack realItem = getRealItem(item);
        ItemGiver.giveItem(p,realItem, amount);
        refreshListingDisplay(100);
        return true;
    }

    public double walkSellPrice(int item, int amount, double max_price) {
        PriorityQueue<BazaarEntry> lkup = sellLookup.get(item);
        Iterator<BazaarEntry> itr = lkup.iterator();
        double total = 0;
        int amt = amount;
        while (amt > 0 && itr.hasNext() && total < max_price) {
            BazaarEntry be = itr.next();
            if (amt >= be.getAmount()) {
                total += (double)be.getAmount() * be.getPrice();
                amt -= be.getAmount();
            } else {
                total += amt * be.getPrice();
                amt = 0;
            }
        }
        if (total > max_price) return -1;
        return total;
    }



    public boolean createBuyListing(Player p, int idx, int amount, double price) {
        ItemStack itm = lookupItems.get(idx);
        double bal = econ.getBalance(p);

        if (bal >= amount * price) {
            BazaarEntry entry = new BazaarEntry(true, idx, amount, price, p.getUniqueId());
            if (bazaarDB.insertListing(entry)) {
                econ.withdrawPlayer(p,amount * price);
                p.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.GOLD + "Successfully created listing!");
                localInsertListing(entry);

                return true;
            } else {
                p.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.RED +
                        "Unexpected error while creating listing..");
                return false;
            }
        } else {
            p.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough money to create this listing.");
            return false;
        }


    }

    public boolean createSellListing(Player p, ItemStack itm, int amount, double price) {
        int idx = 0;
        for (idx = 0; idx < lookupItems.size(); idx++) {
            if (ItemUtil.customCompare(itm, lookupItems.get(idx)))
                break;
        }
        if (idx == lookupItems.size()) {
            p.sendMessage(ForgeFrontier.CHAT_PREFIX + "Item is not listable on the bazaar.");
            return false;
        }
        if (ItemUtil.hasItem(p, lookupItems.get(idx),amount)) {
            BazaarEntry entry = new BazaarEntry(false, idx, amount, price, p.getUniqueId());
            if (bazaarDB.insertListing(entry)) {
                ItemUtil.take(p, itm, amount);
                p.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.GOLD + "Successfully created listing!");
                localInsertListing(entry);
                return true;
            } else {
                p.sendMessage(ForgeFrontier.CHAT_PREFIX + ChatColor.RED +
                        "Unexpected error while creating listing..");
                return false;
            }
        } else {
            p.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough items to list.");
            return false;
        }
    }

    public double getMinInstantBuyPrice(int idx) {
        BazaarEntry be = this.sellLookup.get(idx).peek();
        if (be != null)
            return be.getPrice();
        else return 100;
    }

    public ItemStack getRealItem(int idx) {
        return lookupItems.get(idx);
    }

    public void localInsertListing(BazaarEntry be) {
        playerListings.put(be.getListerID(),be);
        if (be.getBType())
            buyLookup.get(be.getSlotID()).add(be);
        else
            sellLookup.get(be.getSlotID()).add(be);
        refreshListingDisplay(REFRESH_AMT);
    }
}
