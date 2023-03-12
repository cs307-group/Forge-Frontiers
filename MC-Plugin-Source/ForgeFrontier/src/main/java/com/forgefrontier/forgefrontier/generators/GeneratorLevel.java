package com.forgefrontier.forgefrontier.generators;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class GeneratorLevel {

    public int maxSize;
    public int generatorRate;
    /** the tier required of the player to upgrade the generator */
    public int requiredTier;
    public List<MaterialDrop> rareAdditionalDrops;
    public List<MaterialCost> upgradeCosts;

    public GeneratorLevel(int generatorRate, int maxSize) {
        this.generatorRate = generatorRate;
        this.maxSize = maxSize;
        this.rareAdditionalDrops = new ArrayList<>();
        this.upgradeCosts = null;
        this.requiredTier = 0;
    }


    public GeneratorLevel(ConfigurationSection configSection) {

        this.generatorRate = configSection.getInt("generation_rate");
        this.maxSize = configSection.getInt("max_size");
        this.requiredTier = configSection.getInt("req_tier");

        int costInd = 0;
        ConfigurationSection costSection;
        while((configSection = configSection.getConfigurationSection("upgrade_costs." + costInd)) != null) {
            if(this.upgradeCosts == null)
                this.upgradeCosts = new ArrayList<>();
            this.upgradeCosts.add(new MaterialCost(configSection));
            costInd += 1;
        }
    }
}
