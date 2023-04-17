package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorLevel;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.generators.materials.CoinMaterial;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ModifyGeneratorLevelInventoryHolder extends BaseInventoryHolder {

    Generator generator;
    int levelIndex;

    public ModifyGeneratorLevelInventoryHolder(Generator generator, int levelIndex) {
        super(54, "Modify Generator Level");
        this.generator = generator;
        this.levelIndex = levelIndex;
        GeneratorLevel level = generator.getGeneratorLevels().get(levelIndex);

        this.addHandler(9 + 2, (e) -> {
            new AnvilGUI.Builder()
                .onClose((player) ->
                    new PreviousInventoryRunnable(player, this.getInventory())
                        .runTaskLater(ForgeFrontier.getInstance(),5))
                .onComplete((completion -> {
                    try {
                        level.setGeneratorRate(Integer.parseInt(completion.getText()));
                    } catch (NumberFormatException exception) {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(level.getGeneratorRate())));
                    }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                }))
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter Generation Rate")
                .text(Integer.toString(level.getGeneratorRate()))
                .plugin(ForgeFrontier.getInstance())
                .open((Player) e.getWhoClicked());
        });

        this.addHandler(9 + 4, (e) -> {
            new AnvilGUI.Builder()
                .onClose((player) ->
                    new PreviousInventoryRunnable(player, this.getInventory())
                        .runTaskLater(ForgeFrontier.getInstance(),5))
                .onComplete((completion -> {
                    try {
                        level.setMaxSize(Integer.parseInt(completion.getText()));
                    } catch (NumberFormatException exception) {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(level.getGeneratorRate())));
                    }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                }))
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter Level Max Size")
                .text(Integer.toString(level.getMaxSize()))
                .plugin(ForgeFrontier.getInstance())
                .open((Player) e.getWhoClicked());
        });

        this.addHandler(9 + 6, (e) -> {
            level.insertCost();
            this.getInventory();
        });

        this.addHandler(9 * 5, (e -> {
            generator.getGeneratorLevels().remove(levelIndex);
            e.getWhoClicked().openInventory(new ModifyGeneratorInventoryHolder(this.generator).getInventory());
        }));

        this.addHandler(9 * 5 + 8, (e -> {
            e.getWhoClicked().openInventory(new ModifyGeneratorInventoryHolder(this.generator).getInventory());
        }));

    }

    /*
    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
            e.getPlayer().openInventory(new ModifyGeneratorInventoryHolder(generator).getInventory());
        });
    }
     */

    @Override
    public Inventory getInventory() {
        this.inventory.clear();
        this.fillPanes();

        GeneratorLevel level = generator.getGeneratorLevels().get(levelIndex);

        for(int i = 0; i < level.getUpgradeCosts().size(); i++) {
            MaterialCost cost = level.getUpgradeCosts().get(i);
            final int index = i;
            this.addHandler(9 * 3 + 1 + i, (e) -> {
                if(e.getCursor() == null || e.getCursor().getType() == Material.AIR) {
                    level.getUpgradeCosts().set(index, new MaterialCost("coin", "", cost.getAmount()));
                    this.getInventory();
                    return;
                }
                CustomItem item = CustomItemManager.getCustomItem(e.getCursor());
                if(item == null)
                    return;
                level.getUpgradeCosts().set(index, new MaterialCost("item", item.getCode(), cost.getAmount()));
                this.getInventory();
            });
            this.addHandler(9 * 4 + 1 + i, (e) -> {
                new AnvilGUI.Builder()
                    .onClose((player) ->
                            new PreviousInventoryRunnable(player, this.getInventory())
                                    .runTaskLater(ForgeFrontier.getInstance(),5))
                    .onComplete((completion -> {
                        try {
                            cost.setAmount(Integer.parseInt(completion.getText()));
                        } catch (NumberFormatException exception) {
                            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(level.getGeneratorRate())));
                        }
                        return Arrays.asList(AnvilGUI.ResponseAction.close());
                    }))
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("Enter Cost Amount")
                    .text(Integer.toString(cost.getAmount()))
                    .plugin(ForgeFrontier.getInstance())
                    .open((Player) e.getWhoClicked());
            });
        }

        this.setItem(9 + 2, new ItemStackBuilder(Material.DIAMOND_PICKAXE)
            .setDisplayName("&bGenerator Rate: &f" + level.getGeneratorRate())
            .addLoreLine("&7Click to set the generator rate.")
            .build());

        this.setItem(9 + 4, new ItemStackBuilder(Material.CHEST)
            .setDisplayName("&bMax Size: &f" + level.getMaxSize())
            .addLoreLine("&7Click to set the max size.")
            .build());

        this.setItem(9 + 6, new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE)
            .setDisplayName("&aInsert new Upgrade Cost")
            .build());

        for(int i = 0; i < 4; i++) {
            this.setItem(9 * 3 + 1 + i, new ItemStack(Material.AIR));
        }

        int i = 0;
        for(MaterialCost cost: level.getUpgradeCosts()) {
            this.setItem(9 * 3 + 1 + i, new ItemStackBuilder(cost.getMaterial().getRepresentation().getType())
                .setDisplayName("&aCost Item: " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName())
                .addLoreLine("&7Amount: &f" + cost.getAmount())
                .addLoreLine("&7Click with new item to set to the given item.")
                .addLoreLine("&7Click with an empty cursor to set it to use coins.")
                .build());
            this.setItem(9 * 4 + 1 + i, new ItemStackBuilder(Material.PAPER)
                .setDisplayName("&aSet Cost Amount")
                .addLoreLine("&7Current Amount: &f" + cost.getAmount())
                .build());
            i += 1;
        }

        this.setItem(9 * 5, new ItemStackBuilder(Material.BARRIER)
                .setDisplayName("&cDelete Level")
                .build());
        this.setItem(9 * 5 + 8, new ItemStackBuilder(Material.IRON_TRAPDOOR)
                .setDisplayName("&aSave & Go Back")
                .build());


        return this.inventory;
    }

}
