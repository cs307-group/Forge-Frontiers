package com.forgefrontier.forgefrontier.gui;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.MaterialCost;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.statistics.ReforgeStatistic;
import com.forgefrontier.forgefrontier.items.gear.upgradegems.GemEnum;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RerollInventoryHolder extends BaseInventoryHolder {

    public RerollInventoryHolder() {
        super(27, "Reroll Gear");

        this.fillPanes();

        this.setItem(9 + 2, new ItemStack(Material.AIR));
        this.addHandler(9 + 2, (e) -> e.setCancelled(false));

        ItemStackBuilder builder =
                new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE)
                        .setDisplayName("&aClick here to buy a Reroll");
        List<MaterialCost> costs = new ArrayList<>();
        int costInd = 0;
        ConfigurationSection configSection;
        while((configSection = ForgeFrontier.getInstance().getConfig().getConfigurationSection("reroll-costs." + costInd)) != null) {
            MaterialCost cost = new MaterialCost(configSection);
            costs.add(cost);
            builder.addLoreLine("&7>> &f" + cost.getAmount() + "&7x " + cost.getMaterial().getRepresentation().getItemMeta().getDisplayName());
            costInd += 1;
        }

        int slotId = 9 + 6;
        this.setItem(slotId, builder.build());

        this.addHandler(slotId, (e) -> {
            boolean works = true;
            for(MaterialCost cost: costs) {
                if(!cost.hasBalance((Player) e.getWhoClicked())) {
                    works = false;
                    break;
                }
            }
            if(!works) {
                this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou do not have enough to buy a reroll.").build());
                e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have enough to buy a reroll.");
                return;
            }
            ItemStack item = this.getInventory().getItem(9 + 2);
            CustomItemInstance itemInstance = CustomItemManager.asCustomItemInstance(item);
            if(item == null || !(itemInstance instanceof GearItemInstance)) {
                this.replaceItemTemporarily(slotId, new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("&cYou cannot reroll the item in the slot.").build());
                e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "You cannot reroll the item in the slot.");
                return;
            }
            GearItemInstance gearItemInstance = (GearItemInstance) itemInstance;
            for(MaterialCost cost: costs) {
                cost.take((Player) e.getWhoClicked());
            }

            for(ReforgeStatistic stat: gearItemInstance.getReforgeStats()) {
                stat.reforge(GemEnum.WEAPON);
            }
            // System.out.println(gearItemInstance.getMaterial());

            this.getInventory().setItem(9+2, gearItemInstance.asItemStack());

            //e.getWhoClicked().getInventory().addItem(item);
            //e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully rerolled your item.");
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        ItemStack item = this.getInventory().getItem(9+2);
        if(item != null)
            e.getPlayer().getInventory().addItem(item);
    }

}
