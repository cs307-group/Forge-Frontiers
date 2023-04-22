package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import org.bukkit.Material;

public class SilverSword extends CustomWeapon{
        /**
         * Default constructor for the Silver  sword weapon
         */
        public SilverSword() {
            super ("SilverSword", "Silver Sword", QualityEnum.COMMON.getQuality(), 2, 1, Material.IRON_SWORD, Material.IRON_SWORD.getMaxDurability() - 1,
                    "&7A sharp sword forged from hardened &fSilver&7.");

            this.registerInstanceAccumulator((__, itemStack) -> {
                return new CustomWeaponInstance(itemStack);
            });
        }

}
