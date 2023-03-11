package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.player.PlayerStat;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/**
 * Used a library class for functions relating to calculating values based off of statistics
 */
public class StatCalc {

    /**
     * Calculates the outgoing damage of a custom weapon attack
     *
     * @param ffPlayer the player issuing the attack
     * @return the updated damage
     */
    public static double calcOutgoingDamage(FFPlayer ffPlayer, StatEnum MAIN_STAT) {
        // System.out.println(ffPlayer.getStatsString());

        double damage = 0;
        // System.out.println(damage);
        int mainStatInt = StatEnum.getIntFromEnum(MAIN_STAT);

        damage += ffPlayer.getATK();
        // System.out.println(damage);
        switch (mainStatInt) {
            case 2: damage += ffPlayer.getSTR(); break;
            case 3: damage += ffPlayer.getDEX(); break;
            default: break;
        }
        // System.out.println(damage);

        if ((int) (Math.random() * 100) + 1 <= ffPlayer.getCRATE()) {
            double critDamage = ((double) ffPlayer.getCDMG()) / 100 * damage;
            damage += critDamage;
        }
        // System.out.println(damage);

        return damage;
    }

    /**
     * Returns the modified incoming damage based off of the defense statistic
     *
     * @param def the defense of the defending entity
     * @param damage the base damage value being done by the entity
     * @return the modified incoming damage value
     */
    public static double modifyIncomingDamage(double damage, int def) {
        if (def / damage > 0.8) {
            return damage * 0.2;
        }
        else {
            return damage * (1 - (def / damage));
        }
    }

    /**
     * Updates the current health of the ffPlayer and converts the damage specified by the parameter from the
     * ForgeFrontier value to the Minecraft value
     *
     * @param ffDamage the 'ForgeFrontier' amount of damage done to the player
     * @param mcMaxHealth the current 'Minecraft' max health of the player
     * @return the Minecraft amount of damage done to the player
     */
    public static double convertDamage(double ffDamage, double mcMaxHealth, FFPlayer ffPlayer) {
        double updatedHealth = ffPlayer.getCurrentHealth() - ffDamage;
        if (updatedHealth < 0) {
            ffPlayer.setCurrentHealth(0);
        }
        ffPlayer.setCurrentHealth(updatedHealth);
        return mcMaxHealth * (ffDamage / ffPlayer.getHP());
    }

    /**
     * Calculates and updates the total stat value based on its baseValue and percentages attributes
     *
     * @param stat the statistic to be updated
     * @return the calculated total stat value
     */
    public static double calculateTotalStat(PlayerStat stat) {
        ArrayList<Double> percentages = stat.getPercentages();

        double totalStat = 1;
        for (int i = 0; i < percentages.size(); i++) {
            double percentage = percentages.get(i);
            totalStat *= (1.0 - percentage);
        }
        totalStat = stat.getBaseValue() * (1.0 - totalStat);
        return stat.getBaseValue() + totalStat;
    }
}
