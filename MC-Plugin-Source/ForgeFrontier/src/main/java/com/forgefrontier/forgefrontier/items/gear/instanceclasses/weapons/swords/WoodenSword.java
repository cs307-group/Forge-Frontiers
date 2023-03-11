package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Wooden sword item class has all functionality of GearItem
 */
public class WoodenSword extends CustomWeapon {

    /**
     * Instance class extends GearItemInstance for the WoodenSword class
     */
    public static class WoodenSwordInstance extends CustomWeaponInstance {
        public WoodenSwordInstance(ItemStack itemStack) {
            super(itemStack);
        }
    }

    /**
     * Default constructor for the wooden sword weapon
     */
    public WoodenSword() {
        super ("WoodenSword", QualityEnum.UNASSIGNED.getQuality(), 2, 1, Material.WOODEN_SWORD, Material.WOODEN_SWORD.getMaxDurability() - 1,
                "A handy wooden sword, crafted with oak");

        this.registerInstanceAccumulator((__, itemStack) -> {
            return new WoodenSwordInstance(itemStack);
        });
    }

    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance) {

    }

    /**
     * What occurs when the player attacks with the weapon
     *
     * @param e the event details
     * @param itemInstance the instance used
     */
    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance, FFPlayer ffPlayer) {
        super.onAttack(e, itemInstance, ffPlayer);
    }
}
