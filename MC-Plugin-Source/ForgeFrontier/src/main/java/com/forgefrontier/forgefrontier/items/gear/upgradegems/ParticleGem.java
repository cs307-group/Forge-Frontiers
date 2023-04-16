package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import org.bukkit.Material;

public class ParticleGem extends UniqueCustomItem {
    public ParticleGem(String code) {
        super(code);
        this.registerInstanceAccumulator((__, itemStack) -> {
            ParticleGemInstance inst = new ParticleGemInstance(itemStack);
            if (itemStack == null) {
                inst.pp = null;
            } else {
                inst.pp = ForgeFrontier.getInstance().getGearItemManager().
                        getPlayableParticle((String) inst.getData().get("particle-id"));
            }
            return inst;
        });



        this.registerItemStackAccumulator((inst, __) -> {
            ParticleGemInstance gemInst = (ParticleGemInstance) inst;
            ItemStackBuilder builder = new ItemStackBuilder(Material.WHITE_CANDLE);
            builder.setDisplayName("&d&oCosmetic Upgrade");
            if(inst == null)
                return builder.addLoreLine("&Cosmetic&7: &fUnknown").build();

            gemInst.getData().put("particle-id", "none");

            return builder.build();
        });




    }
}
