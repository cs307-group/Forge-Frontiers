package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.materials.CoinMaterial;
import com.forgefrontier.forgefrontier.generators.materials.CustomMaterial;
import com.forgefrontier.forgefrontier.generators.materials.ItemMaterial;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Generator {

    String id;
    String friendlyName;
    Material materialRepresentation;
    CustomMaterial primaryMaterial;
    List<MaterialCost> shopCost;
    List<GeneratorLevel> generatorLevels;

    public Generator(ConfigurationSection config) {
        this.id = config.getString("id");
        this.friendlyName = config.getString("friendly_name");
        this.materialRepresentation = Material.valueOf(config.getString("representation_material"));
        this.shopCost = new ArrayList<>();
        this.generatorLevels = new ArrayList<>();
        String materialType = config.getString("material_type");
        String itemId = config.getString("item_id");
        this.primaryMaterial = ForgeFrontier.getInstance().getGeneratorManager().getCustomMaterial(materialType, itemId);

        int costInd = 0;
        ConfigurationSection configSection;
        while((configSection = config.getConfigurationSection("costs." + costInd)) != null) {
            this.shopCost.add(new MaterialCost(configSection));
            costInd += 1;
        }

        int levelInd = 0;
        while((configSection = config.getConfigurationSection("levels." + levelInd)) != null) {
            this.generatorLevels.add(new GeneratorLevel(configSection));
            levelInd += 1;
        }
    }

    public Generator(String generatorId, String friendlyName, Material blockMaterial, CustomItem resourceItem, List<JSONWrapper> costs, List<JSONWrapper> levels) {
        this.id = generatorId;
        this.friendlyName = friendlyName;
        this.materialRepresentation = blockMaterial;
        this.primaryMaterial = ForgeFrontier.getInstance().getGeneratorManager().getCustomMaterial("item", resourceItem.getCode());
        this.shopCost = new ArrayList<>();
        for(JSONWrapper cost: costs) {
            this.shopCost.add(new MaterialCost(cost));
        }
        this.generatorLevels = new ArrayList<>();
        for(JSONWrapper level: levels) {
            this.generatorLevels.add(new GeneratorLevel(level));
        }
    }

    public String getId() {
        return id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public CustomMaterial getPrimaryMaterial() {
        return primaryMaterial;
    }

    public List<GeneratorLevel> getGeneratorLevels() {
        return generatorLevels;
    }

    public Material getMaterialRepresentation() {
        return materialRepresentation;
    }

    public List<MaterialCost> getShopCost() {
        return shopCost;
    }
}
