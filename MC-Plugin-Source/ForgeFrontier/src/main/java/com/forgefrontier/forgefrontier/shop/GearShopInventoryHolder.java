package com.forgefrontier.forgefrontier.shop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.events.PurchaseEvent;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GearShopInventoryHolder extends BaseInventoryHolder {

    ForgeFrontier plugin;

    public GearShopInventoryHolder() {
        super(54, "Gear Shop");

        this.fillPanes();

        this.plugin = ForgeFrontier.getInstance();

        int shopInd = 0;
        ConfigurationSection config;
        while((config = this.plugin.getConfig("gear-shop").getConfigurationSection("gear-shop." + shopInd)) != null) {
            int shopY = 1 + shopInd / 7;
            int shopX = (shopInd % 7) + 1;
            final int slotId = shopX + shopY * 9;

            GearShopEntry entry = new GearShopEntry(config);

            ItemStackBuilder builder =
                new ItemStackBuilder(entry.getBaseInstance().getMaterial())
                    .setDisplayName("&f" + entry.getBaseInstance().getName())
                    .addLoreLine("&7" + entry.getBaseInstance().getLore());
            for(MaterialCost cost: entry.getCosts()) {
                builder.addLoreLine("&7>> &f" + cost.getAmount() + "&7x " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName());
            }
            this.setItem(slotId, builder.build());
            final List<MaterialCost> costs = entry.getCosts();
            this.addHandler(slotId, (e) -> {
                if(e.getWhoClicked().getInventory().firstEmpty() == -1) {
                    this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to space in your inventory.").build());
                    e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to space in your inventory.");
                    return;
                }
                boolean works = true;
                for(MaterialCost cost: costs) {
                    if(!cost.hasBalance((Player) e.getWhoClicked())) {
                        works = false;
                        break;
                    }
                }
                if(!works) {
                    this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to buy this item.").build());
                    e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to buy this item.");
                    return;
                }
                for(MaterialCost cost: costs) {
                    cost.take((Player) e.getWhoClicked());
                }
                ItemStack item = entry.getGearItem().asInstance(null).asItemStack();
                e.getWhoClicked().getInventory().addItem(item);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully bought the " + entry.getBaseInstance().getName() + ".");

                PurchaseEvent event = new PurchaseEvent((Player) e.getWhoClicked(), PurchaseEvent.PurchaseType.GEAR, item);
                Bukkit.getPluginManager().callEvent(event);

            });

            shopInd += 1;
        }

    }
}
