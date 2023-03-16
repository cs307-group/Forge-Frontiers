package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.materials.CoinMaterial;
import com.forgefrontier.forgefrontier.generators.materials.CustomMaterial;
import com.forgefrontier.forgefrontier.generators.materials.ItemMaterial;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.Manager;
import com.forgefrontier.forgefrontier.utils.QuadTree;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.*;

public class GeneratorManager extends Manager implements Listener {

    Map<String, QuadTree<GeneratorInstance>> generatorInstanceTree;
    Map<String, Generator> generators;
    List<Generator> shopMenuList;

    List<CustomMaterial> materials;

    public GeneratorManager(ForgeFrontier plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.generatorInstanceTree = new HashMap<>();
        this.generators = new HashMap<>();
        this.shopMenuList = new ArrayList<>();

        this.plugin.getCustomItemManager().registerCustomItem(new PlaceGeneratorItem());

        int generatorInd = 0;
        ConfigurationSection configSection;
        while((configSection = this.plugin.getConfig().getConfigurationSection("generators." + generatorInd)) != null) {
            Generator generator = new Generator(configSection);
            this.generators.put(generator.getId(), generator);
            generatorInd += 1;
        }

        int genIndex = 0;
        String generatorId;
        while((generatorId = this.plugin.getConfig().getString("generator-shop." + genIndex)) != null) {
            shopMenuList.add(this.generators.get(generatorId));
            genIndex += 1;
        }

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
    }

    public boolean addGeneratorInstance(GeneratorInstance generatorInstance) {
        Optional<Island> island = BentoBox.getInstance().getIslandsManager().getIslandAt(generatorInstance.getBoundingBox().getLocation());
        if(island.isEmpty()) return false;
        QuadTree<GeneratorInstance> tree = generatorInstanceTree.get(island.get().getUniqueId());
        if(tree == null) {
            Location corner = island.get().getCenter().subtract(512, -128, 512);
            generatorInstanceTree.put(island.get().getUniqueId(), new QuadTree<>(corner.getBlockX(), corner.getBlockY(), corner.getBlockZ(), 1024, 512, 1024));
        }
        tree.insert(generatorInstance);

        //generatorInstance.getBoundingBox().getLocation().getBlock().setType(generatorInstance.generator.getMaterialRepresentation());
        return true;
    }

    public void removeGeneratorInstance(GeneratorInstance generatorInstance) {
        Optional<Island> island = BentoBox.getInstance().getIslandsManager().getIslandAt(generatorInstance.getBoundingBox().getLocation());
        if(island.isEmpty()) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Generator Instance to remove.");
            return;
        }
        QuadTree<GeneratorInstance> tree = generatorInstanceTree.get(island.get().getUniqueId());
        if(tree == null) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to find Generator Instance to remove.");
            return;
        }
        tree.remove(generatorInstance);
        generatorInstance.getBoundingBox().getLocation().getBlock().setType(Material.AIR);
    }

    public List<Generator> getShopMenuList() {
        return this.shopMenuList;
    }

}
