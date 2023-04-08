package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralCustomItem extends CustomItem {

    public static class GeneralCustomItemInstance extends CustomItemInstance {
        public GeneralCustomItemInstance(@Nullable ItemStack itemStack) {
            super(itemStack);
        }
    }

    /**
     * CustomItem constructor
     * <p>
     * Defines the attributes of a base custom item
     * and registers the appropriate accumulators to set those attributes.
     *
     * @param code A string that is used to identify the type of CustomItem an item is
     */
    public GeneralCustomItem(String itemId, String materialName, String nameRaw, String loreRaw) {
        super(itemId);
        Material material = Material.matchMaterial(materialName);
        String name = ChatColor.translateAlternateColorCodes('&', nameRaw);
        List<String> lore = Arrays.asList(ChatColor.translateAlternateColorCodes('&', loreRaw).split("\n"));
        this.registerInstanceAccumulator((__, itemStack) -> {
            return new CustomItemInstance(itemStack);
        });
        this.registerItemStackAccumulator((inst, __) -> {
            return new ItemStackBuilder(material)
                .setDisplayName(name)
                .setFullLore(lore)
                .build();
        });
    }

}
