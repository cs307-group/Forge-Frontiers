package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.configuration.ConfigurationSection;

public class StashItem {

    CustomItem item;
    int maxAmount;
    public StashItem(ConfigurationSection config) {
        this.item = CustomItemManager.getCustomItem(config.getString("item_id"));
        this.maxAmount = config.getInt("max_amount");
    }

}
