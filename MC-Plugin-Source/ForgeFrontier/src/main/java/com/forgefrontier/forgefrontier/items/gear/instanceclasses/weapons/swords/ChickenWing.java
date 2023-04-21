package com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.swords;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.weapons.CustomWeapon;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChickenWing extends CustomWeapon {

    /**
     * Constructor for a GearItem in which every field is specified. The Base stats and original ReforgeStatistics are
     * randomized, but the number of BaseStatistics and Gems can be specified
     */
    public ChickenWing() {
        super(
            "ChickenWing",
            "Chicken Wing",
            QualityEnum.UNIQUE.getQuality(),
            3,
            2,
            Material.CHICKEN,
            1000,
            "&7A chicken wing that seems to be imbued with something\n&7that makes it sharp and deadly."
        );

        this.setRandomizeBaseStats(false);

        this.registerInstanceAccumulator((__, itemStack) -> {
            CustomWeaponInstance instance = new CustomWeaponInstance(itemStack);
            instance.setBaseStats(new BaseStatistic[] {
                new BaseStatistic(StatEnum.ATK, 10),
                new BaseStatistic(StatEnum.CRATE, 25),
                new BaseStatistic(StatEnum.CDMG, 30)
            });
            return instance;
        });

    }
}
