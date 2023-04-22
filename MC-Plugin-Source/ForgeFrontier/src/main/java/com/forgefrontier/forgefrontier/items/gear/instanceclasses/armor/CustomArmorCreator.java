package com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor;

import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

public class CustomArmorCreator extends CustomArmor {
    /**
     * Constructor for a GearItem in which every field is specified. The Base stats and original ReforgeStatistics are
     * randomized, but the number of BaseStatistics and Gems can be specified
     *
     * @param code
     * @param name         the name of the piece of gear
     * @param quality      the quality of the piece of gear
     * @param numBaseStats the number of BaseStatistics the gear has
     * @param numGemSlots  the number of GemSlots the gear has
     * @param gemEnum      the type of gems that the gear allows to be equipped
     * @param material     the material used by the gear
     * @param durability   the durability of the gear (used for cosmetics)
     * @param lore         the lore description of the gear
     */
    public CustomArmorCreator(String code, String name, Quality quality, int numGemSlots, Material material, String lore, EquipmentSlot equipSlot, int def, int hp) {
        super(code, name, quality, 2, numGemSlots, GemEnum.ARMOR, material, 0, lore);

        this.setRandomizeBaseStats(false);
        this.registerInstanceAccumulator((__, itemStack) -> {
            CustomArmorInstance instance = new CustomArmorInstance(itemStack, equipSlot);
            instance.setBaseStats(new BaseStatistic[] {
                new BaseStatistic(StatEnum.DEF, def),
                new BaseStatistic(StatEnum.HP, hp)
            });
            return instance;
        });
    }


    // For Leather items
    public CustomArmorCreator(String code, String name, Quality quality, int numGemSlots, Material material, Color col, String lore, EquipmentSlot equipSlot, int def, int hp) {
        super(code, name, quality, 2, numGemSlots, GemEnum.ARMOR, material, col, 0, lore);
        this.setRandomizeBaseStats(false);
        this.registerInstanceAccumulator((__, itemStack) -> {
            CustomArmorInstance instance = new CustomArmorInstance(itemStack, equipSlot);
            instance.setBaseStats(new BaseStatistic[] {
                    new BaseStatistic(StatEnum.DEF, def),
                    new BaseStatistic(StatEnum.HP, hp)
            });
            return instance;
        });
    }



}
