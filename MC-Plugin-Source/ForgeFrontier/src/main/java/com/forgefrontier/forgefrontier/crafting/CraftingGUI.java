package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftingGUI extends BaseInventoryHolder {

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

    private boolean validRecipe = false;
    private ItemStack output = null;

    CraftingManager craftingManager;

    public CraftingGUI() {
        super(54, "Crafting Table");
        fillPanes();
        this.craftingManager = ForgeFrontier.getInstance().getCraftingManager();
        this.setDefaultCancelInteraction(false);
        // Exit item
        ItemStack exitItem = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").build();
        this.setItem(SEX ,exitItem);
        this.addHandler(SEX, this::onClose);
        this.addHandler(SOUT ,this::SOUTHandler);
    }
    public void inputSlotHandler(InventoryClickEvent e) {
        boolean found = false;
        for (FFRecipe r : craftingManager.getRecipes()) {
            if (!r.isMatch(getSlotItems())) continue;
            ItemStack validOut = r.getResult();
            this.setItem(SOUT, validOut);
            output = validOut;
            validRecipe = true;
            found = true;
            break;
        }
        if (!found) {
            this.validRecipe = false;
            this.output = null;
        }

    }


    public ItemStack[] getSlotItems() {
        Inventory inv = this.getInventory();
        ItemStack[] itms = new ItemStack[9];
        itms[0] = inv.getItem(STL);
        itms[1] = inv.getItem(STM);
        itms[2] = inv.getItem(STR);
        itms[3] = inv.getItem(SCL);
        itms[4] = inv.getItem(SCM);
        itms[5] = inv.getItem(SCR);
        itms[6] = inv.getItem(SBL);
        itms[7] = inv.getItem(SBM);
        itms[8] = inv.getItem(SBR);
        return itms;
    }

    public void SOUTHandler(InventoryClickEvent e) {
        if (!validRecipe) {
            e.setCancelled(true);
            return;
        }
    }

    public void onClose(InventoryClickEvent e) {

        e.getWhoClicked().closeInventory();
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
            this.setItem(i,bgPane);
            this.addHandler(i,(e) -> e.setCancelled(true));
        }

        // Crafting First Row
        this.setItem(10,new ItemStack(Material.AIR)); this.addHandler(10,(e) -> {});
        this.setItem(11,new ItemStack(Material.AIR)); this.addHandler(11,(e) -> {});
        this.setItem(12,new ItemStack(Material.AIR)); this.addHandler(12,(e) -> {});
        // Crafting Second Row
        this.setItem(19,new ItemStack(Material.AIR)); this.addHandler(19,(e) -> {});
        this.setItem(20,new ItemStack(Material.AIR)); this.addHandler(20,(e) -> {});
        this.setItem(21,new ItemStack(Material.AIR)); this.addHandler(21,(e) -> {});
        // Crafting Third Row
        this.setItem(28,new ItemStack(Material.AIR)); this.addHandler(28,(e) -> {});
        this.setItem(29,new ItemStack(Material.AIR)); this.addHandler(29,(e) -> {});
        this.setItem(30,new ItemStack(Material.AIR)); this.addHandler(30,(e) -> {});

        // Crafting Result
        this.setItem(9 * 2 + 5, new ItemStack(Material.BARRIER));

        // Exit row
        for (int i = 9 * 5; i < 54; i++) this.setItem(i, redPane);
    }



    @Override
    public void onClick(InventoryClickEvent e) {
        // Handle player inventory
        if(e.getClickedInventory() != e.getInventory()) {
            if(playerInventoryHandler != null)
                playerInventoryHandler.onClick(e);
            return;
        }

        // Handle default cancellation
        if (cancelInteractionDefault)
            e.setCancelled(true);

        // Handler callback
        if (e.getSlot() < 0 || e.getSlot() > size) return;
        if(handlers[e.getSlot()] != null)
            handlers[e.getSlot()].onClick(e);
    }

}
