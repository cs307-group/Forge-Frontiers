package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatCalc;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * PlayerManager
 *
 * Manages the players which join and leave the server and the custom data associated with them
 */
public class PlayerManager extends Manager implements Listener {


    /** a map for the FFPlayers which store custom data */
    Map<UUID, FFPlayer> ffPlayers;
    /** a map for the Players which store data native to minecraft Players */
    Map<UUID, Player> players;
    Map<String, Player> playersByName;

    /**
     * A Constructor for the player manager class, which sets the plugin and initializes the HashMaps
     *
     * @param plugin the plugin instance which the server is using to run
     */
    public PlayerManager(ForgeFrontier plugin) {
        super(plugin);
        ffPlayers = new HashMap<>();
        players = new HashMap<>();
        playersByName = new HashMap<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void disable() {
        // TODO: Save all players in the hashmap in the database.
    }

    /**
     * Runs whenever a player joins the server
     *
     * @param event the player event which holds data pertaining to the player that joins the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // System.out.println("PLAYER JOINED");
        Player player = event.getPlayer();

        players.put(player.getUniqueId(), player);
        playersByName.put(player.getName(), player);
        ffPlayers.put(player.getUniqueId(), FFPlayer.getPlayerFromDatabase(player.getUniqueId()));
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
        playersByName.remove(player.getName(), player);
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

            // Modifies incoming damage based on defense stat
            damage = StatCalc.modifyIncomingDamage(damage, ffPlayer.getDEF());

            // Modifies incoming damage based on health stat
            double convertedDamage = StatCalc.convertDamage(damage, maxHealth, ffPlayer);
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

    /** Getter */
    public Map<UUID, FFPlayer> getFFPlayers() {
        return ffPlayers;
    }

    /**
     * @param playerID ID of player to be returned
     * @return the FFPlayer object corresponding with the UUID playerID
     */
    public FFPlayer getFFPlayerFromID(UUID playerID) {
        return ffPlayers.get(playerID);
    }

    /**
     * @param name name of the player that is to be returned
     * @return the player with the display name equal to the name parameter
     */
    public Player getPlayerByName(String name) {
        return playersByName.get(name);
    }

    /**
     * @param name name of the player that is being checked to be online
     * @return if the player with the name exists then true
     */
    public boolean hasPlayerWithName(String name) {
        return playersByName.containsKey(name);
    }

    /** @return the map of players mapped to their UUID*/
    public Map<UUID, Player> getPlayersByUUID() {
        return players;
    }
}
