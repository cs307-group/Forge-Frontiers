package com.forgefrontier.forgefrontier.shop;

import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.gear.GearItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class GearShopEntry {

    GearItem gearItem;
    GearItemInstance baseInstance;
    List<MaterialCost> costs;

    public GearItem getGearItem() {
        return gearItem;
    }

    public GearItemInstance getBaseInstance() {
        return baseInstance;
    }

    public List<MaterialCost> getCosts() {
        return costs;
    }

    public GearShopEntry(ConfigurationSection config) {

        this.gearItem = (GearItem) CustomItemManager.getCustomItem(config.getString("custom_item_id"));
        this.baseInstance = (GearItemInstance) this.gearItem.asInstance(null);

        this.costs = new ArrayList<>();
        int costInd = 0;
        ConfigurationSection configSection;
        while((configSection = config.getConfigurationSection("costs." + costInd)) != null) {
            this.costs.add(new MaterialCost(configSection));
            costInd += 1;
        }
    }

}
