package com.forgefrontier.forgefrontier.tutorial;

import org.bukkit.configuration.ConfigurationSection;

public class TaskStep {

    private TaskStepType type;
    private String quickDescription;
    private String fullDescription;
    private String completionMessage;
    private ConfigurationSection data;

    public TaskStep(ConfigurationSection section) {
        this.type = TaskStepType.valueOf(section.getString("type"));
        this.quickDescription = section.getString("quick-description");
        this.fullDescription = section.getString("full-description").replace("\\n", "\n");
        this.completionMessage = section.getString("completion-message").replace("\\n", "\n");
        this.data = section.getConfigurationSection("data");
    }

    public TaskStepType getType() {
        return type;
    }

    public String getQuickDescription() {
        return quickDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getCompletionMessage() {
        return completionMessage;
    }

    public ConfigurationSection getData() {
        return data;
    }
}
