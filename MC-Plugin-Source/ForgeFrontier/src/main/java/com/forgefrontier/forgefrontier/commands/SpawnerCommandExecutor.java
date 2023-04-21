package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.spawners.SpawnBlockItemInstance;
import com.forgefrontier.forgefrontier.spawners.Spawner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;

@Command({"cspawner"})
public class SpawnerCommandExecutor {

    ForgeFrontier plugin;

    public SpawnerCommandExecutor(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    @DefaultFor({"cspawner"})
    @AutoComplete("@cspawner")
    public void spawnCmd(CommandSender sender) {
        if (!sender.hasPermission("forgefrontier.spawner.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
        }
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Available Commands: \n" +
                ChatColor.YELLOW + "1. /spawner give {Spawner ID}\n");
    }

    @Subcommand({"give"})
    @AutoComplete("@cspawner @placeable_items")
    public void spawnGive(CommandSender sender, String spawnerId, @Optional String material) {
        if (!sender.hasPermission("forgefrontier.spawner.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You must be a player to run this command.");
            return;
        }
        Player p = (Player) sender;
        Spawner spawner = plugin.getSpawnerManager().getSpawner(spawnerId);
        if (spawner == null) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to find spawner \"" + spawnerId + "\".");
            return;
        }
        spawner.setBlockMaterial(Material.getMaterial(material));
        SpawnBlockItemInstance spawnBlockItemInstance = (SpawnBlockItemInstance) CustomItemManager.getCustomItem("PlaceSpawnerBlock").asInstance(null);
        spawnBlockItemInstance.setSpawnerData(spawner.getId(), spawnerId);
        ItemStack spawnerItem = spawnBlockItemInstance.asItemStack();
        p.getInventory().addItem(spawnerItem);
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Added the spawner to your inventory");
    }
}
