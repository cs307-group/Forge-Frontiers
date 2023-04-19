package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorInstance;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlaceStashItem extends UniqueCustomItem {

    public PlaceStashItem() {
        super("PlaceStashBlock");

        this.registerItemStackAccumulator((itemInstance, __) -> {
            PlaceStashItemInstance instance = (PlaceStashItemInstance) itemInstance;
            Stash stash = ForgeFrontier.getInstance().getStashManager().getStash(instance.stashId);

            itemInstance.getData().put("stash-id", instance.stashId);

            if(stash == null) {
                return new ItemStackBuilder(Material.CHEST)
                    .setDisplayName("&dUnknown Stash")
                    .build();
            }

            return new ItemStackBuilder(stash.getMaterialRepresentation())
                    .setDisplayName(stash.getFriendlyName())
                    .addLoreLine("&7Place this stash down into the world to store resources.")
                    .build();
        });

        this.registerInstanceAccumulator((__, itemStack) -> {
            PlaceStashItemInstance inst = new PlaceStashItemInstance(itemStack);
            if(itemStack == null) {
                inst.stashId = "";
            } else {
                inst.stashId = (String) inst.getData().get("stash-id");
            }
            return inst;
        });

    }

    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance) {
        if(e.isCancelled())
            return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        Location newLocation = e.getClickedBlock().getLocation().add(e.getBlockFace().getDirection());
        if(!e.getClickedBlock().getWorld().getBlockAt(newLocation).isEmpty()) {
            return;
        }
        e.setCancelled(true);
        PlaceStashItemInstance instance = (PlaceStashItemInstance) itemInstance;
        Stash stash = ForgeFrontier.getInstance().getStashManager().getStash(instance.stashId);
        StashInstance stashInstance = new StashInstance(stash, newLocation);

        ForgeFrontier.getInstance().getStashManager().initializeStashInstance(stashInstance, (success) -> {
            Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
                if(!success) {
                    e.getPlayer().sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to place stash here. You must place it on your island.");
                    return;
                }
                //if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                stashInstance.getLocation().getBlock().setType(stashInstance.stash.getMaterialRepresentation());
            });
        });
    }

}
