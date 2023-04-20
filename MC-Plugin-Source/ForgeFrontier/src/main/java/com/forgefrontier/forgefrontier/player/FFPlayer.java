package com.forgefrontier.forgefrontier.player;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.statistics.*;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemValues;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import net.minecraft.stats.Stat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum.ORDERED_STAT_ENUMS;

/**
 * FFPlayer
 *
 * The representation of a Player which contains extra data necessary for play on the ForgeFrontier server
 */
public class FFPlayer {

    private Map<StatEnum, Integer> stats;

    public enum StatEquipmentType {
        BASE(null),
        ASCEND(null),
        LEVEL(null),
        HELMET(EquipmentSlot.HEAD),
        CHESTPLATE(EquipmentSlot.CHEST),
        LEGGINGS(EquipmentSlot.LEGS),
        BOOTS(EquipmentSlot.FEET);

        EquipmentSlot equipmentSlot;

        StatEquipmentType(EquipmentSlot equipmentSlot) {
            this.equipmentSlot = equipmentSlot;
        }
        public static StatEquipmentType getInstance(EquipmentSlot slot) {
            for(StatEquipmentType type: StatEquipmentType.values()) {
                if(type.equipmentSlot == slot)
                    return type;
            }
            return null;
        }
    }

    private Map<StatEquipmentType, StatHolder> equipmentStats;

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

        UUID playerID = player.getUniqueId();
        this.playerID = playerID;

        this.stats = new HashMap<>();

        StatHolder baseStatHolder = new StatHolder();
        baseStatHolder.addStat(new BaseStatistic(20, StatEnum.HP));
        baseStatHolder.addStat(new BaseStatistic(1, StatEnum.ATK));
        baseStatHolder.addStat(new BaseStatistic(0, StatEnum.STR));
        baseStatHolder.addStat(new BaseStatistic(0, StatEnum.DEX));
        baseStatHolder.addStat(new BaseStatistic(0, StatEnum.CRATE));
        baseStatHolder.addStat(new BaseStatistic(0, StatEnum.CDMG));
        baseStatHolder.addStat(new BaseStatistic(0, StatEnum.DEF));

        this.equipmentStats = new HashMap<>();
        for(StatEquipmentType type: StatEquipmentType.values()) {
            this.equipmentStats.put(type, new StatHolder());
        }

        this.equipmentStats.put(StatEquipmentType.BASE, baseStatHolder);

        this.recalculateStats();

        this.tier = 0;
        this.ascensionLevel = 0;

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().getExistingPlayerStats(playerID, (statsMap) -> {
            if (statsMap != null) {

                if(statsMap.get("base_stats") != null) {
                    baseStatHolder.clear();
                    JSONWrapper wrapper = new JSONWrapper((String) statsMap.get("base_stats"));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("HP"), StatEnum.HP));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("ATK"), StatEnum.ATK));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("STR"), StatEnum.STR));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("DEX"), StatEnum.DEX));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("CRATE"), StatEnum.CRATE));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("CDMG"), StatEnum.CDMG));
                    baseStatHolder.addStat(new BaseStatistic(wrapper.getInt("DEF"), StatEnum.DEF));
                }

                this.equipmentStats.put(StatEquipmentType.BASE, baseStatHolder);
                this.recalculateStats();

                this.currentHealth = (Double) statsMap.get("current_health");
                this.tier = (Integer) statsMap.get("Tier");
                this.ascensionLevel = (Integer) statsMap.get("AscensionLevel");
            }
        });
    }

    public Map<StatEnum, Integer> getStats() {
        return this.stats;
    }

    public StatHolder getStats(StatEquipmentType type) {
        return this.equipmentStats.get(type);
    }

    /**
     * Updates the ForgeFrontier current health value when the player respawns
     */
    public void respawn() {
        currentHealth = this.get(StatEnum.HP);
    }

    public void setLevelStats(int value) {
        StatHolder holder = new StatHolder();
        for(StatEnum statEnum: StatEnum.values()) {
            holder.addStat(new BaseStatistic(value, statEnum));
        }
        this.equipmentStats.put(StatEquipmentType.LEVEL, holder);
        this.recalculateStats();
    }

    /**
     * Updates the player's statistics based on the new armor it equips
     *
     * @param armorInstance the instance of the custom armor being equipped
     */
    public void updateStatsOnArmorEquip(CustomArmor.CustomArmorInstance armorInstance) {

        StatEquipmentType type = StatEquipmentType.getInstance(armorInstance.getEquipmentSlot());
        StatHolder statHolder = new StatHolder();

        BaseStatistic[] baseStatistics = armorInstance.getBaseStats();
        ReforgeStatistic[] reforgeStatistics = armorInstance.getReforgeStats();
        GemValues[] gemValues = armorInstance.getGems();

        for (BaseStatistic baseStatistic : baseStatistics) {
            statHolder.addStat(baseStatistic);
        }
        for (ReforgeStatistic reforgeStatistic : reforgeStatistics) {
            statHolder.addStat(reforgeStatistic);
        }
        for (GemValues gemValue : gemValues) {
            if (gemValue != null) {
                statHolder.addStat(gemValue.getStat());
            }
        }

        this.equipmentStats.put(type, statHolder);
        this.recalculateStats();

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(this);
    }

    /**
     * Updates the player's statistics based on the armor it dequips
     *
     * @param armorInstance the instance of the custom armor being dequipped
     */
    public void updateStatsOnArmorDequip(CustomArmor.CustomArmorInstance armorInstance) {

        StatEquipmentType type = StatEquipmentType.getInstance(armorInstance.getEquipmentSlot());

        StatHolder statHolder = new StatHolder();
        this.equipmentStats.put(type, statHolder);
        this.recalculateStats();

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(this);
    }

    /**
     * Returns the calculated outgoing damage when using a custom weapon
     *
     * @param weaponInstance the custom weapon instance that is being used
     * @return the calculated outgoing damage
     */
    public double getOutgoingDamageOnAttack(CustomWeapon.CustomWeaponInstance weaponInstance) {
        StatHolder weaponStatHolder = new StatHolder();

        if(weaponInstance == null) {
            return StatCalc.calcOutgoingDamage(this, weaponStatHolder, StatEnum.STR);
        }

        BaseStatistic[] baseStatistics = weaponInstance.getBaseStats();
        ReforgeStatistic[] reforgeStatistics = weaponInstance.getReforgeStats();
        GemValues[] gemValues = weaponInstance.getGems();

        for (BaseStatistic baseStatistic : baseStatistics) {
            weaponStatHolder.addStat(baseStatistic);
        }
        for (ReforgeStatistic reforgeStatistic : reforgeStatistics) {
            weaponStatHolder.addStat(reforgeStatistic);
        }
        for (GemValues gemValue : gemValues) {
            if (gemValue != null) {
                weaponStatHolder.addStat(gemValue.getStat());
            }
        }

        return StatCalc.calcOutgoingDamage(this, weaponStatHolder, weaponInstance.getMainStat());
    }

    /**
     * Ascends the player, returning their tier to zero, and giving them more base stats
     */
    public void ascend() {
        ascensionLevel++;

        Player player = ForgeFrontier.getInstance().getPlayerManager().getPlayersByUUID().get(playerID);
        int level = player.getLevel();
        int tier = getTier();

        StatHolder holder = this.equipmentStats.get(StatEquipmentType.BASE);
        for(StatEnum statEnum: ORDERED_STAT_ENUMS) {
            holder.addStat(new BaseStatistic(level / 10 + tier, statEnum));
        }
        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerBaseStats(this);

        player.setLevel(0);
        player.setExp(0);
        setTier(0);

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updatePlayerStats(this);
    }

    private void recalculateStats() {
        for(StatEnum statEnum: ORDERED_STAT_ENUMS) {
            this.stats.put(statEnum, 0);
        }
        for(StatEquipmentType type: StatEquipmentType.values()) {
            StatHolder holder = this.equipmentStats.get(type);
            //ForgeFrontier.getInstance().getLogger().info(type + ": " + holder.toString());
            for(StatEnum statEnum: ORDERED_STAT_ENUMS) {
                this.stats.put(statEnum, this.stats.get(statEnum) + holder.get(statEnum));
            }
        }
    }

    public int get(StatEnum statEnum) {
        return stats.get(statEnum);
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
        List<String> stringList = new ArrayList<>();
        for (StatEnum statEnum: ORDERED_STAT_ENUMS) {
            stringList.add(statEnum.getFriendlyName() + ": " + stats.get(statEnum) + statEnum.getSuffix());
        }
        return String.join("\n", stringList);
    }

    public long getNextSkillTime() {
        return nextSkillTime;
    }

    public void setNextSkillTime(long nextSkillTime) {
        this.nextSkillTime = nextSkillTime;
    }

}
