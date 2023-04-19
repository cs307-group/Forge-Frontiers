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
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class FulfillOrderGUI extends BaseInventoryHolder{
    boolean type; // 0 = sell, 1 = buy
    int lookupIdx;
    BazaarManager bazaarManager;
    int amount;
    private ItemStack amountAnvil;
    private final ItemStack confirmItem;

    private final ItemStack itm;
    private final int itmIdx;
    private final int DISPLAY_SLOT = 4;
    private final int AMOUNT_SELECT_SLOT = 9 + 3;
    private final int CONFIRM_SLOT = 9 + 5;
    private final BaseInventoryHolder prevInv;
    private final String name;

    public FulfillOrderGUI(int size, String name, boolean type, int idx, BaseInventoryHolder prevInv) {
        super(size, name);
        this.name = name;
        itmIdx = idx;
        this.type = type;
        bazaarManager = ForgeFrontier.getInstance().getBazaarManager();
        itm = bazaarManager.getDisplayItems().get(idx);
        this.fillPanes();
        amount = 1;
        amountAnvil = new ItemStackBuilder(Material.ANVIL)
                .setDisplayName("" + ChatColor.BOLD + "Select Amount").setFullLore("1x").build();
        confirmItem = new ItemStackBuilder(Material.GREEN_CANDLE)
                .setDisplayName("" + ChatColor.GREEN + "Confirm?").build();

        this.setItem(DISPLAY_SLOT, itm);
        this.setItem(AMOUNT_SELECT_SLOT, amountAnvil);
        this.setItem(CONFIRM_SLOT, confirmItem);

        this.addHandler(AMOUNT_SELECT_SLOT,(e) -> selectAmount(e));
        this.addHandler(CONFIRM_SLOT,(e) -> confirmClick(e));

        this.prevInv = prevInv;


    }

    private void confirmClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (type) {
            // buy items
            // 1. check if player has money
            // 2. execute transaction & delete listing
            // 3. Give player items, remove currency
            if (bazaarManager.execSellOrder(p, itmIdx, amount)) {
                p.sendMessage(BazaarManager.bazaarPrefix + ChatColor.GREEN +
                                "Successfully purchased from Bazaar!");
            } else {
                p.sendMessage(BazaarManager.bazaarPrefix + ChatColor.RED + "Error purchasing item...");
            }
            p.closeInventory();
        } else {
            // sell items
            // 1. Check if item has items
            // 2. execute transaction & delete listing
            // 3. Send other player items to their stash
            double res = bazaarManager.execBuyOrder(p,itmIdx,amount);
            if (res == -2) {
                p.sendMessage(BazaarManager.bazaarPrefix + ChatColor.RED + "Not enough items");
            } else if (res == -1) {
                p.sendMessage(BazaarManager.bazaarPrefix + ChatColor.RED + "Error purchasing item...");
            } else if (res >= 0) {
                p.sendMessage(BazaarManager.bazaarPrefix + ChatColor.GREEN +
                        "You have gained " + ChatColor.YELLOW + res + "g " + ChatColor.GREEN +
                        " from selling to bazaar!");
            }
            p.closeInventory();
        }



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
                            this.setItem(AMOUNT_SELECT_SLOT, amountAnvil);
                        }
                    } catch (NumberFormatException ignore){}
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                }))
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter Amount").text("" + amount)
                .plugin(ForgeFrontier.getInstance())
                .open((Player) e.getWhoClicked());
    }



}
