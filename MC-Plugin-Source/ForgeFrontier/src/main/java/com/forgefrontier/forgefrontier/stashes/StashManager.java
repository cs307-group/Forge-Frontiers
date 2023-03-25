package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.Manager;
import com.forgefrontier.forgefrontier.utils.QuadTree;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class StashManager extends Manager implements Listener {

    Map<String, QuadTree<StashInstance>> stashInstances;
    Map<String, Stash> stashes;

    public StashManager(ForgeFrontier plugin) {
        super(plugin);
        this.stashInstances = new HashMap<>();
        this.stashes = new HashMap<>();

    }

    @Override
    public void init() {

    }

    @Override
    public void disable() {

    }

    public Stash getStash(String stashId) {
        return this.stashes.get(stashId);
    }

    public void initializeStashInstance(StashInstance stashInstance, Consumer<Boolean> callback) {
        this.addStashInstance(stashInstance);
    }

    public boolean addStashInstance(StashInstance stashInstance) {
        Location location = stashInstance.getLocation().clone();
        //BentoBox.getInstance().getIslandsManager().
        Island island = BentoBox.getInstance().getIslandsManager().getIslandCache().getIslandAt(location);
        //System.out.println("Island: " + island);
        if(island == null)
            return false;
        QuadTree<StashInstance> tree = stashInstances.get(island.getUniqueId());
        if(tree == null) {
            Location corner = island.getCenter().subtract(512, 0, 512);
            tree = new QuadTree<>(corner.getBlockX(), -128, corner.getBlockZ(), 1024, 512, 1024);
            stashInstances.put(island.getUniqueId(), tree);
        }
        tree.insert(stashInstance);
        return true;
    }

}
