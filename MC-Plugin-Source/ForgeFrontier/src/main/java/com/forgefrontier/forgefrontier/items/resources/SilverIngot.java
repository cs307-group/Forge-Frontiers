package com.forgefrontier.forgefrontier.items.resources;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SilverIngot extends CustomItem {

    /**
     * CustomItem constructor
     * <p>
     * Defines the attributes of a base custom item
     * and registers the appropriate accumulators to set those attributes.
     *
     * @param code A string that is used to identify the type of CustomItem an item is
     */
    public SilverIngot() {
        super("SilverIngot");
        this.registerInstanceAccumulator((__, itemStack) -> new CustomItemInstance(itemStack));
        this.registerItemStackAccumulator((inst, __) -> {
            return new ItemStackBuilder(Material.IRON_INGOT)
                    .setDisplayName("&7Silver Ingot")
                    .addLoreLine("&8Ingots of silver from deep beneath the earth.")
                    .build();
        });
    }

}
