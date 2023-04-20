package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItemInstance;
import com.forgefrontier.forgefrontier.gui.ModifyGeneratorInventoryHolder;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;

@Command({"gen"})
public class GeneratorCommand {

    ForgeFrontier plugin;

    public GeneratorCommand(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    @DefaultFor({"gen"})
    public void genCmd(CommandSender sender) {
        if(!sender.hasPermission("forgefrontier.gen.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
            return;
        }
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Available Commands: \n" +
                ChatColor.YELLOW + "1. /gen create {Gen ID}\n" +
                ChatColor.YELLOW + "2. /gen modify {Gen ID}\n" +
                ChatColor.YELLOW + "3. /gen delete {Gen ID}\n" +
                ChatColor.YELLOW + "4. /gen give   {Gen ID}");
    }

    @Subcommand({"create"})
    public void genCreate(CommandSender sender, String generatorId) {
        if(!sender.hasPermission("forgefrontier.gen.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
            return;
        }
        if(!(sender instanceof Player p)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You must be a player to run this command.");
            return;
        }
        if(plugin.getGeneratorManager().getGenerator(generatorId) != null) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to create the generator. That generator already exists!");
            return;
        }
        Generator generator = new Generator(generatorId);
        plugin.getDatabaseManager().getConfigDB().createGenerator(generator, (success) -> {
            if (!success) {
                sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to create the generator. A problem occurred with the database.");
                return;
            }
            plugin.getGeneratorManager().registerGenerator(generator);
            p.openInventory(new ModifyGeneratorInventoryHolder(generator).getInventory());
        });
    }

    @Subcommand({"modify"})
    public void genModify(CommandSender sender, String generatorId) {
        if(!sender.hasPermission("forgefrontier.gen.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
            return;
        }
        if(!(sender instanceof Player p)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You must be a player to run this command.");
            return;
        }
        Generator generator = plugin.getGeneratorManager().getGenerator(generatorId);
        p.openInventory(new ModifyGeneratorInventoryHolder(generator).getInventory());
    }

    @Subcommand({"delete"})
    public void genDelete(CommandSender sender, String generatorId, @Optional String confirm) {
        if(!sender.hasPermission("forgefrontier.gen.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You must be a player to run this command.");
            return;
        }
        Generator generator = plugin.getGeneratorManager().getGenerator(generatorId);
        if(generator == null) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to delete the generator. It doesn't exist.");
        }
        // TODO Delete Generator
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to delete the generator.");
    }

    @Subcommand({"give"})
    public void genGive(CommandSender sender, String generatorId, @Optional String confirm) {
        if(!sender.hasPermission("forgefrontier.gen.cmd")) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You do not have permission to run this command.");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "You must be a player to run this command.");
            return;
        }
        Player p = (Player) sender;
        Generator generator = plugin.getGeneratorManager().getGenerator(generatorId);
        if(generator == null) {
            sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Unable to find generator \"" + generatorId + "\".");
            return;
        }
        PlaceGeneratorItemInstance placeGeneratorItemInstance = (PlaceGeneratorItemInstance) CustomItemManager.getCustomItem("PlaceGeneratorBlock").asInstance(null);
        placeGeneratorItemInstance.setGeneratorData(generator.getId(), 0);
        ItemStack generatorItem = placeGeneratorItemInstance.asItemStack();
        p.getInventory().addItem(generatorItem);
        sender.sendMessage(ForgeFrontier.CHAT_PREFIX + "Added the generator to your inventory.");
    }


}
