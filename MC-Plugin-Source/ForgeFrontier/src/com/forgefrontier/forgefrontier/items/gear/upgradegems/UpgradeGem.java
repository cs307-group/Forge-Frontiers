package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import com.forgefrontier.forgefrontier.items.gear.quality.InvalidMaxQualityException;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import com.forgefrontier.forgefrontier.items.gear.quality.QualityEnum;
import com.forgefrontier.forgefrontier.items.gear.statistics.BaseStatistic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class UpgradeGem extends UniqueCustomItem {

    /**
     * The Constructor for an UpgradeGem instance which randomly decides the statistic associated with the gem and
     * the quality associated with the gem.
     */
    public UpgradeGem() {
        super("UpgradeGem");

        // Register accumulator to set the base attributes of the CustomItemInstance
        // itemStack could be null, or could be an existing UpgradeGem you wish to get the instance of.
        this.registerInstanceAccumulator((__, itemStack) -> {

            // Because at the start the instance will be null, it must be instantiated here.
            UpgradeGemInstance gemInstance = new UpgradeGemInstance(itemStack);
            // Set the attributes relevant to it being specifically the UpgradeGem.
            if (itemStack == null) {
                // Set default value for a brand new UpgradeGem.
                QualityEnum myEnum = QualityEnum.getRandQualityEnum();
                gemInstance.gemValues.quality = myEnum.getQuality();
                gemInstance.gemValues.stat = new BaseStatistic(0, gemInstance.gemValues.quality.getMaxValue());
                gemInstance.gemValues.gemType = GemEnum.getRandGemEnum();
                gemInstance.setGemData();
            } else {

                // Pulls the data from the upgrade-gem-data portion of the data hashmap to specify attributes
                JSONObject data = gemInstance.getData();
                gemInstance.gemData = (HashMap<String, String>) data.get("upgrade-gem-data");
                assert(gemInstance.gemData != null);
                gemInstance.setAttributes();
            }

            // Return it to rise up to the accumulator for UniqueCustomItem (giving it a unique id)
            return gemInstance;
        });

        // Register an accumulator for creating the UpgradeGem item from the UpgradeGem Custom Item Instance.
        this.registerItemStackAccumulator((customItemInstance, itemStack) -> {
            /*
            "customItemInstance" is only a CustomItemInstance, it must be cast before specific UpgradeGem attributes
            can be accessed. Because this accumulator is being run, it is guaranteed it is a safe cast, because all
            UpgradeGem Custom Items will be of this instance (or a subclass).
            */
            UpgradeGemInstance gemInstance = (UpgradeGemInstance) customItemInstance;
            // Create the actual ItemStack.
            ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Upgrade Gem");
            // Set the data based off the item's attributes.
            meta.setLore(
                    Arrays.asList(
                            ChatColor.GRAY + "Can be applied to your " + gemInstance.gemValues.gemType.toString()
                                    + " for stat increases",
                            ChatColor.YELLOW + gemInstance.gemValues.stat.toString(),
                            gemInstance.gemValues.quality.getColor() + gemInstance.gemValues.quality.toString()
                    )
            );
            item.setItemMeta(meta);
            // Return the item to give it to the UniqueCustomItem's accumulator,
            // which will add the unique id to the ItemStack.

            gemInstance.getData().put("upgrade-gem-data", new JSONObject(gemInstance.gemData));

            return item;
        });

    }

    /**
     * The Constructor for an UpgradeGem instance which randomly decides the statistic associated with the gem, but
     * specifies the maximum quality possible for the gem.
     *
     * @param maxQuality the maximum possible quality that can be associated with the gem
     */
    public UpgradeGem(Quality maxQuality) {
        super("UpgradeGem");

        // Register accumulator to set the base attributes of the CustomItemInstance
        // itemStack could be null, or could be an existing UpgradeGem you wish to get the instance of.
        this.registerInstanceAccumulator((__, itemStack) -> {
            // Because at the start the instance will be null, it must be instantiated here.
            UpgradeGemInstance gemInstance = new UpgradeGemInstance(itemStack);
            // Set the attributes relevant to it being specifically the UpgradeGem.
            if (itemStack == null) {
                // Set default value for a brand new UpgradeGem.
                QualityEnum myEnum = QualityEnum.getRandQualityEnum(maxQuality.getRarityInt());
                gemInstance.gemValues.quality = myEnum.getQuality();
                gemInstance.gemValues.stat = new BaseStatistic(0, gemInstance.gemValues.quality.getMaxValue());
                gemInstance.gemValues.gemType = GemEnum.getRandGemEnum();
            } else {
                // TODO: Access ItemStack data and set based off that data.
            }
            // Return it to rise up to the accumulator for UniqueCustomItem (giving it a unique id)
            return gemInstance;
        });

        // Register an accumulator for creating the UpgradeGem item from the UpgradeGem Custom Item Instance.
        this.registerItemStackAccumulator((customItemInstance, itemStack) -> {
            /*
            "customItemInstance" is only a CustomItemInstance, it must be cast before specific UpgradeGem attributes
            can be accessed. Because this accumulator is being run, it is guaranteed it is a safe cast, because all
            UpgradeGem Custom Items will be of this instance (or a subclass).
            */
            UpgradeGemInstance gemInstance = (UpgradeGemInstance) customItemInstance;
            // Create the actual ItemStack.
            ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Upgrade Gem");
            // Set the data based off the item's attributes.
            meta.setLore(
                    Arrays.asList(
                            ChatColor.GRAY + "Can be applied to your " + gemInstance.gemValues.gemType.toString()
                                    + " for stat increases",
                            ChatColor.YELLOW + gemInstance.gemValues.stat.toString(),
                            gemInstance.gemValues.quality.getColor() + gemInstance.gemValues.quality.toString()
                    )
            );
            item.setItemMeta(meta);
            // Return the item to give it to the UniqueCustomItem's accumulator,
            // which will add the unique id to the ItemStack.
            return item;
        });
    }

    /**
     * Opens a GUI which allows the player to select a piece of equipment to apply the UpgradeGem to
     *
     * @param e the player interact event which describes the interact event
     * @param itemInstance the instance of the item which is being used to interact with
     */
    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            //TODO: opens GUI which then allows player to choose from all the
            //TODO: Weapons/Armor in their inventory and apply the gem
        }
    }

    /**
     * Can be left empty in this case
     *
     * @param e the object describing the event
     * @param itemInstance the instance of the item being used to attack
     */
    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance itemInstance) {

    }
}
