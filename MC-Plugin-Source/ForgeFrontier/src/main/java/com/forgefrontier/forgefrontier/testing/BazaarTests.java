package com.forgefrontier.forgefrontier.testing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarEntry;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarManager;
import com.forgefrontier.forgefrontier.connections.BazaarDB;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.UUID;

public class BazaarTests {
    private ForgeFrontier plugin;
    private BazaarManager bm;
    private Player player;
    private final int TEST_ITEM_IDX = 27;
    private Economy econ;
    private BazaarDB db;
    private boolean testsPassed = true;

    public class EntryPair {
        public BazaarEntry buy;
        public BazaarEntry sell;
    }
    public void sendFailMessage(String testmsg) {
        player.sendMessage("" + ChatColor.DARK_RED + "[ Failed Test ] " + testmsg);
    }


    public BazaarTests(ForgeFrontier plugin, Player testPlayer) {
        this.plugin = plugin;
        bm = plugin.getBazaarManager();
        this.player = testPlayer;
        this.econ = plugin.getEconomy();
        this.db = plugin.getDatabaseManager().getBazaarDB();
    }

    public void clear() {
        PriorityQueue<BazaarEntry> pqb = bm.getBuyLookup().get(TEST_ITEM_IDX);
        PriorityQueue<BazaarEntry> pqs = bm.getSellLookup().get(TEST_ITEM_IDX);
        if (pqb != null) {
            ArrayList<BazaarEntry> bea = new ArrayList<>(pqb);
            bea.forEach((e) -> {
                if (bm.removeListingSync(e)) {
                    player.sendMessage("Cleared buy order");
                }
                else
                    player.sendMessage("Failed to clear buy order");
            });
        }
        if (pqs != null) {
            ArrayList<BazaarEntry> bea = new ArrayList<>(pqs);
            bea.forEach((e) -> {
                if (bm.removeListingSync(e)) {
                    player.sendMessage("Cleared sell order");
                }
                else
                    player.sendMessage("Failed to clear sell order");
            });
        }
    }

    public void runTests() {
        testsPassed = true;
        clear();
        EntryPair pair = new EntryPair();

        if (testCreate(pair))
            testFill(pair);
        if (testsPassed) {
            player.sendMessage(ChatColor.GREEN + "[ TESTS PASSED ] Bazaar tests seem to pass.");
        }
    }

    public void testFill(EntryPair pair) {
        player.sendMessage("Testing Fill Buy/Sell Listing");
        double playerMoney = econ.getBalance(player);
        if (!bm.execSellOrder(player, pair.sell.getSlotID(), pair.sell.getAmount())) {
            sendFailMessage("Filling sell order unsuccessful...");
            testsPassed = false;
        }
        if (playerMoney != econ.getBalance(player)) {
            sendFailMessage("Player Balance Error -- Begin: " + playerMoney
                    + " -- End: " + econ.getBalance(player));
            testsPassed = false;
        }
        ItemStack item = bm.getRealItem(pair.buy.getSlotID());
        ItemGiver.giveItem(player, item, pair.buy.getAmount());
        double toGet = pair.buy.getAmount() * pair.buy.getPrice();
        playerMoney = econ.getBalance(player) + toGet;
        double amount = bm.execBuyOrder(player, pair.sell.getSlotID(), pair.sell.getAmount());
        if (amount != toGet) {
            sendFailMessage("Invalid total from fill buy order -- Expected: " + toGet + " -- Actual: " + amount);
        }
    }

    public boolean testCreate(EntryPair pair) {

        player.sendMessage("Testing Create Buy and Sell Listing");
        double bal = econ.getBalance(player);
        econ.depositPlayer(player,403);
        bm.createBuyListing(player,TEST_ITEM_IDX, 1, 403);
        if (bal != econ.getBalance(player)) {
            sendFailMessage("Test Create Balance Error -- Begin: " + bal
                    + " -- End: " + econ.getBalance(player));
            testsPassed = false;
        }
        ItemGiver.giveItem(player,bm.getRealItem(TEST_ITEM_IDX).clone(),5);
        bm.createSellListing(player, bm.getRealItem(TEST_ITEM_IDX), 5,404);
        ArrayList<BazaarEntry> plist = bm.getPlayerListings(player.getUniqueId());
        boolean buyFound = false; boolean sellFound = false;

        for (BazaarEntry be : plist) {
            if (be.getSlotID() == TEST_ITEM_IDX && be.getBType() && be.getPrice() == 403) {
                pair.buy = be;
                buyFound = true;
            }
            if (be.getSlotID() == TEST_ITEM_IDX && !be.getBType() && be.getPrice() == 404) {
                pair.sell = be;
                sellFound = true;
            }
        }
        if (!buyFound || !sellFound) {
            sendFailMessage("Failed to create buy/sell listing");
            testsPassed = false;
            return false;
        }
        return true;
    }






}
