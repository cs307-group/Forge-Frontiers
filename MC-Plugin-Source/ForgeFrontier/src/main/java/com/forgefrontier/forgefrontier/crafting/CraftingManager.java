package com.forgefrontier.forgefrontier.crafting;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class CraftingManager extends Manager implements Listener {

    ArrayList<FFRecipe> ffRecipes;
    HashMap<String, ArrayList<FFRecipe>> recipeFilters;



    public CraftingManager(ForgeFrontier plugin) {
        super(plugin);
        ffRecipes = new ArrayList<>();
    }

    @Override
    public void init() {
        ItemStack itm = new ItemStack(Material.IRON_INGOT);
        ItemStack itm2 = new ItemStack(Material.GLOWSTONE_DUST);
        ItemStack[] testRecipe = new ItemStack[9];
        testRecipe[0] = itm;
        testRecipe[1] = itm2;
        ItemStack out = itm.clone(); out.setAmount(2);
        ffRecipes.add(new FFRecipe(testRecipe, out));

        ItemStack feather = CustomItemManager.getCustomItem("MagicFeather").asInstance(null).asItemStack();
        ItemStack skullOut = CustomItemManager.getCustomItem("AirOrb").asInstance(null).asItemStack();

        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        netherStar.setAmount(5);
        testRecipe = new ItemStack[] {feather, feather, feather, feather, netherStar, feather, feather, feather ,feather};
        FFRecipe airOrbRecipe = new FFRecipe(testRecipe,skullOut);
        airOrbRecipe.setRecipeMeta(new FFRecipe.SLOT_META[]
                {FFRecipe.SLOT_META.CUSTOM, FFRecipe.SLOT_META.CUSTOM, FFRecipe.SLOT_META.CUSTOM,
                 FFRecipe.SLOT_META.CUSTOM, FFRecipe.SLOT_META.VANILLA, FFRecipe.SLOT_META.CUSTOM,
                 FFRecipe.SLOT_META.CUSTOM, FFRecipe.SLOT_META.CUSTOM, FFRecipe.SLOT_META.CUSTOM});
        ffRecipes.add(airOrbRecipe);

    }

    @Override
    public void disable() {

    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getPlayer().isSneaking()) {
                return;
            }
            if (e.getClickedBlock() == null) return;
            if (e.getClickedBlock().getBlockData().getMaterial() == Material.CRAFTING_TABLE) {
                e.setCancelled(true);
                e.getPlayer().openInventory(new CraftingGUI().getInventory());
            }
        }
    }

    public ArrayList<FFRecipe> getRecipes() {
        return ffRecipes;
    }
}
