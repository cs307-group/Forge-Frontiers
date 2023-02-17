package com.forgefrontier.forgefrontier.gui;

import org.bukkit.inventory.Inventory;

public class ConfirmationHolder extends BaseInventoryHolder {

    public ConfirmationHolder(String message, Inventory previousInventory, Runnable confirm) {
        super(27);
        this.fillPanes();
    }

}
