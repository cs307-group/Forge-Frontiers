package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.generators.materials.CoinMaterial;
import com.forgefrontier.forgefrontier.generators.materials.CustomMaterial;
import com.forgefrontier.forgefrontier.generators.materials.ItemMaterial;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GeneratorManager extends Manager implements Listener {

    List<GeneratorInstance> generatorInstances;
    Map<String, Generator> generators;
    List<Generator> shopMenuList;

    List<CustomMaterial> materials;

    public GeneratorManager(ForgeFrontier plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.generatorInstances = new ArrayList<>();
        this.generators = new HashMap<>();
        this.shopMenuList = new ArrayList<>();

        //CustomMaterial material = new CustomMaterial(new ItemStack(Material.DIAMOND), "Diamond");
        //GeneratorLevel level = new GeneratorLevel(250, 128);
        //Generator generator = new Generator(material, Arrays.asList(level));

        //this.generators.add(generator);
        //plugin.getCustomItemManager().registerCustomItem(new PlaceGeneratorItem(generator));

        // Config
        /*
        this.plugin.getConfig().addDefault("generator-shop.0", "diamond-gen");
        this.plugin.getConfig().addDefault("generator-shop.1", "coin-gen");

        this.plugin.getConfig().addDefault("generators.0.id", "diamond-gen");
        this.plugin.getConfig().addDefault("generators.0.friendly_name", "&bCoin Generator 1");
        this.plugin.getConfig().addDefault("generators.0.representation_material", Material.RAW_IRON_BLOCK.toString());
        this.plugin.getConfig().addDefault("generators.0.material_type", "coin");
        this.plugin.getConfig().addDefault("generators.0.item_id", "diamond");
        this.plugin.getConfig().addDefault("generators.0.costs.0.material_type", "coin");
        this.plugin.getConfig().addDefault("generators.0.costs.0.amount", 10000);
        this.plugin.getConfig().addDefault("generators.0.levels.0.generation_rate", 10);
        this.plugin.getConfig().addDefault("generators.0.levels.0.max_size", 10000);
        this.plugin.getConfig().addDefault("generators.0.levels.0.update_costs.0.material_type", "coin");
        this.plugin.getConfig().addDefault("generators.0.levels.0.update_costs.0.amount", 10000);
        this.plugin.getConfig().addDefault("generators.0.levels.1.generation_rate", 5);
        this.plugin.getConfig().addDefault("generators.0.levels.1.max_size", 100000);
        //this.plugin.getConfig().addDefault("generators.0.levels.0.additional_drops.0.chance", 0.01);
        //this.plugin.getConfig().addDefault("generators.0.levels.0.additional_drops.0.chance", 0.01);

        this.plugin.getConfig().addDefault("generators.1.id", "coin-gen");
        this.plugin.getConfig().addDefault("generators.1.friendly_name", "&eCoin Generator 2");
        this.plugin.getConfig().addDefault("generators.1.representation_material", Material.RAW_GOLD_BLOCK.toString());
        this.plugin.getConfig().addDefault("generators.1.material_type", "coin");
        this.plugin.getConfig().addDefault("generators.1.costs.0.material_type", "coin");
        this.plugin.getConfig().addDefault("generators.1.costs.0.amount", 10000);
        this.plugin.getConfig().addDefault("generators.1.levels.0.generation_rate", 10);
        this.plugin.getConfig().addDefault("generators.1.levels.0.max_size", 10000);
        this.plugin.getConfig().addDefault("generators.1.levels.0.update_costs.0.material_type", "coin");
        this.plugin.getConfig().addDefault("generators.1.levels.0.update_costs.0.amount", 10000);
        this.plugin.getConfig().addDefault("generators.1.levels.1.generation_rate", 5);
        this.plugin.getConfig().addDefault("generators.1.levels.1.max_size", 100000);
         */


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
        for(GeneratorInstance generatorInstance: generatorInstances) {
            if(generatorInstance.boundingBox.isColliding(e.getClickedBlock().getLocation())) {
                e.getPlayer().openInventory(generatorInstance.getInventory());
                e.setCancelled(true);
                break;
            }
        }
    }

    public void addGeneratorInstance(GeneratorInstance generatorInstance) {
        this.generatorInstances.add(generatorInstance);
        generatorInstance.getBoundingBox().getLocation().getBlock().setType(generatorInstance.generator.getMaterialRepresentation());
    }

    public void removeGeneratorInstance(GeneratorInstance generatorInstance) {
        this.generatorInstances.remove(generatorInstance);
        generatorInstance.getBoundingBox().getLocation().getBlock().setType(Material.AIR);
    }

    public List<Generator> getShopMenuList() {
        return this.shopMenuList;
    }

}
