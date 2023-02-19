package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * FFPlayer
 *
 * The representation of a Player which contains extra data necessary for play on the ForgeFrontier server
 */
public class FFPlayer {

    /** an array of PlayerStat objects which represent the stats of the player */
    PlayerStat[] stats;

    /** the unique ID of the player this FFPlayer represents */
    UUID playerID;

    /** the current level of the player */
    float currentLevel;

    double currentHealth;

    /**
     * gets the FFPlayer value from the database on player join if it exists
     *
     * @param playerID the ID of the player
     * @return the instance FFPlayer representation of the player
     */
    public static FFPlayer getPlayerFromDatabase(UUID playerID) {
        //TODO: Get player from database
        return null;
    }

    /**
     * Constructs the FFPlayer class given a player object (sets the stats to a level 1 character)
     *
     * @param player the player that this instance of FFPlayer will represent
     */
    public FFPlayer(Player player) {
        //TODO: Check if the player's data already exists in the database

        playerID = player.getUniqueId();

        stats = new PlayerStat[] {
                new PlayerStat(20, StatEnum.HP),
                new PlayerStat(1, StatEnum.ATK),
                new PlayerStat(0, StatEnum.STR),
                new PlayerStat(0, StatEnum.DEX),
                new PlayerStat(0, StatEnum.CRATE),
                new PlayerStat(0, StatEnum.CDMG),
                new PlayerStat(0, StatEnum.DEF)
        };

        this.currentHealth = getHP();
        //TODO: store player data in database
    }

    /**
     * Updates the current health of the ffPlayer and converts the damage specified by the parameter from the
     * ForgeFrontier value to the Minecraft value
     *
     * @param ffDamage the 'ForgeFrontier' amount of damage done to the player
     * @param mcMaxHealth the current 'Minecraft' max health of the player
     * @return the Minecraft amount of damage done to the player
     */
    public double convertDamage(double ffDamage, double mcMaxHealth) {
        currentHealth -= ffDamage;
        return mcMaxHealth * (ffDamage / getHP());
    }

    public void die() {
        currentHealth = getHP();
    }

    /**
     * @return the HP stat value of the FFPlayer
     */
    public int getHP() {
        return stats[0].getStatValue();
    }

    /** @return the ATK stat value of the FFPlayer */
    public int getATK() {
        return stats[1].getStatValue();
    }

    /** @return the STR stat value of the FFPlayer */
    public int getSTR() {
        return stats[2].getStatValue();
    }

    /** @return the DEX stat value of the FFPlayer */
    public int getDEX() {
        return stats[3].getStatValue();
    }

    /** @return the CRATE stat value of the FFPlayer */
    public int getCRATE() {
        return stats[4].getStatValue();
    }

    /** @return the CDMG stat value of the FFPlayer */
    public int getCDMG() {
        return stats[5].getStatValue();
    }

    /** @return the DEF stat value of the FFPlayer */
    public int getDEF() {
        return stats[6].getStatValue();
    }

    /**
     * Returns if the given player is what is represented by this instance of FFPlayer
     *
     * @param player the player to which this instance of FFPlayer is being compared
     * @return true if the FFPlayer instance is representative of the parameter
     */
    public boolean isPlayer(Player player) {
        return playerID == player.getUniqueId();
    }
}
