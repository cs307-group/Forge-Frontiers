package com.forgefrontier.forgefrontier;

import com.forgefrontier.forgefrontier.utils.Manager;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Game manager will load last and set flags/call disable functions
 * Eventually, expect to start GameManager FIRST then not init at all
 */
public class GameManager extends Manager {


    HashMap<String, Boolean> state;
    public GameManager(ForgeFrontier plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        // Read load states from DB
        state = plugin.getDatabaseManager().getManagementDB().getSyncFeatureStates();
        if (state.containsKey("Bazaar") && !state.get("Bazaar")) {
            plugin.getBazaarManager().disable();
        }
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::refreshStates,800,400);


    }

    public void refreshStates() {
        plugin.getLogger().log(Level.INFO, "Refreshing Game State...");
        new Thread(
                () -> {
        HashMap<String, Boolean> nState = plugin.getDatabaseManager().getManagementDB().getSyncFeatureStates();
        if (nState.containsKey("Bazaar") && state.get("Bazaar") != nState.get("Bazaar")) {
            if (nState.get("Bazaar")) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getLogger().log(Level.INFO, "Enabling Bazaar");
                    plugin.getBazaarManager().init();
                });
            }
            else {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    plugin.getBazaarManager().disable();
                    plugin.getLogger().log(Level.INFO,"Disabled Bazaar");
                });
            }
        }
                }).start();
    }


    @Override
    public void disable() {

    }
}
