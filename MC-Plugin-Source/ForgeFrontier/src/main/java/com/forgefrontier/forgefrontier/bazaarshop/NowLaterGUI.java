package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.TwoOptionHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NowLaterGUI extends BaseInventoryHolder {

    ItemStack createOrderItem;
    ItemStack fulfillOrderItem;

    public NowLaterGUI(int size) {
        super(size, "Create or Fulfill Order");
        this.fillPanes();

        createOrderItem = new ItemStackBuilder(Material.WRITABLE_BOOK)
                .setDisplayName("Create Order")
                .setFullLore("" + ChatColor.DARK_GRAY + "Create an order that other players can eventually")
                .build();

    }

    public void slotClick(Player p, int idx) {
        TwoOptionHolder optionHolder = new TwoOptionHolder("Create/Fulfill Order");
        optionHolder.setOpts(
                () -> {
                    p.openInventory(new OrderCreationGUI(27,"Create Buy Order",true,
                            idx, optionHolder).getInventory());
                },
                () -> {
                    p.openInventory(new OrderCreationGUI(27,"Create Sell Order",false,
                            idx, optionHolder).getInventory());
                });
        p.openInventory(optionHolder.getInventory());
    }
}
