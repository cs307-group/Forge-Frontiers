package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.BazaarDB;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

public class BazaarManager {
    private final ForgeFrontier plugin;
    private final BazaarDB bazaarDB;
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

    private static ItemStack DEFAULT_NULL_ITEM;

    public BazaarManager(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.bazaarDB = plugin.getDatabaseManager().getBazaarDB();
        DEFAULT_NULL_ITEM = (new ItemStackBuilder(Material.BARRIER).setDisplayName("" + ChatColor.RED + "N/A").build());
    }

    public void init() {
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
        refreshListingDisplay(100);
    }

    public int loadListings() {
        ArrayList<BazaarEntry> listings = bazaarDB.loadListings();

        // Load Lookup Table
        buyLookup = new HashMap<>();
        sellLookup = new HashMap<>();
        // Load TreeSets
        for (int i = 0; i < MAX_SLOT; i++) {
            buyLookup.put(i, new PriorityQueue<>(new BazaarEntry.BazaarEntryComparator()));
            sellLookup.put(i, new PriorityQueue<>(new BazaarEntry.BazaarEntryComparator()));
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


    public void refreshListingDisplay(int cheapestN) {
        for (int i = 0; i < lookupItems.size(); i++) {
            if (lookupItems.get(i).getType() == DEFAULT_NULL_ITEM.getType()) {
                continue;
            }

            PriorityQueue<BazaarEntry> bItems = buyLookup.get(i);

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
            PriorityQueue<BazaarEntry> sItems = sellLookup.get(i);
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

    public void localInsertListing(BazaarEntry be) {
        playerListings.put(be.getListerID(),be);
        if (be.getBType())
            buyLookup.get(be.getSlotID()).add(be);
        else
            sellLookup.get(be.getSlotID()).add(be);
        refreshListingDisplay(100);
    }
}
