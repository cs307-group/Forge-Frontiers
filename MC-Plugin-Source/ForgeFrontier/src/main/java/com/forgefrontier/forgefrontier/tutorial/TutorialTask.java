package com.forgefrontier.forgefrontier.tutorial;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TutorialTask {

    private Material itemMaterial;
    private String name;
    private String description;
    private List<TaskStep> stepList;

    public TutorialTask(ConfigurationSection config) {
        this.itemMaterial = Material.matchMaterial(config.getString("material"));
        this.name = config.getString("name");
        this.description = config.getString("description");
        this.stepList = new ArrayList<>();

        int taskInd = 0;
        ConfigurationSection section;
        while((section = config.getConfigurationSection("steps." + taskInd)) != null) {
            this.stepList.add(new TaskStep(section));
            taskInd += 1;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStep getStep(int currentStepIndex) {
        return this.stepList.get(currentStepIndex);
    }

    public int getStepAmt() {
        return this.stepList.size();
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public List<TaskStep> getSteps() {
        return this.stepList;
    }
}
