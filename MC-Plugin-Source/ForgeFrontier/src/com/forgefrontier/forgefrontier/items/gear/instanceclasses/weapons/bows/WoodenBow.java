package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.bows;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.GearItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords.WoodenSword;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Wooden bow item class has all functionality of GearItem
 */
public class WoodenBow extends GearItem {

    /**
     * Instance class extends GearItemInstance for the WoodenBow class
     */
    public static class WoodenBowInstance extends GearItemInstance {};

    /**
     * Default constructor for the wooden bow weapon
     */
    public WoodenBow() {
        super ("WoodenBow", QualityEnum.UNASSIGNED.getQuality(), 3, 3,
                GemEnum.WEAPON, Material.BOW, Material.BOW.getMaxDurability(),
                "A handy wooden bow, crafted with oak", WoodenBow.WoodenBowInstance.class);
    }

    /**
     * Leave blank, no functionality for this class
     *
     * @param e event holding information regarding it
     * @param itemInstance the item instance that called this event
     */
    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance) {

    }

    /**
     * Calculates outgoing damage
     *
     * @param e event holding infomration regarding it
     * @param itemInstance the item instance that called this event
     */
    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance) {
        //TODO: calculate outgoing damage
    }
}
