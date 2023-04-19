package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class RecipeCreatorGUI extends BaseInventoryHolder {
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
    // Confirm
    private static final int SCONFIRM = 9 * 2 + 6;
    // Slot Exit
    private static final int SEX = 9 * 5 + 4;

    CraftingManager craftingManager;
    ForgeFrontier plugin;
    ItemStack exitItem;
    ItemStack confirmItem;

    ItemStack[] items;
    ItemStack output;
    public RecipeCreatorGUI() {
        super(54, "Add Custom Recipe");

        fillPanes();
        this.plugin = ForgeFrontier.getInstance();
        this.craftingManager = ForgeFrontier.getInstance().getCraftingManager();
        this.setDefaultCancelInteraction(false);
        // Exit item
        exitItem = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").build();
        confirmItem = new ItemStackBuilder(Material.GREEN_BANNER).setDisplayName(ChatColor.GREEN + "Add Recipe").build();
        this.setItem(SCONFIRM,confirmItem);
        this.setItem(SEX, exitItem);
        this.addHandler(SCONFIRM, this::confirmHandler);
        this.addHandler(SEX, this::onClose);
        this.addHandler(SOUT, this::SOUTHandler);
        this.addHandler(STL, this::inputSlotHandler);
        this.addHandler(STM, this::inputSlotHandler);
        this.addHandler(STR, this::inputSlotHandler);
        this.addHandler(SCL, this::inputSlotHandler);
        this.addHandler(SCM, this::inputSlotHandler);
        this.addHandler(SCR, this::inputSlotHandler);
        this.addHandler(SBL, this::inputSlotHandler);
        this.addHandler(SBM, this::inputSlotHandler);
        this.addHandler(SBR, this::inputSlotHandler);
        this.registerPlayerInventoryHandler(this::inputSlotHandler);
        items = new ItemStack[9];
        output = null;
    }


    public void updateItemsTask() {
        // Update Inventory contents
        Inventory inv = this.getInventory();
        items[0] = inv.getItem(STL);
        items[1] = inv.getItem(STM);
        items[2] = inv.getItem(STR);
        items[3] = inv.getItem(SCL);
        items[4] = inv.getItem(SCM);
        items[5] = inv.getItem(SCR);
        items[6] = inv.getItem(SBL);
        items[7] = inv.getItem(SBM);
        items[8] = inv.getItem(SBR);
    }

    public void inputSlotHandler(InventoryClickEvent e) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::updateItemsTask, 5);
    }

    public void outputHandler() {
        ItemStack itm = this.getInventory().getItem(SOUT);
        if (itm != null && itm.getType() != Material.AIR) {
            output = itm.clone();
            output.setAmount(itm.getAmount());
        }
    }
    public void SOUTHandler(InventoryClickEvent e) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::outputHandler, 5);

    }

    public void confirmHandler(InventoryClickEvent e) {
        if (output == null) { e.setCancelled(true); return; }
        FFRecipe recipe = new FFRecipe(items,output);
        FFRecipe.SLOT_META[] meta = new FFRecipe.SLOT_META[9];
        plugin.getLogger().log(Level.INFO, "New Recipe Added");
        boolean allNull = true;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) { meta[i] = FFRecipe.SLOT_META.VANILLA; continue; }
            allNull = false;
            CustomItemInstance ci = CustomItemManager.asCustomItemInstance(items[i]);
            if (ci != null) {
                meta[i] = FFRecipe.SLOT_META.CUSTOM;
                plugin.getLogger().log(Level.INFO, "\tSlot " + i + " - " +
                        ci.getBaseItem().getCode() + " x" + items[i].getAmount());
            } else {
                meta[i] = FFRecipe.SLOT_META.VANILLA;
                if (items[i] != null)
                plugin.getLogger().log(Level.INFO, "\tSlot " + i + " - " +
                        items[i].getType() + " x" + items[i].getAmount());
            }
        }
        if (allNull) { e.setCancelled(true); return; }
        plugin.getLogger().log(Level.INFO, "\tResult: " +
               output.getType() + " x" + output.getAmount());
        recipe.setRecipeMeta(meta);
        if (craftingManager.addNewRecipeDB(recipe)) {
            e.getWhoClicked().sendMessage(ChatColor.GREEN + "Successfully added recipe!");
        } else {
            e.getWhoClicked().sendMessage(ChatColor.RED + "Failed to add recipe.");
        }
        e.getWhoClicked().closeInventory();
        e.setCancelled(true);
    }

    public void onClose(InventoryClickEvent e) {
        ItemStack[] slotItems = getSlotItems();
        for (ItemStack i : slotItems) {
            ItemGiver.giveItem((Player)e.getWhoClicked(), i);
        }
        e.getWhoClicked().closeInventory();
    }

    public ItemStack[] getSlotItems() {
        Inventory inv = this.getInventory();
        ItemStack[] itms = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            ItemStack s = inv.getItem(CraftingGUI.idxToSlot(i));
            if (s != null) {
                items[i] = s.clone();
            }
        }
        return itms;
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
        this.setItem(10,new ItemStack(Material.AIR)); this.addHandler(10, this::inputSlotHandler);
        this.setItem(11,new ItemStack(Material.AIR)); this.addHandler(11, this::inputSlotHandler);
        this.setItem(12,new ItemStack(Material.AIR)); this.addHandler(12, this::inputSlotHandler);
        // Crafting Second Row
        this.setItem(19,new ItemStack(Material.AIR)); this.addHandler(19, this::inputSlotHandler);
        this.setItem(20,new ItemStack(Material.AIR)); this.addHandler(20, this::inputSlotHandler);
        this.setItem(21,new ItemStack(Material.AIR)); this.addHandler(21, this::inputSlotHandler);
        // Crafting Third Row
        this.setItem(28,new ItemStack(Material.AIR)); this.addHandler(28, this::inputSlotHandler);
        this.setItem(29,new ItemStack(Material.AIR)); this.addHandler(29, this::inputSlotHandler);
        this.setItem(30,new ItemStack(Material.AIR)); this.addHandler(30, this::inputSlotHandler);
        this.setItem(SOUT,new ItemStack(Material.AIR)); this.addHandler(30, this::SOUTHandler);
        this.setItem(SCONFIRM, confirmItem); this.addHandler(SCONFIRM, this::confirmHandler);

        // Crafting Result
        this.setItem(9 * 2 + 5, new ItemStack(Material.AIR));

        // Exit row
        for (int i = 9 * 5; i < 54; i++) this.setItem(i, redPane);
        this.setItem(SEX, exitItem);
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
