package com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor;

import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import org.bukkit.Material;

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
    public CustomArmorCreator(String code, String name, Quality quality, int numBaseStats, int numGemSlots, GemEnum gemEnum, Material material, int durability, String lore) {
        super(code, name, quality, numBaseStats, numGemSlots, gemEnum, material, durability, lore);
    }
}
