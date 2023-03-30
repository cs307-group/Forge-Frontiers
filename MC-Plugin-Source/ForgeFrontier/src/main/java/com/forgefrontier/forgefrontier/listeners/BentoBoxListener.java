package com.forgefrontier.forgefrontier.listeners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.events.team.TeamJoinedEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public class BentoBoxListener implements Listener {

    ForgeFrontier plugin;

    public BentoBoxListener(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Island island = BentoBox.getInstance().getIslandsManager().getIsland(Bukkit.getWorld("bskyblock_world"), BentoBox.getInstance().getPlayers().getUser(e.getPlayer().getUniqueId()));
        if(island == null)
            return;
        plugin.getDatabaseManager().getPlayerDB().updatePlayerIsland(e.getPlayer().getUniqueId(), island.getUniqueId(), (success) -> {
            plugin.getLogger().severe("Unable to update player island id. Player: " + e.getPlayer().getName());
        });
    }

    @EventHandler
    public void islandJoinEvent(TeamJoinedEvent e) {
        plugin.getDatabaseManager().getPlayerDB().updatePlayerIsland(e.getPlayerUUID(), e.getIsland().getUniqueId(), (success) -> {
            plugin.getLogger().severe("Unable to update player island id. Player: " + Bukkit.getPlayer(e.getPlayerUUID()).getName());
        });
    }

    @EventHandler
    public void islandLeaveEvent(TeamLeaveEvent e) {
        plugin.getDatabaseManager().getPlayerDB().updatePlayerIsland(e.getPlayerUUID(), null, (success) -> {
            plugin.getLogger().severe("Unable to update player island id. Player: " + Bukkit.getPlayer(e.getPlayerUUID()).getName());
        });
    }

}
