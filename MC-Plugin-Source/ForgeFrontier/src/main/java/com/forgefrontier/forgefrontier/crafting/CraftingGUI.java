package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

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
    private FFRecipe currentRecipe = null;
    private int currAmount = 0;


    CraftingManager craftingManager;
    ForgeFrontier plugin;
    public CraftingGUI() {
        super(54, "Crafting Table");
        fillPanes();
        this.plugin = ForgeFrontier.getInstance();
        this.craftingManager = ForgeFrontier.getInstance().getCraftingManager();
        this.setDefaultCancelInteraction(false);
        // Exit item
        ItemStack exitItem = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").build();
        this.setItem(SEX, exitItem);
        this.addHandler(SEX, this::exitHandler);
        this.addHandler(SOUT ,this::SOUTHandler);

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
    }
    public void inputSlotHandler(InventoryClickEvent e) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::checkRecipeTask, 5);
    }

    public void playerShiftHandler(InventoryClickEvent e) {
        if (e.isShiftClick()) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::checkRecipeTask, 5);
        }
    }
    public void checkRecipeTask() {

        for (FFRecipe r : craftingManager.getRecipes()) {
            ItemStack[] slotItems = getSlotItems();
            if (updateToRecipe(r,slotItems)) return;
        }
        // Nothing found
        this.validRecipe = false;
        this.output = null;
        this.setItem(SOUT, new ItemStack(Material.BARRIER));
        currentRecipe = null;
        currAmount = 0;
    }

    public boolean updateToRecipe(FFRecipe r, ItemStack[] slotItems) {
        if (!r.isMatch(slotItems)) {
            return false;
        }
        ItemStack validOut = r.getResult();
        currAmount = r.getCraftableAmount(slotItems);
        this.setItem(SOUT, validOut);
        output = validOut;
        validRecipe = true;
        currentRecipe = r;
        return true;
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
        if (!validRecipe || currentRecipe == null) {
            e.setCancelled(true);
            return;
        }
        if (e.getCursor() != null && e.getCursor().getType() != Material.AIR &&
                !e.getCursor().isSimilar(currentRecipe.getResult())) {
            e.setCancelled(true);
            return;
        }
        // Calculate amounts
        ItemStack[] itms = getSlotItems();
         if (e.isShiftClick()) {
            // Validate amounts
            for (int i = 0; i < 9; i++) {
                ItemStack ritem = currentRecipe.getItemComponent(i);
                if (ritem != null && ritem.getType() != Material.AIR) {
                    if (itms[i] == null || itms[i].getAmount() / ritem.getAmount() < currAmount)
                    { e.setCancelled(true); return; }
                }
            }
            // Update crafting slots
            for (int i = 0; i < 9; i++) {
                ItemStack ritem = currentRecipe.getItemComponent(i);
                if (ritem != null && ritem.getType() != Material.AIR) {
                    int rm = itms[i].getAmount() - currAmount * ritem.getAmount();
                    if (rm == 0) {
                        this.setItem(idxToSlot(i), new ItemStack(Material.AIR));
                    } else {
                        ItemStack nitm = itms[i].clone();
                        nitm.setAmount(rm);
                        this.setItem(idxToSlot(i), nitm);
                    }
                }
            }
            ItemStack outItm = currentRecipe.getResult();
            outItm.setAmount(1);
            ItemGiver.giveItem((Player) e.getWhoClicked(),outItm,currentRecipe.getOutAmount() * currAmount);
            currAmount = 0;
        } else if (e.isLeftClick() || e.isRightClick()) {
            // Validate amounts
            int nAmt = 1;
            for (int i = 0; i < 9; i++) {
                ItemStack ritem = currentRecipe.getItemComponent(i);
                if (ritem != null && ritem.getType() != Material.AIR) {
                    if (itms[i] == null || itms[i].getAmount() / ritem.getAmount() < nAmt)
                    { e.setCancelled(true); return; }
                }
            }
            boolean more = true;    // more = can make more after doing 1 craft
             // Update crafting slots
             for (int i = 0; i < 9; i++) {
                ItemStack ritem = currentRecipe.getItemComponent(i);
                if (ritem != null && ritem.getType() != Material.AIR) {
                    int rm = (itms[i].getAmount() - currentRecipe.getItemComponent(i).getAmount());
                    if (rm == 0) {
                        this.setItem(idxToSlot(i), new ItemStack(Material.AIR));
                        more = false;
                    } else {
                        // Can still craft more
                        ItemStack nItem = itms[i].clone();
                        nItem.setAmount(rm);
                        this.setItem(idxToSlot(i), nItem);
                    }
                }
            }
            if (more) {
                this.setItem(SOUT,currentRecipe.getResult());
            } else {
                this.setItem(SOUT, new ItemStack(Material.BARRIER));
                validRecipe = false;
                output = null;
            }
            if (e.getCursor() == null || e.getCursor().getType() == Material.AIR) {
                e.getWhoClicked().setItemOnCursor(currentRecipe.getResult());   // result should have correct out amt
            } else {
                ItemStack curs = e.getCursor();
                curs = curs.clone();
                curs.setAmount(curs.getAmount() + currentRecipe.getOutAmount());
                e.getWhoClicked().setItemOnCursor(curs);
            }

        }
        e.setCancelled(true);



    }


    public static int idxToSlot(int idx) {
        return switch (idx) {
            case 0 -> STL;
            case 1 -> STM;
            case 2 -> STR;
            case 3 -> SCL;
            case 4 -> SCM;
            case 5 -> SCR;
            case 6 -> SBL;
            case 7 -> SBM;
            case 8 -> SBR;
            default -> STL;
        };
    }



    public void exitHandler(InventoryClickEvent e) {
//        ForgeFrontier.getInstance().getLogger().log(Level.INFO, "Close Crafting Table, returning items");
//        ItemStack[] slotItems = getSlotItems();
//        for (ItemStack i : slotItems) {
//            ItemGiver.giveItem((Player)e.getWhoClicked(), i);
//        }
        e.getWhoClicked().closeInventory();
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        ForgeFrontier.getInstance().getLogger().log(Level.INFO, "Close Crafting Table, returning items");
        ItemStack[] slotItems = getSlotItems();
        Player p = (Player) e.getPlayer();
        for (ItemStack i : slotItems) {
            if (i == null || i.getType() == Material.AIR) continue;
            ItemGiver.giveItem(p, i);
        }
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
