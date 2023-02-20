package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.GearItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Wooden sword item class has all functionality of GearItem
 */
public class WoodenSword extends GearItem {

    /**
     * Instance class extends GearItemInstance for the WoodenSword class
     */
    public static class WoodenSwordInstance extends GearItemInstance {}

    /**
     * Default constructor for the wooden sword weapon
     */
    public WoodenSword() {
        super ("WoodenSword", QualityEnum.UNASSIGNED.getQuality(), 2, 1,
                GemEnum.WEAPON, Material.WOODEN_SWORD, Material.WOODEN_SWORD.getMaxDurability(),
                "A handy wooden sword, crafted with oak", WoodenSwordInstance.class);
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
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance) {
        //TODO: update outgoing damage values based on stats
    }
}
