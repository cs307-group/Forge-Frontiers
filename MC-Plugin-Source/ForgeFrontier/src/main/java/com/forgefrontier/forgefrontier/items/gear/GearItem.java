package com.forgefrontier.forgefrontier.items.gear;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import com.forgefrontier.forgefrontier.items.gear.instanceclasses.armor.CustomArmor;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import com.forgefrontier.forgefrontier.items.gear.statistics.ReforgeStatistic;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemValues;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * GearItem
 *
 * A class derivative of UniqueCustomItem which is used as a base to derive different custom gear types, which
 * each have their own statistics which affect the player who is equipping it. It is linked to its own custom appearance
 * as well.
 */
public abstract class GearItem extends UniqueCustomItem {

    /**
     * Constructor for a GearItem in which every field is specified. The Base stats and original ReforgeStatistics are
     * randomized, but the number of BaseStatistics and Gems can be specified
     *
     * @param name the name of the piece of gear
     * @param quality the quality of the piece of gear
     * @param numBaseStats the number of BaseStatistics the gear has
     * @param numGemSlots the number of GemSlots the gear has
     * @param gemEnum the type of gems that the gear allows to be equipped
     * @param material the material used by the gear
     * @param durability the durability of the gear (used for cosmetics)
     * @param lore the lore description of the gear
     */
    public GearItem(String name, Quality quality, int numBaseStats, int numGemSlots, GemEnum gemEnum,
                    Material material, int durability, String lore) {
        super(name);
        registerAccumulators(name, quality, numBaseStats, numGemSlots, gemEnum, material, durability, lore);
    }

    /**
     * Randomly initializes the base statistic slots for the piece of gear
     *
     * @param numBaseStats the number of specified base statistics the gear should contain
     */
    private void initBaseStatSlots(GearItemInstance instance, int numBaseStats) {
        // checks to see if baseStats has already been instantiated
        if (instance.baseStats == null) {
            instance.numBaseStats = numBaseStats;
            instance.baseStats = new BaseStatistic[numBaseStats];
        }

        // fills in all empty stats with random stats
        for (int i = 0; i < numBaseStats; i++) {
            if (instance.baseStats[i] == null) {
                instance.baseStats[i] = new BaseStatistic(1, instance.quality.getMaxValue(), instance.gemEnum);
            }
        }
    }

    /**
     * Registers the accumulators with the specified data
     *
     * @param name the name of the piece of gear
     * @param quality the quality of the piece of gear
     * @param numBaseStats the number of BaseStatistics the gear has
     * @param numGemSlots the number of GemSlots the gear has
     * @param gemEnum the type of gems that the gear allows to be equipped
     * @param material the material used by the gear
     * @param durability the durability of the gear (used for cosmetics)
     * @param lore the lore description of the gear
     */
    private void registerAccumulators(String name, Quality quality, int numBaseStats, int numGemSlots, GemEnum gemEnum,
                                      Material material, int durability, String lore) {
        this.registerInstanceAccumulator((instance, itemStack) -> {
            GearItemInstance gearItemInstance = (GearItemInstance) instance;

            if (itemStack == null) {
                gearItemInstance.gemEnum = gemEnum;

                gearItemInstance.name = name;
                if (quality.getRarityInt() == -1) {
                    gearItemInstance.quality = QualityEnum.getRandQualityEnum().getQuality();
                } else {
                    gearItemInstance.quality = quality;
                }

                gearItemInstance.reforgeStats = new ReforgeStatistic[3];
                for (int i = 0; i < 3; i++) {
                    gearItemInstance.reforgeStats[i] = new ReforgeStatistic(gearItemInstance.quality, gearItemInstance.gemEnum);
                }
                initBaseStatSlots(gearItemInstance, numBaseStats);

                gearItemInstance.numGemSlots = numGemSlots;
                gearItemInstance.gems = new GemValues[gearItemInstance.numGemSlots];

                gearItemInstance.material = material;
                gearItemInstance.durability = durability;

                gearItemInstance.lore = lore;
                gearItemInstance.setGearData();
            } else {
                // System.out.println("LOADING GEAR FROM PREVIOUS DATA");

                // Pulls the data from the gear-data portion of the data hashmap to specify attributes
                JSONObject data = gearItemInstance.getData();
                gearItemInstance.gearData = (HashMap<String, String>) data.get("gear-data");
                assert(gearItemInstance.gearData != null);
                gearItemInstance.setAttributes();

            }
            return gearItemInstance;
        });

        this.registerItemStackAccumulator((customItemInstance, itemStack) -> {
            GearItemInstance gearItemInstance = (GearItemInstance) customItemInstance;

            ItemStack item = new ItemStack(gearItemInstance.material);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(gearItemInstance.quality.getColor() + gearItemInstance.name);
            ArrayList<String> loreArr = new ArrayList<>();
            loreArr.add(ChatColor.GRAY + gearItemInstance.lore);
            loreArr.add("");
            loreArr.add(gearItemInstance.quality.getColor() + gearItemInstance.quality.toString());
            loreArr.add("");
            loreArr.add(ChatColor.WHITE + "Base Stats");
            for (int i = 0; i < gearItemInstance.numBaseStats; i++) {
                loreArr.add(gearItemInstance.quality.getColor() + gearItemInstance.baseStats[i].toString());
            }
            loreArr.add("");
            loreArr.add(ChatColor.WHITE + "Reforge Stats");
            for (int i = 0; i < 3; i++) {
                loreArr.add(gearItemInstance.quality.getColor() + gearItemInstance.reforgeStats[i].toString());
            }
            loreArr.add("");
            for (int i = 0; i < gearItemInstance.numGemSlots; i++) {
                if (gearItemInstance.gems[i] == null) {
                    loreArr.add(ChatColor.DARK_GRAY + "Empty Slot");
                } else {
                    loreArr.add(gearItemInstance.gems[i].getQuality().getColor() + gearItemInstance.gems[i].toString());
                }
            }
            meta.setLore(loreArr);

            // sets the durability of the item to the specified durability
            Damageable damageable = (Damageable) meta;
            damageable.setDamage(gearItemInstance.material.getMaxDurability() - gearItemInstance.durability);
            // System.out.println("durability: " + durability);

            // makes the item unbreakable
            meta.setUnbreakable(true);

            // set the item to do no damage
            Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
            if (modifiers != null) {
                modifiers.get(Attribute.GENERIC_ATTACK_DAMAGE).clear();
            }
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 0,
                    AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);

            // if the gear is armor, set the defense to 0
            if (gearItemInstance instanceof CustomArmor.CustomArmorInstance armorInstance) {
                if (modifiers != null) {
                    modifiers.get(Attribute.GENERIC_ARMOR).clear();
                }
                modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0,
                        AttributeModifier.Operation.ADD_NUMBER, armorInstance.getEquipmentSlot());
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
            }

            item.setItemMeta(damageable);

            // stores the attribute values in the gear-data hashmap
            gearItemInstance.getData().put("gear-data", new JSONObject(gearItemInstance.gearData));

            return item;
        });
    }
}