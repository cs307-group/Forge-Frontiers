package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem;
import com.forgefrontier.forgefrontier.particles.FFCosmeticParticle;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.PlayerParticle;
import jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;

public class ParticleGemInstance  extends UniqueCustomItem.UniqueCustomItemInstance {
    FFCosmeticParticle pp;
    public ParticleGemInstance(@Nullable ItemStack itemStack) {
        super(itemStack);
    }
    public FFCosmeticParticle getPlayableParticle() {return pp;}



}
