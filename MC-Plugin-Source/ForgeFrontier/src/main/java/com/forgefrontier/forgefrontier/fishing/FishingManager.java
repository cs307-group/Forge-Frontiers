package com.forgefrontier.forgefrontier.fishing;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;


public class FishingManager extends Manager implements Listener {
    public FishingManager(ForgeFrontier plugin) {
        super(plugin);
    }

    @Override
    public void init() {



    }

    @Override
    public void disable() {

    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        PlayerFishEvent.State s = event.getState();



    }

}
