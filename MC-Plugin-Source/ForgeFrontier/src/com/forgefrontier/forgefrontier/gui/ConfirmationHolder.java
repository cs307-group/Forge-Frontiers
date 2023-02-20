package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
public class ConfirmationHolder extends BaseInventoryHolder {

    private static final int ACCEPT_SLOT = 9 + 3;
    private static final int CANCEL_SLOT = 9 + 5;
    private static final String DEFAULT_ACCEPT_STR = "§aYes";
    private static final String DEFAULT_CANCEL_STR = "§rNo";

    private String acceptName;
    private String cancelName;

    /**
     * Simple Confirmation GUI
     * @param message Name of inventory
     * @param previousInventory Gets opened after, NULL if none (close after done).
     * @param confirm Code to execute on confirm.
     */
    public ConfirmationHolder(String message, Inventory previousInventory, Runnable confirm) {
        super(27, message);
        this.fillPanes();
        setAcceptItem(Material.GREEN_STAINED_GLASS_PANE, DEFAULT_ACCEPT_STR);
        setCancelItem(Material.RED_STAINED_GLASS_PANE, DEFAULT_CANCEL_STR);
        this.addHandler(ACCEPT_SLOT, (e) -> confirmHandler(e,previousInventory,confirm));
        this.addHandler(CANCEL_SLOT, (e) -> closeHandler(e,previousInventory));
    }

    public ConfirmationHolder(String message, Inventory previousInventory,
                              Runnable confirm, Material accept, Material cancel) {
        super(27, message);
        this.fillPanes();
        setAcceptItem(accept, DEFAULT_ACCEPT_STR);
        setCancelItem(cancel, DEFAULT_CANCEL_STR);
        this.addHandler(ACCEPT_SLOT, (e) -> confirmHandler(e,previousInventory,confirm));
        this.addHandler(CANCEL_SLOT, (e) -> closeHandler(e,previousInventory));
    }

    public ConfirmationHolder(String message, Inventory previousInventory,
                              Runnable confirm, ItemStack accept, ItemStack cancel) {
        super(27, message);
        this.fillPanes();
        setAcceptItem(accept);
        setCancelItem(cancel);
        this.addHandler(ACCEPT_SLOT, (e) -> confirmHandler(e,previousInventory,confirm));
        this.addHandler(CANCEL_SLOT, (e) -> closeHandler(e,previousInventory));
    }

    /**
     * Create ItemStack for accept
     * @param m Material for item
     * @param name Name for item
     */
    public void setAcceptItem(Material m, String name) {
        ItemStack yesItem = (new ItemStackBuilder(m).setDisplayName(name).build());
        this.setItem(ACCEPT_SLOT,yesItem);
    }
    /** Set Accept button by Itemstack **/
    public void setAcceptItem(ItemStack is) {
        ItemStack yesItem = (new ItemStackBuilder(is).build());
        this.setItem(ACCEPT_SLOT, yesItem);
    }

    /** Set Cancel button by Itemstack **/
    public void setCancelItem(ItemStack is) {
        ItemStack noItem = (new ItemStackBuilder(is).build());
        this.setItem(CANCEL_SLOT, noItem);
    }

    /**
     * Create ItemStack for cancel
     * @param m Material for item
     * @param name Name for item
     */
    public void setCancelItem(Material m, String name) {
        ItemStack noItem = (new ItemStackBuilder(m).setDisplayName(name).build());
        this.setItem(CANCEL_SLOT, noItem);
    }

    /** Handles opening inventory. Runs runnable, then goes back to previous inventory once complete.
     *  Closes if there is no previous inventory. **/
    public void confirmHandler(InventoryClickEvent e, Inventory previousInventory, Runnable r) {
        r.run();
        if (previousInventory == null) {
            e.getWhoClicked().closeInventory();
            return;
        }
        e.getWhoClicked().openInventory(previousInventory);
    }

    /** Goes back to previous inventory. Closes if there is none. **/
    public void closeHandler(InventoryClickEvent e, Inventory previousInventory) {
        if (previousInventory == null) {
            e.getWhoClicked().closeInventory();
            return;
        }
        e.getWhoClicked().openInventory(previousInventory);
    }
















}
