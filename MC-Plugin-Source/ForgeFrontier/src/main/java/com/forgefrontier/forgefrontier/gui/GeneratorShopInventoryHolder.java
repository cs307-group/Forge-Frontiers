package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItem;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.stashes.PlaceStashItemInstance;
import com.forgefrontier.forgefrontier.stashes.Stash;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorShopInventoryHolder extends BaseInventoryHolder {

    ForgeFrontier plugin;

    public GeneratorShopInventoryHolder() {
        super(54, "Generator Shop");

        this.fillPanes();

        for(int x = 0; x < 7; x++) {
            for(int y = 0; y < 2; y++) {
                int slotId = (x + 1) + (y+1) * 9;
                this.setItem(slotId, new ItemStack(Material.AIR));
            }
            int slotId = (x + 1) + 4 * 9;
            this.setItem(slotId, new ItemStack(Material.AIR));
        }

        this.plugin = ForgeFrontier.getInstance();

        List<Generator> shopGenerators = this.plugin.getGeneratorManager().getShopMenuList();

        int i = 0;
        for(Generator generator: shopGenerators) {
            final int slotId = 9 + 1 + i + (i / 7) * 9;
            ItemStackBuilder builder =
                    new ItemStackBuilder(generator.getMaterialRepresentation())
                            .setDisplayName(generator.getFriendlyName());
            for(MaterialCost cost: generator.getShopCost()) {
                builder.addLoreLine("&7>> &f" + cost.getAmount() + "&7x " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName());
            }
            this.setItem(slotId, builder.build());
            final List<MaterialCost> costs = generator.getShopCost();
            this.addHandler(slotId, (e) -> {
                if(e.getWhoClicked().getInventory().firstEmpty() == -1) {
                    this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to space in your inventory.").build());
                    e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to space in your inventory.");
                    return;
                }
                boolean works = true;
                for(MaterialCost cost: costs) {
                    if(!cost.hasBalance((Player) e.getWhoClicked())) {
                        works = false;
                        break;
                    }
                }
                if(!works) {
                    this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to buy this item.").build());
                    e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to buy this item.");
                    return;
                }
                for(MaterialCost cost: costs) {
                    cost.take((Player) e.getWhoClicked());
                }
                PlaceGeneratorItemInstance placeGeneratorItemInstance = (PlaceGeneratorItemInstance) CustomItemManager.getCustomItem("PlaceGeneratorBlock").asInstance(null);
                placeGeneratorItemInstance.setGeneratorData(generator.getId(), 0);
                ItemStack generatorItem = placeGeneratorItemInstance.asItemStack();
                e.getWhoClicked().getInventory().addItem(generatorItem);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully bought the " + generatorItem.getItemMeta().getDisplayName() + ".");
            });
            i++;
        }



        List<Stash> shopStashes = this.plugin.getStashManager().getStashShopList();

        i = 0;
        for(Stash stash: shopStashes) {
            final int slotId = 4 * 9 + 1 + i;
            ItemStackBuilder builder =
                    new ItemStackBuilder(stash.getMaterialRepresentation())
                            .setDisplayName(stash.getFriendlyName());
            for(MaterialCost cost: stash.getShopCost()) {
                builder.addLoreLine("&7>> &f" + cost.getAmount() + "&7x " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName());
            }
            this.setItem(slotId, builder.build());
            final List<MaterialCost> costs = stash.getShopCost();
            this.addHandler(slotId, (e) -> {
                if(e.getWhoClicked().getInventory().firstEmpty() == -1) {
                    this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to space in your inventory.").build());
                    e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to space in your inventory.");
                    return;
                }
                boolean works = true;
                for(MaterialCost cost: costs) {
                    if(!cost.hasBalance((Player) e.getWhoClicked())) {
                        works = false;
                        break;
                    }
                }
                if(!works) {
                    this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to buy this item.").build());
                    e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to buy this item.");
                    return;
                }
                for(MaterialCost cost: costs) {
                    cost.take((Player) e.getWhoClicked());
                }
                PlaceStashItemInstance placeStashItemInstance = (PlaceStashItemInstance) CustomItemManager.getCustomItem("PlaceStashBlock").asInstance(null);
                placeStashItemInstance.setStashId(stash.getId());
                ItemStack stashItem = placeStashItemInstance.asItemStack();
                e.getWhoClicked().getInventory().addItem(stashItem);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully bought the " + stashItem.getItemMeta().getDisplayName() + ".");
            });
            i++;
        }


    }

}
