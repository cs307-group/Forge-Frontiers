package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
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

        //Inventory baseInventory = this.getInventory();
        //this.setItem(9+6, (e) -> {
            //BaseInventoryHolder baseHolder = new BaseInventoryHolder();
            //baseHolder.fillPanes();
            // Are you sure?
        //});
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
