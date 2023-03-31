package com.forgefrontier.forgefrontier.testing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarEntry;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BazaarTests {
    private ForgeFrontier plugin;
    private BazaarManager bm;
    private Player player;
    private final int TEST_ITEM_IDX = 27;


    public BazaarTests(ForgeFrontier plugin, Player testPlayer) {
        this.plugin = plugin;
        bm = plugin.getBazaarManager();
        this.player = testPlayer;
    }

    public void testCreate() {

        player.sendMessage("Testing Create Buy and Sell Listing");
        bm.createBuyListing(player,TEST_ITEM_IDX, 1, 403);
        bm.createSellListing(player, bm.getRealItem(TEST_ITEM_IDX), 1,404);
        ArrayList<BazaarEntry> plist = bm.getPlayerListings(player.getUniqueId());




    }






}
