package com.forgefrontier.forgefrontier.mining;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MiningCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender.hasPermission("forgefrontier.mining"))) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX  + "You do not have permission to run this command.");
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player p = (Player) sender;
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("wand")) {
                p.getInventory().addItem(CustomItemManager.getCustomItem("mining-wand").asInstance(null).asItemStack());
                return true;
            }
            if(args[0].equalsIgnoreCase("create")) {
                if(args.length < 3) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid number of arguments. Usage: \n/mining create <Area Name> <Replacement Material>");
                    return true;
                }
                String areaName = args[1];
                Material material = Material.matchMaterial(args[2].toUpperCase());
                if(material == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid material. See list of Materials for a list of names to use.");
                    return true;
                }
                if(!material.isBlock()) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid material. Material must be a block.");
                    return true;
                }
                int slot = p.getInventory().getHeldItemSlot();
                ItemStack item = p.getInventory().getItem(slot);
                CustomItem customItem = CustomItemManager.getCustomItem(item);
                if(customItem == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Please hold your wand to create a new mining area.");
                    return true;
                }
                CustomItemInstance inst = customItem.asInstance(item);
                if(!(inst instanceof MiningWandItem.MiningWandItemInstance)) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Please hold your wand to create a new mining area.");
                    return true;
                }
                MiningWandItem.MiningWandItemInstance miningWandItemInstance = (MiningWandItem.MiningWandItemInstance) inst;
                MiningArea miningArea = new MiningArea(
                    miningWandItemInstance.pos1,
                    miningWandItemInstance.pos2,
                    areaName,
                    material
                );
                ForgeFrontier.getInstance().getMiningManager().registerNewMiningArea(miningArea);
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully registered the new area.");
                return true;
            }
            if(args[0].equalsIgnoreCase("addresource")) {
                if(args.length < 5) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid number of arguments. Usage: \n/mining addresource <Area Name> <Item> <Block Material> <Time>");
                    return true;
                }
                String areaName = args[1];
                String itemCode = args[2];
                String materialCode = args[3];
                if(Material.matchMaterial(materialCode) == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid material. See list of Materials for a list of names to use.");
                    return true;
                }
                if(!Material.matchMaterial(materialCode).isBlock()) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid material. Material must be a block.");
                    return true;
                }
                if(CustomItemManager.getCustomItem(itemCode) == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid custom item. Please use a valid custom item.");
                    return true;
                }
                String timeStr = args[4];
                int time;
                try { time = Integer.parseInt(timeStr); } catch(NumberFormatException e) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid time. Must be an integer.");
                    return true;
                }
                if(time < 1) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid time. Must be a positive integer.");
                    return true;
                }

                MiningArea area = ForgeFrontier.getInstance().getMiningManager().getMiningArea(areaName);
                if(area == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid mining area. The given one does not exist.");
                    return true;
                }
                area.addResource(new MiningResource(itemCode, materialCode, time));
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully added the given resource.");
                return true;
            }
            if(args[0].equalsIgnoreCase("remove")) {
                if(args.length < 2) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid number of arguments. Usage: \n/mining remove <Area Name>");
                    return true;
                }
                String areaName = args[1];
                if(ForgeFrontier.getInstance().getMiningManager().getMiningArea(areaName) == null) {
                    sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to find mining area with name: " + areaName);
                    return true;
                }
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Successfully delted mining area with name: " + areaName);
                ForgeFrontier.getInstance().getMiningManager().removeMiningArea(areaName);
            }
        }
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Invalid command arguments. Usage:");
        sender.sendMessage(ChatColor.RED + "1. " + ChatColor.YELLOW + "/mining wand");
        sender.sendMessage(ChatColor.RED + "2. " + ChatColor.YELLOW + "/mining create <Area Name> <Replacement Material>");
        sender.sendMessage(ChatColor.RED + "3. " + ChatColor.YELLOW + "/mining addresource <Area Name> <Item> <Block Material> <Time>");
        sender.sendMessage(ChatColor.RED + "4. " + ChatColor.YELLOW + "/mining remove <Area Name>");
        return true;
    }
}
