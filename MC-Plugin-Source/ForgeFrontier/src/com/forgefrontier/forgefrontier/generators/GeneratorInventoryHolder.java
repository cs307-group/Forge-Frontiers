package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.ConfirmationHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class GeneratorInventoryHolder extends BaseInventoryHolder {
    GeneratorInstance generatorInstance;
    int updateTaskId;
    public GeneratorInventoryHolder(GeneratorInstance generatorInstance) {
        super(27);
        this.generatorInstance = generatorInstance;

        this.fillPanes();
        this.setCollectItem();
        this.addHandler(9+2, (e) -> {
            generatorInstance.collect((Player) e.getWhoClicked());
        });
        ItemStack removeItem = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = removeItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Remove Generator");

        Inventory baseInventory = this.getInventory();
        this.addHandler(9+6, (e) -> {
            ConfirmationHolder confirmHolder = new ConfirmationHolder("Are you sure you want to pickup this generator?", baseInventory, () -> {
                ForgeFrontier.getInstance().getGeneratorManager().removeGeneratorInstance(generatorInstance);
            });
            e.getWhoClicked().openInventory(confirmHolder.getInventory());
        });
    }

    public void setCollectItem() {
        ItemStack collectItem = new ItemStack(generatorInstance.generator.primaryMaterial.representation);
        ItemMeta collectItemMeta = collectItem.getItemMeta();
        collectItemMeta.setDisplayName(ChatColor.YELLOW + "Collect");
        collectItemMeta.setLore(Arrays.asList(
                ChatColor.RED + "Available Items: " + generatorInstance.getCollectAmt() + " / " + generatorInstance.getMaxAmt() + " " + generatorInstance.generator.primaryMaterial.name + "(s)"
        ));
        collectItem.setItemMeta(collectItemMeta);
        this.setItem(9+2, collectItem);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        int id = Bukkit.getScheduler().runTaskTimer(ForgeFrontier.getInstance(), () -> {
            setCollectItem();
        }, 0, generatorInstance.getGeneratorLevel().generatorRate * 20 / 1000).getTaskId();
        this.updateTaskId = updateTaskId;
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().cancelTask(this.updateTaskId);
    }


}
