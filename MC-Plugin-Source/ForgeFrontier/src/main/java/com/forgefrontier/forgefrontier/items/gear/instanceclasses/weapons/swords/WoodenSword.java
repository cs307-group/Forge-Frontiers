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
     * Default constructor for the wooden sword weapon
     */
    public WoodenSword() {
        super ("WoodenSword", "Wooden Sword", QualityEnum.UNASSIGNED.getQuality(), 2, 1, Material.WOODEN_SWORD, Material.WOODEN_SWORD.getMaxDurability() - 1,
                "A handy wooden sword, crafted with oak");

        this.registerInstanceAccumulator((__, itemStack) -> {
            return new CustomWeaponInstance(itemStack);
        });
    }

}
