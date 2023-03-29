package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.TwoOptionHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NowLaterGUI extends BaseInventoryHolder {

    ItemStack createOrderItem;
    ItemStack fulfillOrderItem;
    int CREATE_ORDER_SLOT = 9 + 3;
    int FULFILL_SLOT = 9 + 5;
    int lookupIdx;
    public NowLaterGUI(int size, int item_idx) {
        super(size, "Create or Fulfill Order");
        lookupIdx = item_idx;


        this.fillPanes();

        createOrderItem = new ItemStackBuilder(Material.WRITABLE_BOOK)
                .setDisplayName("" + ChatColor.BOLD + ChatColor.GREEN + "Create Order")
                .setFullLore("" + ChatColor.DARK_GRAY + "Create an order that other players\n"
                                + ChatColor.DARK_GRAY + "may eventually fulfill.")
                .build();
        fulfillOrderItem = new ItemStackBuilder(Material.SUNFLOWER)
                .setDisplayName("" + ChatColor.BOLD + ChatColor.GOLD +"Fulfill Order")
                .setFullLore("" + ChatColor.DARK_GRAY + "Instantly Buy/Sell your items\n"
                                + ChatColor.DARK_GRAY + "by fulfilling a order!")
                .build();
        this.setItem(CREATE_ORDER_SLOT, createOrderItem);
        this.setItem(FULFILL_SLOT, fulfillOrderItem);

        this.addHandler(CREATE_ORDER_SLOT, (e) -> createClick(e));
        this.addHandler(FULFILL_SLOT, (e) -> fulfillClick(e));
    }

    public void createClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        TwoOptionHolder optionHolder = new TwoOptionHolder("Create Buy/Sell Order");
        ItemStack leftOpt = new ItemStackBuilder(Material.BLUE_CANDLE)
                .setDisplayName("" + ChatColor.BLUE + "Create Buy Order").build();
        ItemStack rightOpt = new ItemStackBuilder(Material.GOLD_NUGGET)
                .setDisplayName("" + ChatColor.GOLD + "Create Sell Order").build();
        optionHolder.setDisplaySlots(leftOpt,rightOpt);
        optionHolder.setOpts(
                () -> {
                    p.openInventory(new OrderCreationGUI(27,"Create Buy Order",true,
                            lookupIdx, optionHolder).getInventory());
                },
                () -> {
                    p.openInventory(new OrderCreationGUI(27,"Create Sell Order",false,
                            lookupIdx, optionHolder).getInventory());
                });
        p.openInventory(optionHolder.getInventory());
    }

    public void fulfillClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        TwoOptionHolder optionHolder = new TwoOptionHolder("Fulfill Buy/Sell Order");
        ItemStack leftOpt = new ItemStackBuilder(Material.FERN)
                .setDisplayName("" + ChatColor.GREEN + "Buy Items").build();
        ItemStack rightOpt = new ItemStackBuilder(Material.YELLOW_GLAZED_TERRACOTTA)
                .setDisplayName("" + ChatColor.GOLD + "Sell Items").build();
        optionHolder.setDisplaySlots(leftOpt,rightOpt);
        optionHolder.setOpts(
                () -> {
                    p.openInventory(new FulfillOrderGUI(27,"Fulfill Buy Order",true,
                            lookupIdx, optionHolder).getInventory());
                },
                () -> {
                    p.openInventory(new FulfillOrderGUI(27,"Fulfill Sell Order",false,
                            lookupIdx, optionHolder).getInventory());
                });
        p.openInventory(optionHolder.getInventory());
    }
}
