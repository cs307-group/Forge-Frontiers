package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TwoOptionHolder extends BaseInventoryHolder {
    private static final int LEFT_SLOT = 9 + 3;
    private static final int RIGHT_SLOT = 9 + 5;
    private static final String DEFAULT_ACCEPT_STR = "§aYes";
    private static final String DEFAULT_CANCEL_STR = "§rNo";
    private BaseInventoryHolder prev = null;

    public TwoOptionHolder(String name, Runnable leftOpt, Runnable rightOpt) {
        super(27, name);
        this.fillPanes();
        this.addHandler(LEFT_SLOT, (e) -> clickHandler(e, leftOpt));
        this.addHandler(RIGHT_SLOT, (e) -> clickHandler(e, rightOpt));
        ItemStack defaultLeft = (new ItemStackBuilder(Material.BLUE_BANNER).setDisplayName("Option 1").build());
        ItemStack defaultRight = (new ItemStackBuilder(Material.RED_BANNER).setDisplayName("Option 2").build());

        this.setItem(LEFT_SLOT, defaultLeft);
        this.setItem(RIGHT_SLOT, defaultRight);

    }

    public TwoOptionHolder(String name) {
        super(27, name);
        this.fillPanes();
        ItemStack defaultLeft = (new ItemStackBuilder(Material.BLUE_BANNER).setDisplayName("Option 1").build());
        ItemStack defaultRight = (new ItemStackBuilder(Material.RED_BANNER).setDisplayName("Option 2").build());

        this.setItem(LEFT_SLOT, defaultLeft);
        this.setItem(RIGHT_SLOT, defaultRight);

    }

    public void setOpts(Runnable leftOpt, Runnable rightOpt) {
        this.addHandler(LEFT_SLOT, (e) -> clickHandler(e, leftOpt));
        this.addHandler(RIGHT_SLOT, (e) -> clickHandler(e, rightOpt));
    }

    public void setDisplaySlots(ItemStack left, ItemStack right) {
        this.setItem(LEFT_SLOT, left);
        this.setItem(RIGHT_SLOT, right);
    }

    public void setLeftSlot(ItemStack left) {
        this.setItem(LEFT_SLOT, left);
    }

    public void setRightSlot(ItemStack right) {
        this.setItem(RIGHT_SLOT, right);
    }

    /** Handles opening inventory. Runs runnable, then goes back to previous inventory once complete.
     *  Closes if there is no previous inventory. **/
    public void clickHandler(InventoryClickEvent e, Runnable r) {
        r.run();
    }


    public void setPreviousInventory(BaseInventoryHolder prev) {
        this.prev = prev;
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if (prev != null) {
            e.getPlayer().openInventory(prev.getInventory());
        }
    }
}
