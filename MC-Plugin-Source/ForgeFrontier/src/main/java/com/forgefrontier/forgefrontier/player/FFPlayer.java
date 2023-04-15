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
import java.util.function.Consumer;

/**
 * FFPlayer
 *
 * The representation of a Player which contains extra data necessary for play on the ForgeFrontier server
 */
public class FFPlayer {

    /** an array of PlayerStat objects which represent the stats of the player */
    private PlayerStat[] stats;

    /** the unique ID of the Player this FFPlayer represents */
    public UUID playerID;

    /** the tier the current player is in */
    public int tier;

    /** the ascension level of the player */
    public int ascensionLevel;

    /** the current health of the player */
    double currentHealth;

    // The time of the next skill used. Used for cooldowns.
    long nextSkillTime;

    /**
     * gets the FFPlayer value from the database on player join if it exists
     *
     * @param playerID the ID of the player
     * @return the instance FFPlayer representation of the player
     */
    /*
    public static void getPlayerFromDatabase(UUID playerID, Consumer<FFPlayer> callback) {
        ForgeFrontier plugin = ForgeFrontier.getInstance();
        callback.accept(new FFPlayer(playerID));
        plugin.getDatabaseManager().getPlayerDB().getExistingPlayerStats(playerID, (Map<String, Object> result) -> {
            if (result != null) {
                PlayerStat[] stats = new PlayerStat[7];
                stats[0] = new PlayerStat((int) result.get("HP"), StatEnum.HP);
                stats[1] = new PlayerStat((int) result.get("ATK"), StatEnum.ATK);
                stats[2] = new PlayerStat((int) result.get("STR"), StatEnum.STR);
                stats[3] = new PlayerStat((int) result.get("DEX"), StatEnum.DEX);
                stats[4] = new PlayerStat((int) result.get("CRATE"), StatEnum.CRATE);
                stats[5] = new PlayerStat((int) result.get("CDMG"), StatEnum.CDMG);
                stats[6] = new PlayerStat((int) result.get("DEF"), StatEnum.DEF);

                callback.accept(new FFPlayer(playerID, (Double) result.get("current_health"), stats));
            } else {
                FFPlayer ffPlayer = new FFPlayer(playerID);
                callback.accept(ffPlayer);
                plugin.getDatabaseManager().getPlayerDB().createPlayerStats(playerID, ffPlayer.getStats());
            }
        });
    }
    */

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

        this.tier = 0;
        this.ascensionLevel = 0;
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
        this.tier = 0;
        this.ascensionLevel = 0;

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().getExistingPlayerStats(playerID, (statsMap) -> {
            if (statsMap != null) {
                stats = new PlayerStat[] {
                        new PlayerStat((Integer) statsMap.get("HP"), StatEnum.HP),
                        new PlayerStat((Integer) statsMap.get("ATK"), StatEnum.ATK),
                        new PlayerStat((Integer) statsMap.get("STR"), StatEnum.STR),
                        new PlayerStat((Integer) statsMap.get("DEX"), StatEnum.DEX),
                        new PlayerStat((Integer) statsMap.get("CRATE"), StatEnum.CRATE),
                        new PlayerStat((Integer) statsMap.get("CDMG"), StatEnum.CDMG),
                        new PlayerStat((Integer) statsMap.get("DEF"), StatEnum.DEF)
                };
                this.currentHealth = (Double) statsMap.get("current_health");
                this.tier = (Integer) statsMap.get("Tier");
                this.ascensionLevel = (Integer) statsMap.get("AscensionLevel");
            }
        });
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

        this.tier = 0;
        this.ascensionLevel = 0;
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
        for (PlayerStat stat : stats) {
            if (stat.getStatType() == StatEnum.CDMG || stat.getStatType() == StatEnum.CRATE) {
                stat.addStat(new BaseStatistic(1, stat.getStatType()));
            } else {
                stat.addStat(new BaseStatistic(1, stat.getStatType()));
            }
        }
    }


    /**
     * Decreases all player stats by one on level down
     */
    public void levelDown() {
        for (PlayerStat stat : stats) {
            if (stat.getStatType() == StatEnum.CDMG || stat.getStatType() == StatEnum.CRATE) {
                stat.removeStat(new BaseStatistic(1, stat.getStatType()));
            } else {
                stat.removeStat(new BaseStatistic(1, stat.getStatType()));
            }
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
        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(this);
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
        //ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(this);

        return damage;
    }

    /**
     * Ascends the player, returning their tier to zero, and giving them more base stats
     */
    public void ascend() {
        ascensionLevel++;

        Player player = ForgeFrontier.getInstance().getPlayerManager().getPlayersByUUID().get(playerID);
        int level = player.getLevel();
        player.setLevel(0);
        player.setExp(0);
        int tier = getTier();
        setTier(0);

        for (int i = 0; i < level - (level / 10); i++) {
            levelDown();
        }

        for (int i = 0; i < tier; i++) {
            levelUp();
        }
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

    /** @return the tier of the ffplayer */
    public int getTier() {
        return this.tier;
    }

    /** updates the current player tier to the new specified tier */
    public void setTier(int tier) {
        this.tier = tier;
    }

    /** @return the current ffPlayer value for current health */
    public double getCurrentHealth() {
        return this.currentHealth;
    }

    /** Updates the value for current health */
    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getAscension() {
        return ascensionLevel;
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

    public long getNextSkillTime() {
        return nextSkillTime;
    }

    public void setNextSkillTime(long nextSkillTime) {
        this.nextSkillTime = nextSkillTime;
    }

}
