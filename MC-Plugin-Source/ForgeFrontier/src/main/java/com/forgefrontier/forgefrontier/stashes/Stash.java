package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.GeneratorLevel;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Stash {

    String id;
    String friendlyName;
    Material materialRepresentation;
    List<MaterialCost> shopCosts;
    List<StashItem> stashItems;

    public Stash(ConfigurationSection config) {

        this.id = config.getString("id");
        this.friendlyName = config.getString("friendly_name");
        this.materialRepresentation = Material.valueOf(config.getString("representation_material"));
        this.shopCosts = new ArrayList<>();
        this.stashItems = new ArrayList<>();

        int costInd = 0;
        ConfigurationSection configSection;
        while((configSection = config.getConfigurationSection("costs." + costInd)) != null) {
            this.shopCosts.add(new MaterialCost(configSection));
            costInd += 1;
        }

        int itemInd = 0;
        while((configSection = config.getConfigurationSection("contents." + itemInd)) != null) {
            this.stashItems.add(new StashItem(configSection));
            itemInd += 1;
        }
    }

    public Stash(String stashId, String friendlyName, Material blockMaterial, List<JSONWrapper> costs, List<JSONWrapper> contents) {
        this.id = stashId;
        this.friendlyName = friendlyName;
        this.materialRepresentation = blockMaterial;
        this.shopCosts = new ArrayList<>();
        for(JSONWrapper cost: costs) {
            this.shopCosts.add(new MaterialCost(cost));
        }
        for(JSONWrapper stashItem: contents) {
            this.stashItems.add(new StashItem(stashItem));
        }
    }

    public String getId() {
        return this.id;
    }

    public Material getMaterialRepresentation() {
        return this.materialRepresentation;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public List<MaterialCost> getShopCost() {
        return this.shopCosts;
    }

    public List<StashItem> getStashItems() {
        return this.stashItems;
    }

    public StashItem getStashItem(String code) {
        for(StashItem item: this.stashItems) {
            if(item.getItem().getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}

