package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.GearItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Wooden bow item class has all functionality of GearItem
 */
public class WoodenBow extends CustomWeapon {

    /**
     * Instance class extends GearItemInstance for the WoodenBow class
     */
    public static class WoodenBowInstance extends CustomWeaponInstance {
        public WoodenBowInstance(ItemStack itemStack) {
            super(itemStack);
        }
    };

    /**
     * Default constructor for the wooden bow weapon
     */
    public WoodenBow() {
        super ("WoodenBow", QualityEnum.UNASSIGNED.getQuality(), 3, 3, Material.BOW, Material.BOW.getMaxDurability(),
                "A handy wooden bow, crafted with oak");

        this.registerInstanceAccumulator((__, itemStack) -> {
            return new WoodenBowInstance(itemStack);
        });
    }

    /**
     * Calculates outgoing damage
     *
     * @param e event holding infomration regarding it
     * @param itemInstance the item instance that called this event
     */
    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance, FFPlayer ffPlayer) {
        //TODO: calculate outgoing damage
    }

}
