package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorInstance;
import com.forgefrontier.forgefrontier.generators.PlaceGeneratorItem;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.Manager;
import com.forgefrontier.forgefrontier.utils.QuadTree;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.events.BentoBoxReadyEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.*;
import java.util.function.Consumer;

public class StashManager extends Manager implements Listener {

    Map<String, QuadTree<StashInstance>> stashInstances;
    Map<String, Stash> stashes;
    List<Stash> shopStashesList;

    public StashManager(ForgeFrontier plugin) {
        super(plugin);
        this.stashInstances = new HashMap<>();
        this.stashes = new HashMap<>();
        this.shopStashesList = new ArrayList<>();
    }

    @Override
    public void init() {

        this.plugin.getCustomItemManager().registerCustomItem(new PlaceStashItem());

        this.plugin.getDatabaseManager().getConfigDB().loadStash((stashes) -> {
            for(Stash stash: stashes) {
                this.stashes.put(stash.getId(), stash);
            }

            int stashInd = 0;
            String stashId;
            while((stashId = this.plugin.getConfig("stashes").getString("stash-shop." + stashInd)) != null) {
                shopStashesList.add(this.stashes.get(stashId));
                stashInd += 1;
            }
        });

        /*
        int stashInd = 0;
        ConfigurationSection configSection;
        while((configSection = this.plugin.getConfig("stashes").getConfigurationSection("stashes." + stashInd)) != null) {
            Stash stash = new Stash(configSection);
            this.stashes.put(stash.getId(), stash);
            stashInd += 1;
        }*/



    }

    @EventHandler
    public void onBentoBoxReady(BentoBoxReadyEvent e) {
        plugin.getDatabaseManager().getStashDB().importStashes();
    }

    @Override
    public void disable() {

    }

    @EventHandler
    public void onInteractStash(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        Location location = e.getClickedBlock().getLocation();
        Optional<Island> island = BentoBox.getInstance().getIslandsManager().getIslandAt(location);
        if(island.isEmpty()) {
            return;
        }
        QuadTree<StashInstance> insts = this.stashInstances.get(island.get().getUniqueId());
        if(insts == null) return;
        StashInstance instance = insts.get(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if(instance == null) return;
        e.getPlayer().openInventory(instance.getInventory());
        e.setCancelled(true);

        ForgeFrontier.getInstance().getDatabaseManager().getStashDB().updateStash(instance, (status) -> {
            Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
                switch(status) {
                    case UPDATE_NEEDED:
                        e.getPlayer().openInventory(instance.getInventory());
                        break;
                    case ERROR:
                        plugin.getLogger().severe("An error occurred when attempting to look for updates to a stash.");
                        break;
                }
            });
        });

    }

    public Stash getStash(String stashId) {
        return this.stashes.get(stashId);
    }

    public void initializeStashInstance(StashInstance stashInstance, Consumer<Boolean> callback) {
        boolean success = this.addStashInstance(stashInstance);
        if(!success) {
            callback.accept(false);
            return;
        }
        ForgeFrontier.getInstance().getDatabaseManager().getStashDB().insertStash(stashInstance, (insertResult) -> {
            callback.accept(insertResult.isSuccess());
        });
    }

    public boolean addStashInstance(StashInstance stashInstance) {
        Location location = stashInstance.getLocation().clone();
        Island island = BentoBox.getInstance().getIslandsManager().getIslandCache().getIslandAt(location);
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

    public List<Stash> getStashShopList() {
        return this.shopStashesList;
    }

    public void removeStashInstance(StashInstance stashInstance) {
        Location location = stashInstance.getLocation().clone();
        Island island = BentoBox.getInstance().getIslandsManager().getIslandCache().getIslandAt(location);
        if(island == null)
            return;
        QuadTree<StashInstance> tree = stashInstances.get(island.getUniqueId());
        if(tree == null)
            return;
        tree.remove(stashInstance);
        stashInstance.getLocation().getBlock().setType(Material.AIR);
        ForgeFrontier.getInstance().getDatabaseManager().getStashDB().removeStash(stashInstance);
    }
}
