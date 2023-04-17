package com.forgefrontier.forgefrontier.items;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GeneralCustomSkullItem extends CustomItem {


        public static class GeneralCustomSkullItemInstance extends CustomItemInstance {
            public GeneralCustomSkullItemInstance(@Nullable ItemStack itemStack) {
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
        public GeneralCustomSkullItem(String itemId, String nameRaw, String loreRaw) {
            super(itemId);
            String name = ChatColor.translateAlternateColorCodes('&', nameRaw);
            List<String> lore = Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                    loreRaw).split("\n"));

            this.registerInstanceAccumulator((__, itemStack) -> {
                return new CustomItemInstance(itemStack);
            });
            this.registerItemStackAccumulator((inst, __) -> {
                ItemStack skull = ForgeFrontier.getInstance().getCustomSkullManager().getSkullItem(itemId);
                ItemMeta im = skull.getItemMeta();
                im.setDisplayName(name);
                im.setLore(lore);
                skull.setItemMeta(im);
                return skull;
            });
        }


}
