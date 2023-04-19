package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.crafting.CraftingManager;
import com.forgefrontier.forgefrontier.crafting.RecipeCreatorGUI;
import com.forgefrontier.forgefrontier.fishing.FishingManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;

@Command({"ffcraft"})
public class CraftingCommands {
    private final ForgeFrontier plugin;
    private final CraftingManager craftingManager;
    public CraftingCommands(ForgeFrontier plugin) {
        this.plugin = plugin;
        this.craftingManager = plugin.getCraftingManager();
    }

    @DefaultFor({"ffcraft"})
    public void ffCraftCmd(CommandSender sender) {
        sender.sendMessage("" + ChatColor.GOLD + "Available Commands: \n" +
                ChatColor.AQUA + "/ffcraft add" +
                ChatColor.WHITE + " - Add a new recipe!");
    }
    @Subcommand({"add", "newrecipe"})
    public void addRecipe(CommandSender sender) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can add recipes!");
            throw new SenderNotPlayerException();
        }
        p.openInventory(new RecipeCreatorGUI().getInventory());
    }



}
