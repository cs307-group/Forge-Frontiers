package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeneratorLevel {

    private int maxSize;
    private int generatorRate;
    /** the tier required of the player to upgrade the generator */
    private int requiredTier;
    private final List<MaterialCost> upgradeCosts;

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

    public int getMaxSize() {
        return maxSize;
    }

    public int getGeneratorRate() {
        return generatorRate;
    }

    public int getRequiredTier() {
        return requiredTier;
    }

    public List<MaterialCost> getUpgradeCosts() {
        return upgradeCosts;
    }

    public void setGeneratorRate(int generatorRate) {
        this.generatorRate = generatorRate;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setRequiredTier(int requiredTier) {
        this.requiredTier = requiredTier;
    }

    public void insertCost() {
        this.upgradeCosts.add(new MaterialCost("coin", "", 1000));
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("generation_rate", this.generatorRate);
        obj.put("max_size", this.maxSize);
        obj.put("req_tier", this.requiredTier);
        JSONArray upgradeArr = new JSONArray();
        for(MaterialCost cost: this.upgradeCosts) {
            upgradeArr.add(cost.toJSONObject());
        }
        obj.put("upgrade_costs", upgradeArr);
        return obj;
    }
}
