package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.logging.Level;

public class FFRecipe {
    public enum SLOT_META {
        ANY, VANILLA, METAVANILLA, CUSTOM
    }
    ItemStack[] recipeItems;
    SLOT_META[] recipeMeta;
    ItemStack result;
    int baseOutAmount;

    public FFRecipe(ItemStack[] recipeItems, ItemStack result) {
        this.recipeItems = recipeItems;
        this.recipeMeta = new SLOT_META[9];
        Arrays.fill(recipeMeta,SLOT_META.ANY);
        this.result = result;
        this.baseOutAmount = result.getAmount();
    }


    public boolean isMatch(ItemStack[] input) {
        // Compare each slot, check if match
        for (int i = 0; i < 9; i++) {
            switch(recipeMeta[i]) {
                case ANY -> { if (!matchAny(input[i],recipeItems[i])) return false; }
                case CUSTOM -> { if (!matchCustom(input[i],recipeItems[i])) return false; }
                case VANILLA -> { if (!matchVanilla(input[i],recipeItems[i])) return false; }
                case METAVANILLA -> { if (!matchMetaVanilla(input[i],recipeItems[i])) return false; }
            }
        }
        return true;
    }

    private boolean matchAny(ItemStack input, ItemStack rItem) {
        //ForgeFrontier.getInstance().getLogger().log(Level.INFO,"Matching: " + a + " - " + b);
        if (input == null || input.getType() == Material.AIR) {
            return (rItem == null || rItem.getType() == Material.AIR);
        }
        if (rItem == null || rItem.getType() == Material.AIR) return false;

        return (input.getType() == rItem.getType() && input.getAmount() >= rItem.getAmount());  // just compare types
    }
    private boolean matchVanilla(ItemStack input, ItemStack recipeItem) {
        if (input == null || input.getType() == Material.AIR) {
            return (recipeItem == null || recipeItem.getType() == Material.AIR);
        }
        if (recipeItem == null || recipeItem.getType() == Material.AIR) return false;
        CustomItemInstance ci = CustomItemManager.asCustomItemInstance(input);
        if (ci != null) return false;   // dont accept custom items
        return (input.getType() == recipeItem.getType() && input.getAmount() >= recipeItem.getAmount());  // just compare types
    }

    private boolean matchMetaVanilla(ItemStack input, ItemStack recipeItem) {
        if (input == null && (recipeItem == null || recipeItem.getType() == Material.AIR)) return true;
        if (input != null && input.getType() == Material.AIR && (recipeItem == null || recipeItem.getType() == Material.AIR)) return true;
        if (input == null) return false;

        CustomItemInstance ci = CustomItemManager.asCustomItemInstance(input);
        if (ci != null) return false;   // dont accept custom items

        if (input.getType() != recipeItem.getType()) return false;
        ItemMeta inMeta = input.getItemMeta();
        ItemMeta recMeta = recipeItem.getItemMeta();
        if (inMeta == null || recMeta == null) { return inMeta == recMeta; }

        // Check meta and display name
        if (!inMeta.getDisplayName().equals(recMeta.getDisplayName())) return false;
        if (!ItemUtil.getStringLore(input).equals(ItemUtil.getStringLore(recipeItem))) return false;
        return (input.getAmount() >= recipeItem.getAmount());
    }

    private boolean matchCustom(ItemStack input, ItemStack recipeItem) {
        CustomItemInstance ci = CustomItemManager.asCustomItemInstance(input);
        CustomItemInstance cr = CustomItemManager.asCustomItemInstance(recipeItem);
        if (ci == null || cr == null) return ci == cr;
        return (ci.getBaseItem().getCode().equals(cr.getBaseItem().getCode())
                && input.getAmount() >= recipeItem.getAmount());
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
        return result.clone();
    }

    public int getCraftableAmount(ItemStack[] items) {
        int baseAmount = result.getAmount();

        int total = 64 / baseAmount;
        for (int i = 0; i < 9; i++) {
            if (recipeItems[i] == null || recipeItems[i].getType() == Material.AIR) continue;
            total = Math.min((items[i].getAmount() / recipeItems[i].getAmount()), total);
        }
        return total;
    }


    public ItemStack getItemComponent(int i) { return recipeItems[i]; }

    public ItemStack[] getComponents() { return this.recipeItems; }
    public void setResult(ItemStack result) {
        this.result = result; this.baseOutAmount = result.getAmount();
    }

    public static boolean isSame(FFRecipe a, FFRecipe b) {
        ItemStack[] acomp = a.getComponents();
        return b.isMatch(acomp);
    }


    public int getOutAmount() {
        return baseOutAmount;
    }
}
