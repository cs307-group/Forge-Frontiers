package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.particles.FFCosmeticParticle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ParticleGem extends UniqueCustomItem {
    public ParticleGem() {
        super("ParticleGem");
        this.registerInstanceAccumulator((__, itemStack) -> {
            ParticleGemInstance inst = new ParticleGemInstance(itemStack);
            if (itemStack == null) {
                inst.pp = ForgeFrontier.getInstance().getParticleManager().randCosParticle();
            } else {
                inst.pp = (FFCosmeticParticle) ForgeFrontier.getInstance().getParticleManager()
                        .getParticleFromID((String) inst.getData().get("particle-id"));
            }
            return inst;
        });



        this.registerItemStackAccumulator((inst, __) -> {
            ParticleGemInstance gemInst = (ParticleGemInstance) inst;
            ItemStackBuilder builder = new ItemStackBuilder(gemInst.pp.cosmeticItem);
            builder.setDisplayName(ChatColor.RESET + gemInst.pp.prefixColor + "Particle Gem");
            if(inst == null)
                return builder.addLoreLine("&7Cosmetic&8: &fUnknown").build();
            builder.addLoreLine("&7Particle&8: " + gemInst.pp.name);
            gemInst.getData().put("particle-id", gemInst.pp.id);
            return builder.build();
        });

    }

    @Override
    public void onApply(InventoryClickEvent e, CustomItemInstance itemInstance, ItemStack appliedItem) {
        CustomItemInstance inst = CustomItemManager.asCustomItemInstance(appliedItem);

        if(inst == null)
            return;
        if(!(inst instanceof GearItemInstance gearInstance)) {
            return;
        }

        ParticleGemInstance particleGemInstance = (ParticleGemInstance) itemInstance;

        gearInstance.setParticleEffect(particleGemInstance.getPlayableParticle());

        e.setCancelled(true);
        e.setCurrentItem(gearInstance.asItemStack());
        e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
        ((Player) e.getWhoClicked()).updateInventory();
    }

}
