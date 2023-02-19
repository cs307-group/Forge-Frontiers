package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PlayerManager
 *
 * Manages the players which join and leave the server and the custom data associated with them
 */
public class PlayerManager implements Listener {

    /** an instance of the ForgeFrontier plugin used for referencing events */
    ForgeFrontier plugin;

    /** a map for the FFPlayers which store custom data */
    Map<UUID, FFPlayer> ffPlayers;
    /** a map for the Players which store data native to minecraft Players */
    Map<UUID, Player> players;

    /**
     * A Constructor for the player manager class, which sets the plugin and initializes the HashMaps
     *
     * @param plugin the plugin instance which the server is using to run
     */
    public PlayerManager(ForgeFrontier plugin) {
        this.plugin = plugin;
        ffPlayers = new HashMap<>();
        players = new HashMap<>();
    }

    /**
     * Ran whenever the plugin enabled the module. You can safely access other modules here,
     * but no guarantee data will be in them.
     */
    public void init() {

    }

    /**
     * Runs whenever a player joins the server
     *
     * @param event the player event which holds data pertaining to the player that joins the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        players.put(player.getUniqueId(), player);
        ffPlayers.put(player.getUniqueId(), new FFPlayer(player));
    }

    /**
     * Runs whenever a player quits the server
     *
     * @param event the player event which holds data pertaining to the player that quits the server
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        players.remove(player.getUniqueId());
        ffPlayers.remove(player.getUniqueId());
    }

    /**
     * Handles the conversion of incoming damage so that when the player is hit it takes into account the ForgeFrontier
     * specific states (i.e. def/hp) which differ from the stats minecraft uses to handle taking damage
     *
     * @param event the event which occurs when an entity (including projectiles) damages another entity
     */
    @EventHandler
    public void onEntityDamageByEntity (EntityDamageEvent event) {
        //ensures the entity with the incoming damages is a player
        if (event.getEntity() instanceof Player player) {
            double damage = event.getDamage();
            FFPlayer ffPlayer = ffPlayers.get(player.getUniqueId());
            AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            double maxHealth = 20;
            if (maxHealthAttr != null) {
                maxHealth = maxHealthAttr.getValue();
            }
            double convertedDamage = ffPlayer.convertDamage(damage, maxHealth);
            event.setDamage(convertedDamage);
        }
    }

    /**
     * handles updating player health accordingly on respawn
     *
     * @param event the respawn event
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        FFPlayer ffPlayer = ffPlayers.get(player.getUniqueId());
        ffPlayer.respawn();
    }
}
