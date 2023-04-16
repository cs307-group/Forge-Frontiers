package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorLevel;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
                        level.generatorRate = Integer.parseInt(completion.getText());
                    } catch (NumberFormatException exception) {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(level.generatorRate)));
                    }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                }))
                .text(Integer.toString(level.generatorRate))
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter Generator Level").text(generator.getFriendlyName())
                .plugin(ForgeFrontier.getInstance())
                .open((Player) e.getWhoClicked());
        });

    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
            e.getPlayer().openInventory(new ModifyGeneratorInventoryHolder(generator).getInventory());
        });
    }

    @Override
    public Inventory getInventory() {
        this.inventory.clear();
        this.fillPanes();

        GeneratorLevel level = generator.getGeneratorLevels().get(levelIndex);

        this.setItem(9 + 2, new ItemStackBuilder(Material.DIAMOND_PICKAXE)
                .setDisplayName("&bGenerator Rate: &7" + level.generatorRate)
                .addLoreLine("&7Click to set the generator rate.")
                .build());

        return this.inventory;
    }

}
