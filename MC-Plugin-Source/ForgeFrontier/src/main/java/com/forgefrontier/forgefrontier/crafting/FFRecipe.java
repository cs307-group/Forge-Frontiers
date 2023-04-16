package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.logging.Level;

public class FFRecipe {
    public enum SLOT_META {
        ANY, VANILLA, CUSTOM
    }
    ItemStack[] recipeItems;
    SLOT_META[] recipeMeta;
    ItemStack result;
    public FFRecipe(ItemStack[] recipeItems, ItemStack result) {
        this.recipeItems = recipeItems;
        this.recipeMeta = new SLOT_META[9];
        Arrays.fill(recipeMeta,SLOT_META.ANY);
        this.result = result;
    }


    public boolean isMatch(ItemStack[] input) {
        // Compare each slot, check if match
        for (int i = 0; i < 9; i++) {
            if (recipeMeta[i] == SLOT_META.ANY) {
                if (!matchAny(input[i],recipeItems[i])) return false;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean matchAny(ItemStack a, ItemStack b) {
        ForgeFrontier.getInstance().getLogger().log(Level.INFO,"Matching: " + a + " - " + b);
        if (a == null && (b == null || b.getType() == Material.AIR)) return true;
        if (a != null && a.getType() == Material.AIR && (b == null || b.getType() == Material.AIR)) return true;
        if (a == null) return false;
        return a.getType() == b.getType();  // just compare types
    }

    public ItemStack[] getRecipeItems() {
        return recipeItems;
    }

    public void setRecipeItems(ItemStack[] recipeItems) {
        this.recipeItems = recipeItems;
    }

    public SLOT_META[] getRecipeMeta() {
        return recipeMeta;
    }

    public void setRecipeMeta(SLOT_META[] recipeMeta) {
        this.recipeMeta = recipeMeta;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }
}
