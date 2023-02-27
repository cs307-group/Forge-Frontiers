package com.forgefrontier.forgefrontier.items.gear.statistics;

import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.player.PlayerStat;

import java.util.ArrayList;

/**
 * Used a library class for functions relating to calculating values based off of statistics
 */
public class StatCalc {

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
