package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorUpgradeInventoryHolder extends BaseInventoryHolder {

    GeneratorInstance generatorInstance;
    public GeneratorUpgradeInventoryHolder(GeneratorInstance generatorInstance) {
        super(5*9, "Upgrade Generator");
        this.generatorInstance = generatorInstance;
        this.fillPanes();

        Generator generator = generatorInstance.generator;

        int i = 0;
        for(GeneratorLevel genLevel: generatorInstance.generator.getGeneratorLevels()) {
            this.setItem(9+1+i,
                new ItemStackBuilder(generator.getMaterialRepresentation())
                    .setDisplayName(generator.getFriendlyName() + "&7 - Lvl &f" + (i + 1))
                    .addLoreLine("&7Generation Rate: &f" + ((float) genLevel.generatorRate / 1000) + "&7 seconds per item")
                    .addLoreLine("&7Max Size: &f" + genLevel.maxSize)
                    .setShiny(i <= generatorInstance.level)
                    .build());
            i++;
        }
        for(; i < 7; i++) {
            this.setItem(9+1+i, new ItemStack(Material.AIR));
        }

        this.setItem(9 * 3 + 1, new ItemStackBuilder(Material.RED_WOOL)
            .setDisplayName("&cGo Back")
            .build()
        );
        this.addHandler(9 * 3 + 1, (e) -> {
            e.getWhoClicked().openInventory(new GeneratorInventoryHolder(generatorInstance).getInventory());
        });

        int level = generatorInstance.level;
        GeneratorLevel genLevel = generator.generatorLevels.get(level);
        this.setItem(9 * 3 + 4,
                new ItemStackBuilder(generator.getMaterialRepresentation())
                    .setDisplayName("&6Current Level: " + generator.getFriendlyName() + "&7 - Lvl &f" + (level+1))
                    .addLoreLine("&7Generation Rate: &f" + ((float) genLevel.generatorRate / 1000) + "&7 seconds per item")
                    .addLoreLine("&7Max Size: &f" + genLevel.maxSize)
                    .setShiny(i <= generatorInstance.level)
                    .build());
        if(generator.getGeneratorLevels().get(generatorInstance.level).upgradeCosts == null) {
            this.setItem(9 * 3 + 7, new ItemStackBuilder(Material.GREEN_WOOL)
                    .setDisplayName("&cYou have reached the max level.").build());
            return;
        }
        ItemStackBuilder buyItemBuilder = new ItemStackBuilder(Material.GREEN_WOOL)
            .setDisplayName("&aBuy Upgrade");
        for(MaterialCost cost: generator.getShopCost()) {
            buyItemBuilder.addLoreLine("&7>> &f" + cost.getAmount() + "&7x " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName());
        }
        this.setItem(9 * 3 + 7, buyItemBuilder.build());
        final List<MaterialCost> costs = generator.getGeneratorLevels().get(generatorInstance.level).upgradeCosts;
        this.addHandler(9 * 3 + 7, (e) -> {
            boolean works = true;
            for(MaterialCost cost: costs) {
                if(!cost.hasBalance((Player) e.getWhoClicked())) {
                    works = false;
                    break;
                }
            }
            if(!works) {
                this.replaceItemTemporarily(9 * 3 + 7, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to buy this item.").build());
                e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to buy this upgrade.");
                return;
            }
            for(MaterialCost cost: costs) {
                cost.take((Player) e.getWhoClicked());
            }
            generatorInstance.upgrade();
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully upgraded the generator.");
        });

    }

}