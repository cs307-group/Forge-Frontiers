package com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.helmet;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Leather helmet custom armor
 */
public class LeatherHelmet extends CustomArmor {

    /**
     * Instance class extends GearItemInstance for the WoodenSword class
     */
    public static class LeatherHelmetInstance extends CustomArmorInstance {
        public LeatherHelmetInstance(ItemStack itemStack) {
            super(itemStack);
        }
    }

    /** Constructor for the leather helmet */
    public LeatherHelmet() {
        super ("LeatherHelmet", QualityEnum.UNASSIGNED.getQuality(), 2, 1,
                GemEnum.ARMOR, Material.LEATHER_HELMET, Material.LEATHER_HELMET.getMaxDurability(),
                "A scrappy leather helm");

        this.registerInstanceAccumulator((__, itemStack) -> {
            return new LeatherHelmetInstance(itemStack);
        });
    }
}
