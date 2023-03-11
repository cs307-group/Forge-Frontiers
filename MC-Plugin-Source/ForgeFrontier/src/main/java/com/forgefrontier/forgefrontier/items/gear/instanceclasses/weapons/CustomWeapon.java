package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.GearItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomWeapon extends GearItem {

    /**
     * Class for CustomSwordInstance
     */
    public abstract static class CustomWeaponInstance extends GearItemInstance {

        StatEnum MAIN_STAT = StatEnum.STR;

        public CustomWeaponInstance(ItemStack itemStack) {
            super(itemStack);
        }

        public StatEnum getMainStat() {
            return MAIN_STAT;
        }
    }

    /**
     * Constructor for a GearItem in which every field is specified. The Base stats and original ReforgeStatistics are
     * randomized, but the number of BaseStatistics and Gems can be specified
     *
     * @param name         the name of the piece of gear
     * @param quality      the quality of the piece of gear
     * @param numBaseStats the number of BaseStatistics the gear has
     * @param numGemSlots  the number of GemSlots the gear has
     * @param material     the material used by the gear
     * @param durability   the durability of the gear (used for cosmetics)
     * @param lore         the lore description of the gear
     */
    public CustomWeapon(String name, Quality quality, int numBaseStats, int numGemSlots, Material material, int durability, String lore) {
        super(name, quality, numBaseStats, numGemSlots, GemEnum.WEAPON, material, durability, lore);
    }

    /**
     * Calculates Outgoing damage based upon player stats
     *
     * @param e the event details
     * @param itemInstance the instance used
     * @param ffPlayer the ffPlayer using the weapon
     */
    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance, FFPlayer ffPlayer) {
        double damage = ffPlayer.getOutgoingDamageOnAttack((CustomWeaponInstance) itemInstance);
        e.setDamage(damage);
    }
}
