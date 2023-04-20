package com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor;

import com.forgefrontier.forgefrontier.items.gear.GearItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * CustomArmor class which acts as a definition to know that the item should be equip-able in the armor slots
 */
public abstract class CustomArmor extends GearItem {

    /** Instance class extending GearItemInstance to represent CustomArmor */
    public static class CustomArmorInstance extends GearItemInstance {

        EquipmentSlot equipmentSlot;

        /** calls the super constructor and sets the item slot of the item */
        public CustomArmorInstance(ItemStack itemStack, EquipmentSlot slot) {
            super(itemStack);
            this.equipmentSlot = slot;
        }

        /** returns the equipment slot of the armor */
        public EquipmentSlot getEquipmentSlot() {
            return equipmentSlot;
        }
    }

    /**
     * Constructor for a GearItem in which every field is specified. The Base stats and original ReforgeStatistics are
     * randomized, but the number of BaseStatistics and Gems can be specified
     *
     * @param name         the name of the piece of gear
     * @param quality      the quality of the piece of gear
     * @param numBaseStats the number of BaseStatistics the gear has
     * @param numGemSlots  the number of GemSlots the gear has
     * @param gemEnum      the type of gems that the gear allows to be equipped
     * @param material     the material used by the gear
     * @param durability   the durability of the gear (used for cosmetics)
     * @param lore         the lore description of the gear
     */
    public CustomArmor(String name, Quality quality, int numBaseStats, int numGemSlots, GemEnum gemEnum, Material material, int durability, String lore) {
        super(name, quality, numBaseStats, numGemSlots, gemEnum, material, durability, lore);

        this.registerInstanceAccumulator((instance, itemStack) -> {
            CustomArmorInstance armorInstance = (CustomArmorInstance) instance;

            if (itemStack == null) {
                BaseStatistic[] baseStats = new BaseStatistic[numBaseStats];
                armorInstance.setBaseStats(baseStats);
            }

            return armorInstance;
        });
    }
}
