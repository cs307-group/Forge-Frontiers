package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.materials.CoinMaterial;
import com.forgefrontier.forgefrontier.generators.materials.CustomMaterial;
import com.forgefrontier.forgefrontier.generators.materials.ItemMaterial;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.stashes.Stash;
import com.forgefrontier.forgefrontier.utils.Manager;
import com.forgefrontier.forgefrontier.utils.QuadTree;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.events.BentoBoxReadyEvent;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.island.IslandCache;

import java.util.*;
import java.util.function.Consumer;

public class GeneratorManager extends Manager implements Listener {

    Map<String, QuadTree<GeneratorInstance>> generatorInstanceTree;
    Map<String, Generator> generators;
    List<Generator> shopMenuList;

    List<CustomMaterial> materials;

    public GeneratorManager(ForgeFrontier plugin) {
        super(plugin);
        this.generatorInstanceTree = new HashMap<>();
        this.generators = new HashMap<>();
        this.shopMenuList = new ArrayList<>();
    }

    @Override
    public void init() {

        this.plugin.getCustomItemManager().registerCustomItem(new PlaceGeneratorItem());

        this.plugin.getDatabaseManager().getConfigDB().loadGenerators((generators) -> {
            for(Generator generator: generators) {
                this.generators.put(generator.getId(), generator);
            }

            int genIndex = 0;
            String generatorId;
            while((generatorId = this.plugin.getConfig("generators").getString("generator-shop." + genIndex)) != null) {
                if(!this.generators.containsKey(generatorId)) {
                    ForgeFrontier.getInstance().getLogger().severe("Unable to find generator \"" + generatorId + "\" to put in the shop...");
                    genIndex += 1;
                    continue;
                }
                shopMenuList.add(this.generators.get(generatorId));
                genIndex += 1;
            }
        });

    }

    @EventHandler
    public void onBentoBoxReady(BentoBoxReadyEvent e) {
        plugin.getDatabaseManager().getGeneratorDB().importGenerators();
    }

    public Generator getGenerator(String generatorId) {
        return this.generators.get(generatorId);
    }

    public CustomMaterial getCustomMaterial(String materialType, String id) {
        if(materialType.equals("item")) {
            return new ItemMaterial(CustomItemManager.getCustomItem(id));
        } else if(materialType.equals("coin")) {
            return new CoinMaterial();
        }
        ForgeFrontier.getInstance().getLogger().severe("[Generators] Configuration includes unknown material type: " + materialType);
        return null;
    }

    @Override
    public void disable() {
        plugin.getDatabaseManager().getConfigDB().updateGenerators();
    }

    @EventHandler
    public void onInteractGenerator(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        Location location = e.getClickedBlock().getLocation();
        Optional<Island> island = BentoBox.getInstance().getIslandsManager().getIslandAt(location);
        if(island.isEmpty()) {
            return;
        }
        QuadTree<GeneratorInstance> insts = generatorInstanceTree.get(island.get().getUniqueId());
        if(insts == null) return;
        GeneratorInstance instance = insts.get(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if(instance == null) return;
        e.getPlayer().openInventory(instance.getInventory());
        e.setCancelled(true);

        ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().updateGenerator(instance, (status) -> {
            Bukkit.getScheduler().runTask(ForgeFrontier.getInstance(), () -> {
                switch(status) {
                    case UPDATE_NEEDED:
                        e.getPlayer().openInventory(instance.getInventory());
                        break;
                    case ERROR:
                        plugin.getLogger().severe("An error occurred when attempting to look for updates to a generator.");
                        break;
                }
            });
        });

    }

    public void initializeGeneratorInstance(GeneratorInstance generatorInstance, Consumer<Boolean> callback) {
        boolean success = addGeneratorInstance(generatorInstance);
        if(!success) {
            callback.accept(false);
            return;
        }
        ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().insertGenerator(generatorInstance, (insertResult) -> {
            callback.accept(insertResult.isSuccess());
        });
    }

    public boolean addGeneratorInstance(GeneratorInstance generatorInstance) {
        Location location = generatorInstance.getLocation().clone();
        Island island = BentoBox.getInstance().getIslandsManager().getIslandCache().getIslandAt(location);
        if(island == null)
            return false;
        QuadTree<GeneratorInstance> tree = generatorInstanceTree.get(island.getUniqueId());
        if(tree == null) {
            Location corner = island.getCenter().subtract(512, 0, 512);
            tree = new QuadTree<>(corner.getBlockX(), -128, corner.getBlockZ(), 1024, 512, 1024);
            generatorInstanceTree.put(island.getUniqueId(), tree);
        }
        if(tree.get(location.getBlockX(), location.getBlockY(), location.getBlockZ()) != null) {
            tree.remove(tree.get(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            return true;
        }
        tree.insert(generatorInstance);
        return true;
    }

    public void removeGeneratorInstance(GeneratorInstance generatorInstance) {
        Island island = generatorInstance.getIsland();
        QuadTree<GeneratorInstance> tree = generatorInstanceTree.get(island.getUniqueId());
        if(tree == null) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Generator Instance to remove.");
            return;
        }
        tree.remove(generatorInstance);
        generatorInstance.getLocation().getBlock().setType(Material.AIR);
        ForgeFrontier.getInstance().getDatabaseManager().getGeneratorDB().removeGenerator(generatorInstance);
    }

    public List<Generator> getShopMenuList() {
        return this.shopMenuList;
    }

    public Collection<String> getGeneratorIds() {
        return this.generators.keySet();
    }

    public void registerGenerator(Generator generator) {
        this.generators.put(generator.getId(), generator);
    }
}
