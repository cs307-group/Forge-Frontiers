package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.ReforgeStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatCalc;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemValues;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * FFPlayer
 *
 * The representation of a Player which contains extra data necessary for play on the ForgeFrontier server
 */
public class FFPlayer {

    public static final PlayerStat[] baseStats = new PlayerStat[] {
            new PlayerStat(20, StatEnum.HP),
            new PlayerStat(1, StatEnum.ATK),
            new PlayerStat(0, StatEnum.STR),
            new PlayerStat(0, StatEnum.DEX),
            new PlayerStat(0, StatEnum.CRATE),
            new PlayerStat(0, StatEnum.CDMG),
            new PlayerStat(0, StatEnum.DEF)
    };

    /** an array of PlayerStat objects which represent the stats of the player */
    private PlayerStat[] stats;

    /** the unique ID of the Player this FFPlayer represents */
    public UUID playerID;

    /** the current health of the player */
    double currentHealth;

    /**
     * gets the FFPlayer value from the database on player join if it exists
     *
     * @param playerID the ID of the player
     * @return the instance FFPlayer representation of the player
     */
    public static FFPlayer getPlayerFromDatabase(UUID playerID) {
        ForgeFrontier plugin = ForgeFrontier.getInstance();
        FFPlayer ffPlayer = new FFPlayer(playerID);
        /*
        AtomicReference<FFPlayer> ffPlayer = new AtomicReference<>();
        plugin.getDBConnection().getExistingPlayerStats(playerID, (Map<String, Object> result) -> {
            if (result != null) {
                PlayerStat[] stats = new PlayerStat[7];
                stats[0] = new PlayerStat((int) result.get("HP"), StatEnum.HP);
                stats[1] = new PlayerStat((int) result.get("ATK"), StatEnum.ATK);
                stats[2] = new PlayerStat((int) result.get("STR"), StatEnum.STR);
                stats[3] = new PlayerStat((int) result.get("DEX"), StatEnum.DEX);
                stats[4] = new PlayerStat((int) result.get("CRATE"), StatEnum.CRATE);
                stats[5] = new PlayerStat((int) result.get("CDMG"), StatEnum.CDMG);
                stats[6] = new PlayerStat((int) result.get("DEF"), StatEnum.DEF);
                ffPlayer.set(new FFPlayer(playerID, (Double) result.get("current_health"), stats));
            } else {
                ffPlayer.set(new FFPlayer(playerID));
                plugin.getDBConnection().createPlayerStats(playerID, ffPlayer.get().getStats());
            }
        });
        return ffPlayer.get();
         */
        return ffPlayer;
    }

    /**
     * Constructs the FFPlayer class given a player object (sets the stats to a level 1 character if the character
     * does not exist in the database)
     *
     * @param player the player that this instance of FFPlayer will represent
     */
    public FFPlayer(Player player) {

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
    }


    /**
     * Constructs the FFPlayer class given a player object (sets the stats to a level 1 character if the character
     * does not exist in the database)
     *
     * @param playerID the playerID that this instance of FFPlayer will represent
     */
    public FFPlayer(UUID playerID) {

        this.playerID = playerID;


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
    }


    /**
     * Constructs the FFPlayer class given a player object (sets the stats to a level 1 character if the character
     * does not exist in the database)
     *
     * @param playerID the player that this instance of FFPlayer will represent
     * @param currentHealth the current health of the player
     * @param stats the player stats
     */
    public FFPlayer(UUID playerID, double currentHealth, PlayerStat[] stats) {

        this.playerID = playerID;

        this.stats = stats;

        this.currentHealth = currentHealth;
    }

    /**
     * Updates the ForgeFrontier current health value when the player respawns
     */
    public void respawn() {
        currentHealth = getHP();
    }

    /**
     * Increases all player stats by one on level up
     */
    public void levelUp() {
        for (int i = 0; i < stats.length; i++) {
            stats[i].addStat(new BaseStatistic(1, stats[i].getStatType()));
        }
    }

    /**
     * Updates the player's statistics based on the new armor it equips
     *
     * @param armorInstance the instance of the custom armor being equipped
     */
    public void updateStatsOnArmorEquip(CustomArmor.CustomArmorInstance armorInstance) {
        BaseStatistic[] baseStatistics = armorInstance.getBaseStats();
        ReforgeStatistic[] reforgeStatistics = armorInstance.getReforgeStats();
        GemValues[] gemValues = armorInstance.getGems();
        for (BaseStatistic baseStatistic : baseStatistics) {
            stats[StatEnum.getIntFromEnum(baseStatistic.getStatType())].addStat(baseStatistic);
        }
        for (ReforgeStatistic reforgeStatistic : reforgeStatistics) {
            stats[StatEnum.getIntFromEnum(reforgeStatistic.getStatType())].addStat(reforgeStatistic);
        }
        for (GemValues gemValue : gemValues) {
            if (gemValue != null) {
                stats[StatEnum.getIntFromEnum(gemValue.getStat().getStatType())].addStat(gemValue.getStat());
            }
        }
    }

    /**
     * Updates the player's statistics based on the armor it dequips
     *
     * @param armorInstance the instance of the custom armor being dequipped
     */
    public void updateStatsOnArmorDequip(CustomArmor.CustomArmorInstance armorInstance) {
        BaseStatistic[] baseStatistics = armorInstance.getBaseStats();
        ReforgeStatistic[] reforgeStatistics = armorInstance.getReforgeStats();
        GemValues[] gemValues = armorInstance.getGems();
        for (BaseStatistic baseStatistic : baseStatistics) {
            stats[StatEnum.getIntFromEnum(baseStatistic.getStatType())].removeStat(baseStatistic);
        }
        for (ReforgeStatistic reforgeStatistic : reforgeStatistics) {
            stats[StatEnum.getIntFromEnum(reforgeStatistic.getStatType())].removeStat(reforgeStatistic);
        }
        for (GemValues gemValue : gemValues) {
            if (gemValue != null) {
                stats[StatEnum.getIntFromEnum(gemValue.getStat().getStatType())].removeStat(gemValue.getStat());
            }
        }
    }

    /**
     * Returns the calculated outgoing damage when using a custom weapon
     *
     * @param weaponInstance the custom weapon instance that is being used
     * @return the calculated outgoing damage
     */
    public double getOutgoingDamageOnAttack(CustomWeapon.CustomWeaponInstance weaponInstance) {
        double damage = 0;

        BaseStatistic[] baseStatistics = weaponInstance.getBaseStats();
        ReforgeStatistic[] reforgeStatistics = weaponInstance.getReforgeStats();
        GemValues[] gemValues = weaponInstance.getGems();
        for (BaseStatistic baseStatistic : baseStatistics) {
            stats[StatEnum.getIntFromEnum(baseStatistic.getStatType())].addStat(baseStatistic);
        }
        for (ReforgeStatistic reforgeStatistic : reforgeStatistics) {
            stats[StatEnum.getIntFromEnum(reforgeStatistic.getStatType())].addStat(reforgeStatistic);
        }
        for (GemValues gemValue : gemValues) {
            if (gemValue != null) {
                stats[StatEnum.getIntFromEnum(gemValue.getStat().getStatType())].addStat(gemValue.getStat());
            }
        }

        damage = StatCalc.calcOutgoingDamage(this, weaponInstance.getMainStat()) ;

        for (BaseStatistic baseStatistic : baseStatistics) {
            stats[StatEnum.getIntFromEnum(baseStatistic.getStatType())].removeStat(baseStatistic);
        }
        for (ReforgeStatistic reforgeStatistic : reforgeStatistics) {
            stats[StatEnum.getIntFromEnum(reforgeStatistic.getStatType())].removeStat(reforgeStatistic);
        }
        for (GemValues gemValue : gemValues) {
            if (gemValue != null) {
                stats[StatEnum.getIntFromEnum(gemValue.getStat().getStatType())].removeStat(gemValue.getStat());
            }
        }

        return damage;
    }

    /** @return the HP stat value of the FFPlayer */
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

    /** @return the stats array of PlayerStats */
    public PlayerStat[] getStats() {
        return stats;
    }

    /** @return the current ffPlayer value for current health */
    public double getCurrentHealth() {
        return this.currentHealth;
    }

    /** Updates the value for current health */
    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
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

    /**
     * @return the string representation of player stats
     */
    public String getStatsString() {
        String output = "";
        for (int i = 0; i < stats.length; i++) {
            output += stats[i].toString() + "\n";
        }
        return output;
    }
}
