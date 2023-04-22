package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import org.bukkit.Material;

public class CobaltSword extends CustomWeapon {




    public CobaltSword() {
        super ("CobaltSword", "Cobalt Sword", QualityEnum.RARE.getQuality(), 4,
                1, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD.getMaxDurability() - 1,
                "&7Pointy stick of &9Cobalt");
        this.registerInstanceAccumulator((__, itemStack) -> {
            return new CustomWeapon.CustomWeaponInstance(itemStack);
        });
    }




}
