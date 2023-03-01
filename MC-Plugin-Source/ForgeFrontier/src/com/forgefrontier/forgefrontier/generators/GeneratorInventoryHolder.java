package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GeneratorInventoryHolder extends BaseInventoryHolder {
    GeneratorInstance generatorInstance;
    int updateTaskId;
    public GeneratorInventoryHolder(GeneratorInstance generatorInstance) {
        super(27, generatorInstance.generator.getFriendlyName());
        this.generatorInstance = generatorInstance;

        this.fillPanes();
        this.setCollectItem();
        this.addHandler(9+2, (e) -> {
            generatorInstance.collect((Player) e.getWhoClicked());
            setCollectItem();
        });

        this.addHandler(9 + 4, (e) -> {
            e.getWhoClicked().openInventory(new GeneratorUpgradeInventoryHolder(generatorInstance).getInventory());
        });
        this.setItem(9 + 4, new ItemStackBuilder(Material.ANVIL).setDisplayName("&bView Generator Upgrades & Stats").build());

        ItemStack removeItem = new ItemStackBuilder(Material.RED_WOOL).setDisplayName(ChatColor.RED + "Remove Generator").build();
        this.setItem(9+6, removeItem);
        Inventory baseInventory = this.getInventory();
        this.addHandler(9+6, (e) -> {
            ConfirmationHolder confirmHolder = new ConfirmationHolder("Are you sure you want to pickup this generator?", baseInventory, () -> {
                ForgeFrontier.getInstance().getGeneratorManager().removeGeneratorInstance(generatorInstance);
                GeneratorPlaceItemInstance genPlaceItemInst = CustomItemManager.getCustomItem("GenPlace-" + generatorInstance.generator.getId()).asInstance(null);
                genPlaceItemInst.
                e.getWhoClicked().getInventory().addItem(.asInstance(null).asItemStack());
                e.getWhoClicked().closeInventory();
            }, false);
            e.getWhoClicked().openInventory(confirmHolder.getInventory());
        });
    }

    public void setCollectItem() {
        ItemStack item = new ItemStackBuilder(generatorInstance.generator.primaryMaterial.getRepresentation().getType())
            .setDisplayName(ChatColor.YELLOW + "Collect")
            .addLoreLine(ChatColor.RED + "Available Items: " + generatorInstance.getCollectAmt() + " / " + generatorInstance.getMaxAmt() + " " + generatorInstance.generator.primaryMaterial.getRepresentation().getItemMeta().getDisplayName() + "(s)")
            .setAmount(1)
            .build();
        this.setItem(9+2, item);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        int id = Bukkit.getScheduler().runTaskTimer(ForgeFrontier.getInstance(), () -> {
            setCollectItem();
        }, 0, generatorInstance.getGeneratorLevel().generatorRate * 20L / 1000).getTaskId();
        this.updateTaskId = id;
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().cancelTask(this.updateTaskId);
    }


}
