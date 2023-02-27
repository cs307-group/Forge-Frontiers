package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlaceGeneratorItem extends CustomItem {

    Generator generator;

    public PlaceGeneratorItem(Generator generator) {
        super("GenPlace-" + generator.getId());
        this.generator = generator;

        this.registerItemStackAccumulator((itemInstance, __) -> {
            return new ItemStackBuilder(generator.getMaterialRepresentation())
                    .setDisplayName(generator.getFriendlyName() + "&7 - Lvl &f" + itemInstance.getData().get("level"))
                    .addLoreLine("&7Place this generator down into the world to start generating materials.")
                    .build();
        });

        this.registerInstanceAccumulator((__, itemStack) -> {
            CustomItemInstance inst = new CustomItemInstance(itemStack);
            if(itemStack == null)
                inst.getData().put("level", 1);
            return inst;
        });

    }

    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance instance) {
        if(e.isCancelled())
            return;
        Location newLocation = e.getClickedBlock().getLocation().add(e.getBlockFace().getDirection());
        Block block = e.getClickedBlock().getWorld().getBlockAt(newLocation);
        if(!e.getClickedBlock().getWorld().getBlockAt(newLocation).isEmpty()) {
            return;
        }
        block.setType(e.getItem().getType());
        e.setCancelled(true);
        if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.getItem().setAmount(e.getItem().getAmount() - 1);
        GeneratorInstance generatorInstance = new GeneratorInstance(generator, newLocation);
        ForgeFrontier.getInstance().getGeneratorManager().addGeneratorInstance(generatorInstance);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance inst) {
    }

}
