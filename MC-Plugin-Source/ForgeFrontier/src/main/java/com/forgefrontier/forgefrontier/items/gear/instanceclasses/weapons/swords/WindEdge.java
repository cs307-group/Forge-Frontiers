package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import org.bukkit.Material;

public class WindEdge extends CustomWeapon {

    public WindEdge() {
        super ("WindEdge", "Wind's Edge", QualityEnum.UNIQUE.getQuality(), 2, 3,
                Material.IRON_HOE, Material.IRON_HOE.getMaxDurability() - 1,
                "The &fWind's Edge&7 is a razor-sharp blade that\n" +
                        "&7slices through the air with grace and speed.");

        this.registerInstanceAccumulator((__, itemStack) -> {
            return new CustomWeaponInstance(itemStack);
        });
    }


}
