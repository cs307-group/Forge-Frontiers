package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SingleRecipeViewer extends BaseInventoryHolder {
    // top
    private static final int STL = 9 + 1;
    private static final int STM = 9 + 2;
    private static final int STR = 9 + 3;
    // center
    private static final int SCL = 9 * 2 + 1;
    private static final int SCM = 9 * 2 + 2;
    private static final int SCR = 9 * 2 + 3;
    // bottom
    private static final int SBL = 9 * 3 + 1;
    private static final int SBM = 9 * 3 + 2;
    private static final int SBR = 9 * 3 + 3;
    // Crafting slot
    private static final int SOUT = 9 * 2 + 5;
    // Slot Exit
    private static final int SEX = 9 * 5 + 4;


    ItemStack exitItem;
    boolean prevTable = false;
    public SingleRecipeViewer(FFRecipe recipe) {
        super(54, "Crafting Recipe");
        fillPanes();
        exitItem = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").build();
        this.setItem(SEX, exitItem);
        this.addHandler(SEX,this::exitHandler);
        ItemStack[] itms = recipe.getRecipeItems();
        ItemStack output = recipe.getResult();
        for (int i = 0; i < 9; i++) {
            this.setItem(CraftingGUI.idxToSlot(i), itms[i]);
        }
        this.setItem(SOUT,output);
        this.prevTable = false;
    }

    public SingleRecipeViewer(FFRecipe recipe, boolean prevTable) {
        super(54, "Crafting Recipe");
        fillPanes();
        exitItem = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").build();
        this.setItem(SEX, exitItem);
        this.addHandler(SEX,this::exitHandler);
        ItemStack[] itms = recipe.getRecipeItems();
        ItemStack output = recipe.getResult();
        for (int i = 0; i < 9; i++) {
            this.setItem(CraftingGUI.idxToSlot(i), itms[i]);
        }
        this.setItem(SOUT,output);
        this.prevTable = prevTable;

    }

    /**
     * WARNING: Will clear inventory contents! Do not use if player items present!
     * Expected to only be used upon creation
     */
    @Override
    public void fillPanes() {
        ItemStack bgPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) {
            if ((i / 9 >= 1 && i/9 < 4 && i % 9 > 0 && i % 9 < 4)
                    || i == SOUT || i == SEX) continue;  // Skip non-background
            this.setItem(i,bgPane);
            this.addHandler(i,(e) -> e.setCancelled(true));
        }

        this.addHandler(SEX, this::exitHandler);

        // Exit row
        for (int i = 9 * 5; i < 54; i++) this.setItem(i, redPane);
    }
    public void exitHandler(InventoryClickEvent e) {
        e.getWhoClicked().openInventory(new RecipeListGUI(true).getInventory());
    }

}
