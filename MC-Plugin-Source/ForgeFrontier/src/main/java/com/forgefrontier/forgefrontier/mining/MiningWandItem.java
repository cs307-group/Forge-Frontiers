package com.forgefrontier.forgefrontier.mining;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MiningWandItem extends CustomItem {

    public static class MiningWandItemInstance extends CustomItemInstance {

        Vector pos1;
        Vector pos2;

        public MiningWandItemInstance(ItemStack itemStack) {
            super(itemStack);
        }

    }

    public MiningWandItem() {
        super("mining-wand");

        this.registerItemStackAccumulator((itemInstance, __) -> {
            MiningWandItemInstance inst = (MiningWandItemInstance) itemInstance;
            if(inst.pos1 != null)
                inst.getData().put("pos1", inst.pos1.getBlockX() + "," + inst.pos1.getBlockY() + "," + inst.pos1.getBlockZ());
            if(inst.pos2 != null)
                inst.getData().put("pos2", inst.pos2.getBlockX() + "," + inst.pos2.getBlockY() + "," + inst.pos2.getBlockZ());
            return new ItemStackBuilder(Material.GOLDEN_AXE)
                    .setDisplayName("&6Mining Wand")
                    .addLoreLine("&7Right/Left click on blocks to")
                    .addLoreLine("&7set the corners of a mining area.")
                    .addLoreLine("&aPosition 1: " + (inst.pos1 == null ? "Unset" : inst.pos1.toString()))
                    .addLoreLine("&aPosition 2: " + (inst.pos2 == null ? "Unset" : inst.pos2.toString()))
                    .build();
        });
        this.registerInstanceAccumulator(((__, itemStack) -> {
            MiningWandItemInstance inst = new MiningWandItemInstance(itemStack);

            if(inst.getData().containsKey("pos1")) {
                String[] pos1Vals = ((String) inst.getData().get("pos1")).split(",");
                inst.pos1 = new Vector(
                    Integer.parseInt(pos1Vals[0]),
                    Integer.parseInt(pos1Vals[1]),
                    Integer.parseInt(pos1Vals[2])
                );
            }

            if(inst.getData().containsKey("pos2")) {
                String[] pos2Vals = ((String) inst.getData().get("pos2")).split(",");
                inst.pos2 = new Vector(
                        Integer.parseInt(pos2Vals[0]),
                        Integer.parseInt(pos2Vals[1]),
                        Integer.parseInt(pos2Vals[2])
                );
            }

            return inst;
        }));
    }

    @Override
    public void onInteract(PlayerInteractEvent e, CustomItemInstance itemInstance) {
        if(!e.getPlayer().hasPermission("forgefrontier.mining.wand")) {
            return;
        }
        e.setCancelled(true);
        if(!ForgeFrontier.getInstance().getMiningManager().getMiningWorld().equals(e.getPlayer().getLocation().getWorld().getName())) {
            e.getPlayer().sendMessage(ForgeFrontier.CHAT_PREFIX + "You cannot use the wand here. You are not currently in the mining world.");
            return;
        }
        int slot = e.getPlayer().getInventory().getHeldItemSlot();
        MiningWandItemInstance inst = (MiningWandItemInstance) itemInstance;
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            inst.pos2 = e.getClickedBlock().getLocation().toVector();
            e.getPlayer().sendMessage(ForgeFrontier.CHAT_PREFIX + "Position 2 set to location at " + inst.pos2.getBlockX() + ", " + inst.pos2.getBlockY() + ", " + inst.pos2.getBlockZ());
        }
        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            inst.pos1 = e.getClickedBlock().getLocation().toVector();
            e.getPlayer().sendMessage(ForgeFrontier.CHAT_PREFIX + "Position 1 set to location at " + inst.pos1.getBlockX() + ", " + inst.pos1.getBlockY() + ", " + inst.pos1.getBlockZ());
        }
        e.getPlayer().getInventory().setItem(slot, itemInstance.asItemStack());
    }

}
