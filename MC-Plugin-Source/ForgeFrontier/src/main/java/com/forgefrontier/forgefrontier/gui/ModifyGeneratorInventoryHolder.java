package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorLevel;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ModifyGeneratorInventoryHolder extends BaseInventoryHolder {

    private Generator generator;

    public ModifyGeneratorInventoryHolder(Generator generator) {
        super(54, "Modify Generator");

        this.generator = generator;

        this.setItem(9 + 2, new ItemStackBuilder(Material.OAK_SIGN)
            .setDisplayName("&bSet Friendly Name")
            .addLoreLine("&fCurrent Name&7: " + generator.getFriendlyName())
            .build());
        this.addHandler(9 + 2, (e) -> {
            new AnvilGUI.Builder()
                .onClose((player) ->
                        new PreviousInventoryRunnable(player, this.getInventory())
                                .runTaskLater(ForgeFrontier.getInstance(),5))
                .onComplete((completion -> {
                    generator.setFriendlyName(completion.getText());
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                }))
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter Friendly Name")
                .text(generator.getFriendlyName())
                .plugin(ForgeFrontier.getInstance())
                .open((Player) e.getWhoClicked());
        });
        this.addHandler(9 + 4, (e) -> {
            if(e.getCursor() == null || e.getCursor().getType() == Material.AIR)
                return;
            if(!e.getCursor().getType().isBlock())
                return;
            generator.setBlockMaterial(e.getCursor().getType());
            this.getInventory();
        });
        this.addHandler(9 + 6, (e) -> {
            if(e.getCursor() == null || e.getCursor().getType() == Material.AIR)
                return;
            ItemStack item = e.getCursor();
            CustomItem customItem = CustomItemManager.getCustomItem(item);
            if(customItem == null)
                return;
            generator.setPrimaryMaterial(customItem);
            this.getInventory();
        });
        this.addHandler(9 * 4 + 7, (e) -> {
            int index = this.generator.getGeneratorLevels().size();
            this.generator.getGeneratorLevels().add(new GeneratorLevel(10000, 256));
            e.getWhoClicked().openInventory(new ModifyGeneratorLevelInventoryHolder(generator, index).getInventory());
        });
        for(int i = 0; i < generator.getGeneratorLevels().size(); i++) {
            final int index = i;
            this.addHandler(9 * 3 + 1 + i, (e) -> {
                e.getWhoClicked().openInventory(new ModifyGeneratorLevelInventoryHolder(generator, index).getInventory());
            });
        }
    }

    @Override
    public Inventory getInventory() {
        this.inventory.clear();
        this.fillPanes();
        this.setItem(9 + 2, new ItemStackBuilder(Material.OAK_SIGN)
            .setDisplayName("&bSet Friendly Name")
            .addLoreLine("&fCurrent Name&7: " + generator.getFriendlyName())
            .build());

        this.setItem(9 + 4, new ItemStackBuilder(generator.getMaterialRepresentation())
            .setDisplayName("&bBlock Material")
            .addLoreLine("&7Click with a new block to change the block.")
            .build());
        this.setItem(9 + 6, new ItemStackBuilder(generator.getPrimaryMaterial().getRepresentation().getType())
            .setDisplayName("&bResource Generated &f- " + generator.getPrimaryMaterial().getRepresentation().getItemMeta().getDisplayName())
            .addLoreLine("&7Click with a new item to change the resource.")
            .build());

        for(int i = 0; i < 7; i++){
            this.setItem(9 * 3 + 1 + i, new ItemStack(Material.AIR));
        }
        int i = 0;
        for(GeneratorLevel level: generator.getGeneratorLevels()) {
            ItemStackBuilder builder = new ItemStackBuilder(generator.getMaterialRepresentation())
                .setDisplayName("&7Level &f" + (i + 1))
                    .addLoreLine("&7Generation Rate: &f" + level.getGeneratorRate())
                    .addLoreLine("&7Max Size: &f" + level.getMaxSize())
                    .addLoreLine("&8Costs: ");

            for(MaterialCost cost: level.getUpgradeCosts()) {
                builder.addLoreLine("&7>> &f" + cost.getAmount() + "&7x " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName());
            }
            this.setItem(9 * 3 + 1 + i, builder.build());
            i += 1;
        }
        this.setItem(9 * 4 + 7, new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE)
            .setDisplayName("&aAdd new Generator Level")
            .build());

        return this.inventory;
    }

}
