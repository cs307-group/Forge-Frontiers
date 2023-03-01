package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceGeneratorItem extends UniqueCustomItem {


    public static class PlaceGeneratorItemInstance extends UniqueCustomItemInstance {

        String generatorId;
        int level;

        public PlaceGeneratorItemInstance(ItemStack itemStack) {
            super(itemStack);
        }
    }

    public PlaceGeneratorItem() {
        super("PlaceGeneratorBlock");

        this.registerItemStackAccumulator((itemInstance, __) -> {
            PlaceGeneratorItemInstance placeGeneratorItemInstance = (PlaceGeneratorItemInstance) itemInstance;
            Generator generator = ForgeFrontier.getInstance().getGeneratorManager().getGenerator(placeGeneratorItemInstance.generatorId);
            return new ItemStackBuilder(generator.getMaterialRepresentation())
                    .setDisplayName(generator.getFriendlyName() + "&7 - Lvl &f" + (placeGeneratorItemInstance.level + 1))
                    .addLoreLine("&7Place this generator down into the world to start generating materials.")
                    .build();
        });

        this.registerInstanceAccumulator((__, itemStack) -> {
            PlaceGeneratorItemInstance inst = new PlaceGeneratorItemInstance(itemStack);
            if(itemStack == null) {
                inst.generatorId = "";
                inst.level = 0;
            } else {
                inst.generatorId = (String) inst.getData().get("generator-id");
                inst.level = (int) inst.getData().get("level");
            }
            return inst;
        });

    }

    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance instance) {
        if(e.isCancelled())
            return;
        Location newLocation = e.getClickedBlock().getLocation().add(e.getBlockFace().getDirection());
        if(!e.getClickedBlock().getWorld().getBlockAt(newLocation).isEmpty()) {
            return;
        }
        e.setCancelled(true);
        if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.getItem().setAmount(e.getItem().getAmount() - 1);
        PlaceGeneratorItemInstance placeGeneratorItemInstance = (PlaceGeneratorItemInstance) instance;
        Generator generator = ForgeFrontier.getInstance().getGeneratorManager().getGenerator(placeGeneratorItemInstance.generatorId);
        GeneratorInstance generatorInstance = new GeneratorInstance(generator, newLocation);
        ForgeFrontier.getInstance().getGeneratorManager().addGeneratorInstance(generatorInstance);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e, CustomItemInstance inst, FFPlayer ffPlayer) {
    }

}
