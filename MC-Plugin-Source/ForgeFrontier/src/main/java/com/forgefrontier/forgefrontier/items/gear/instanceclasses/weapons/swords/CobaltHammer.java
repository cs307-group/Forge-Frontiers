package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import org.bukkit.Material;

public class CobaltHammer extends CustomWeapon {

    public CobaltHammer() {
        super ("CobaltHammer", "Cobalt Hammer", QualityEnum.RARE.getQuality(), 4,
                1, Material.DIAMOND_AXE, Material.DIAMOND_AXE.getMaxDurability() - 1,
                "&7A &9Cobalt Hammer &7capable of delivering devastating blows.");
        this.registerInstanceAccumulator((__, itemStack) -> {
            return new CustomWeaponInstance(itemStack);
        });
    }

}
