package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlaceGeneratorItem extends CustomItem {

    Generator generator;

    public PlaceGeneratorItem(Generator generator) {
        super("GenPlace-" + generator.getCode());
        this.generator = generator;
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        if(e.isCancelled())
            return;
        Location newLocation = e.getClickedBlock().getLocation().add(e.getBlockFace().getDirection());
        Block block = e.getClickedBlock().getWorld().getBlockAt(newLocation);
        if(!e.getClickedBlock().getWorld().getBlockAt(newLocation).isEmpty()) {
            return;
        }
        e.setCancelled(true);
        if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.getItem().setAmount(e.getItem().getAmount() - 1);
        block.setType(Material.GOLD_BLOCK);
        GeneratorInstance generatorInstance = new GeneratorInstance(generator, newLocation);
        ForgeFrontier.getInstance().getGeneratorManager().addGeneratorInstance(generatorInstance);
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Diamond Generator");
        item.setItemMeta(meta);
        return item;
    }

}
