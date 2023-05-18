package com.forgefrontier.forgefrontier.commands;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.custommobs.passive.ChickenTest;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.mobs.CustomEntityManager;
import com.forgefrontier.forgefrontier.utils.ItemGiver;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Chicken;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;

@Command({"cspawn"})
@Description("Command for spawning custom items.")
public class CustomSpawnCommand {
    private final ForgeFrontier plugin;

    public CustomSpawnCommand(ForgeFrontier plugin) {
        this.plugin = plugin;
    }

    @DefaultFor("cspawn")
    @AutoComplete("@cspawn")
    public void cspawnCmd(CommandSender sender, String mobname) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Unable to give items to a non-player");
            throw new SenderNotPlayerException();
        }
        spawnMob(mobname, p);
    }

    @Command("tchick")
    public void tchickSpawn(CommandSender sender) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Unable to give items to a non-player");
            throw new SenderNotPlayerException();
        }
        Location loc = p.getLocation();

        ServerLevel worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        ChickenTest.spawnCustomEntity(loc);
    }

    /** executes the spawning of the mob specified by the command sender) */
    private void spawnMob(String mobName, Player player) {
        if (plugin.getCustomEntityManager().spawnEntity(mobName, player)) {
            player.sendMessage("Successfully spawned the " + mobName);
        } else {
            player.sendMessage("Failed to spawn entity - likely " + mobName + " does not exist");
        }
    }



}
