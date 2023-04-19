package com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.chestpiece;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LeatherChestplate extends CustomArmor {

    /**
     * Instance class extends GearItemInstance for the WoodenSword class
     */
    public static class LeatherChestplateInstance extends CustomArmorInstance {
        public LeatherChestplateInstance(ItemStack itemStack) {
            super(itemStack, EquipmentSlot.CHEST);
        }
    }

    /** Constructor for the leather helmet */
    public LeatherChestplate() {
        super ("LeatherChestplate", QualityEnum.UNASSIGNED.getQuality(), 2, 1,
                GemEnum.ARMOR, Material.LEATHER_CHESTPLATE, Material.LEATHER_CHESTPLATE.getMaxDurability(),
                "A scrappy leather chest piece");

        this.registerInstanceAccumulator((__, itemStack) -> {
            return new LeatherChestplateInstance(itemStack);
        });
    }

}
