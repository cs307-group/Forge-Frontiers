package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.gui.PreviousInventoryRunnable;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderCreationGUI extends BaseInventoryHolder {


    private final boolean type;
    private ItemStack amountAnvil;
    private ItemStack priceItem;
    private int itemIdx;
    private int amount;
    private double price;
    private BazaarManager bazaarManager;
    private ItemStack itm;
    private BaseInventoryHolder prevInv;
    private String name;


    public OrderCreationGUI(int size, String name, boolean type, int idx, BaseInventoryHolder prevInv) {
        super(size, name);
        this.name = name;
        this.type = type;
        itemIdx = idx;
        bazaarManager = ForgeFrontier.getInstance().getBazaarManager();
        itm = bazaarManager.getDisplayItems().get(idx);
        this.fillPanes();
        amount = 1;
        amountAnvil = new ItemStackBuilder(Material.ANVIL)
                .setDisplayName("" + ChatColor.BOLD + "Select Amount").setFullLore("1x").build();
        this.setItem(9 + 2, amountAnvil);
        price = bazaarManager.getMinInstantBuyPrice(idx);
        priceItem = new ItemStackBuilder(Material.PAPER)
                .setDisplayName(ChatColor.GOLD + "Select Price")
                .setFullLore("" + ChatColor.BOLD + "%.2f".formatted(price) + "g").build();
        this.setItem( 4, itm);
        this.setItem(9 + 4, priceItem);
        this.addHandler(9 + 2, (e) -> selectAmount(e));
        this.prevInv = prevInv;
    }


    private void selectAmount(InventoryClickEvent e) {
        new AnvilGUI.Builder()
            .onClose((player) ->
                new PreviousInventoryRunnable(player,this.getInventory())
                    .runTaskLater(ForgeFrontier.getInstance(),5))
            .onComplete((completion -> {
                String input = completion.getText();
                try {
                    amount = Integer.parseInt(input);
                    if (amount > 0) {
                        amountAnvil = new ItemStackBuilder(Material.ANVIL)
                                .setDisplayName("" + ChatColor.BOLD + "Select Amount")
                                .setFullLore("" + ChatColor.GRAY + amount + "x").build();
                        this.setItem(9 + 2, amountAnvil);
                    }
                } catch (NumberFormatException ignore){}
                return Arrays.asList(AnvilGUI.ResponseAction.close());
            }))
                .itemLeft(new ItemStack(Material.PAPER))
            .title("Enter Amount").text("" + amount)
            .plugin(ForgeFrontier.getInstance())
            .open((Player) e.getWhoClicked());
    }

    public void selectPrice(InventoryClickEvent e) {
        new AnvilGUI.Builder().onClose((player) -> {
            new PreviousInventoryRunnable(player,this.getInventory()).runTaskLater(ForgeFrontier.getInstance(),5);
            })
            .onComplete((completion -> {
                String input = completion.getText();
                try {
                    price = Integer.parseInt(input);
                    priceItem = new ItemStackBuilder(Material.PAPER)
                            .setDisplayName(ChatColor.GOLD + "Select Price")
                            .setFullLore("" + ChatColor.GOLD + "%.2f".formatted(price) + "g").build();
                    this.setItem(9 + 4, priceItem);
                } catch (NumberFormatException ignore){}
                return Arrays.asList(AnvilGUI.ResponseAction.close());
            }))
            .itemLeft(new ItemStack(Material.PAPER))
            .title("Enter Price").text("" + amount)
            .plugin(ForgeFrontier.getInstance())
            .open((Player) e.getWhoClicked());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        /*
        if (prevInv != null) {
            BukkitRunnable br = new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(prevInv.getInventory());
                }
            };
            br.runTaskLater(ForgeFrontier.getInstance(),5);
        }

         */
    }

}
