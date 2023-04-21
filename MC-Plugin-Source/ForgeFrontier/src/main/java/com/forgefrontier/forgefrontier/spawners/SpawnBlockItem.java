package com.forgefrontier.forgefrontier.spawners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnBlockItem extends UniqueCustomItem {

    public SpawnBlockItem() {
        super("PlaceSpawnerBlock");

        // Builds new itemstack from scratch
        this.registerItemStackAccumulator((itemInstance, __) -> {
            // Places block instance in the world
            SpawnBlockItemInstance spawnBlockItemInstance = (SpawnBlockItemInstance) itemInstance;

            // Gets spawner from manager
            Spawner spawner = ForgeFrontier.getInstance().getSpawnerManager().getSpawner(spawnBlockItemInstance.entityCode);

            // Register metadata
            itemInstance.getData().put("entity-code", spawnBlockItemInstance.entityCode);
            itemInstance.getData().put("block", spawnBlockItemInstance.block);

            //Builds the item stack representation of the spawner
            return new ItemStackBuilder(Material.matchMaterial(spawnBlockItemInstance.block))
                    .setDisplayName("&aSpawner&7 - Entity &f" + (spawnBlockItemInstance.entityCode))
                    .addLoreLine("&7Place this spawner down into")
                    .addLoreLine("&7the world to continually spawn entities.")
                    .build();
        });

        // builds itemstack from metadata
        this.registerInstanceAccumulator((__, itemStack) -> {
            // Define new instance of Spawner item
            SpawnBlockItemInstance inst = new SpawnBlockItemInstance(itemStack);
            if(itemStack == null) {
                inst.entityCode = "";
                inst.block = "BARRIER";
            } else {
                inst.entityCode = (String) inst.getData().get("entity-code");
                inst.block = (String) inst.getData().get("block");
            }
            return inst;
        });
    }

    /**
     * Defines interaction behavior. Places spawner block in world
     */
    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance instance) {
        if(e.isCancelled())
            return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        Location newLocation = e.getClickedBlock().getLocation().add(e.getBlockFace().getDirection());
        if(!e.getClickedBlock().getWorld().getBlockAt(newLocation).isEmpty()) {
            return;
        }
        e.setCancelled(true);
        // Gets instance of spawner item
        SpawnBlockItemInstance spawnBlockItemInstance = (SpawnBlockItemInstance) instance;

        // Gets the Spawner form the manager
        Spawner spawner = ForgeFrontier.getInstance().getSpawnerManager().getSpawner(spawnBlockItemInstance.entityCode);

        // Ensures that spawner pulled from manager is valid, otherwise fill space
        if(spawner == null) {
            e.getPlayer().getInventory().setItem(e.getHand(), instance.asItemStack());
            e.getPlayer().sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to place down this spawner. It doesn't seem to exist.");
            return;
        }

        // Creates new spawner instance based on location of placed spawner block
        SpawnerInstance spawnerInstance = new SpawnerInstance(spawner, newLocation);

        // sets the level of the instance to the same as the item (since they are placed in the same world)
        spawnerInstance.entityCode = spawnBlockItemInstance.entityCode;

        // Initializes the spawner instance
        boolean success = ForgeFrontier.getInstance().getSpawnerManager().addSpawnerInstance(spawnerInstance);
        if(!success) {
            e.getPlayer().sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to place spawner here.");
            return;
        }
        //e.getItem().setAmount(e.getItem().getAmount() - 1);
        spawnerInstance.getLocation().getBlock().setMetadata("spawner-block", new FixedMetadataValue(ForgeFrontier.getInstance(), spawnerInstance.getId()));
        spawnerInstance.getLocation().getBlock().setType(Material.matchMaterial(spawnBlockItemInstance.block));

        ForgeFrontier.getInstance().getSpawnerManager().save();

    }
}
