package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class GeneratorLevel {

    public int maxSize;
    public int generatorRate;
    /** the tier required of the player to upgrade the generator */
    public int requiredTier;
    public List<MaterialCost> upgradeCosts;

    public GeneratorLevel(int generatorRate, int maxSize) {
        this.generatorRate = generatorRate;
        this.maxSize = maxSize;
        this.upgradeCosts = new ArrayList<>();
        this.requiredTier = 0;
    }


    public GeneratorLevel(ConfigurationSection configSection) {

        this.generatorRate = configSection.getInt("generation_rate");
        this.maxSize = configSection.getInt("max_size");
        this.requiredTier = configSection.getInt("req_tier");
        this.upgradeCosts = new ArrayList<>();

        int costInd = 0;
        ConfigurationSection costSection;
        while((configSection = configSection.getConfigurationSection("upgrade_costs." + costInd)) != null) {
            this.upgradeCosts.add(new MaterialCost(configSection));
            costInd += 1;
        }
    }

    public GeneratorLevel(JSONWrapper jsonWrapper) {

        this.generatorRate = jsonWrapper.getInt("generation_rate");
        this.maxSize = jsonWrapper.getInt("max_size");
        this.requiredTier = jsonWrapper.getInt("req_tier");
        this.upgradeCosts = new ArrayList<>();

        for(JSONWrapper cost: jsonWrapper.getList("upgrade_costs")) {
            this.upgradeCosts.add(new MaterialCost(cost));
        }

    }

}
