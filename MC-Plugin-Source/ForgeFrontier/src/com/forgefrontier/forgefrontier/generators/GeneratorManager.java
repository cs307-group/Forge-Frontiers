package com.forgefrontier.forgefrontier.generators;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorManager extends Manager implements Listener {

    List<GeneratorInstance> generatorInstances;
    List<Generator> generators;

    public GeneratorManager(ForgeFrontier plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.generatorInstances = new ArrayList<>();
        this.generators = new ArrayList<>();

        CustomMaterial material = new CustomMaterial(new ItemStack(Material.DIAMOND), "Diamond");
        GeneratorLevel level = new GeneratorLevel(250, 128);
        Generator generator = new Generator(material, Arrays.asList(level));

        this.generators.add(generator);
        plugin.getCustomItemManager().registerCustomItem(new PlaceGeneratorItem(generator));

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
    }

    public void removeGeneratorInstance(GeneratorInstance generatorInstance) {
        this.generatorInstances.remove(generatorInstance);
    }

}
