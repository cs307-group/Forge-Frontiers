package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.materials.CustomMaterial;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MaterialCost {

    CustomMaterial material;
    int amount;

    public MaterialCost(ConfigurationSection configSection) {
        this.material = ForgeFrontier.getInstance().getGeneratorManager().getCustomMaterial(
            configSection.getString("material_type"),
            configSection.getString("item_id")
        );
        this.amount = configSection.getInt("amount");
    }

    public MaterialCost(JSONWrapper jsonWrapper) {
        this.material = ForgeFrontier.getInstance().getGeneratorManager().getCustomMaterial(
                jsonWrapper.getString("material_type"),
                jsonWrapper.getString("item_id")
        );
        this.amount = jsonWrapper.getInt("amount");
    }

    public CustomMaterial getMaterial() {
        return this.material;
    }

    public boolean hasBalance(Player p) {
        return this.material.hasBalance(p, this.amount);
    }

    public void take(Player p) {
        this.material.take(p, this.amount);
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return "(material: " + material + ", amount: " + amount + ")";
    }

}
