package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class RecipeListGUI extends BaseInventoryHolder {
    private boolean prevTable;
    // Slot Exit
    private static final int SEX = 9 * 5 + 4;

    /**
     * Opens up list of recipes
     * @param prevTable on close, open back to table?
     */
    public RecipeListGUI(boolean prevTable) {
        super(54, "Recipe List");
        ArrayList<FFRecipe> recipes = ForgeFrontier.getInstance().getCraftingManager().getRecipes();
        int n = Math.min(recipes.size(), 45);
        for (int i = 0; i < n; i++) {
            this.setItem(i,recipes.get(i).getResult());

            int finalI = i;
            this.addHandler(i, (e) -> recipeClick(e, recipes.get(finalI)));

        }
        ItemStack nothing = new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build();
        ItemStack redbar = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build();
        ItemStack exit = new ItemStackBuilder(Material.BARRIER)
                                .setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Exit").build();

        for (int i = n; i < 45; i++) {
            this.setItem(i,nothing);
        }
        for (int i = 45; i < 54; i++) {
            this.setItem(i,redbar);
        }
        this.setItem(SEX, exit);
        this.addHandler(SEX,this::onExitHandler);
        this.prevTable = prevTable;
    }

    public void onExitHandler(InventoryClickEvent e) {
        if (prevTable) {
            e.getWhoClicked().openInventory(new CraftingGUI().getInventory());
        } else {
            e.getWhoClicked().closeInventory();
        }
    }
    public void recipeClick(InventoryClickEvent e, FFRecipe ffRecipe) {
        e.getWhoClicked().openInventory(new SingleRecipeViewer(ffRecipe, prevTable).getInventory());
    }







}
