package com.forgefrontier.forgefrontier.particles.gameparticles;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.FFCosmeticParticle;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.ParticleManager;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignChain;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignHalo;
import com.forgefrontier.forgefrontier.particles.designs.ParticleDesignSphere;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;

/**
 *
 * ADDING COSMETIC PARTICLES:
 *  - ID Allocated range: 12-{0-99} (1200 - 1299)
 *  - Name
 *  - Prefix Color: Gem will take on this color
 *  - Cosmetic Item: Item of gem (usually colored candle)
 *
 */
public class CosmeticParticles {
    public static FFCosmeticParticle HEARTHALO;
    public static FFCosmeticParticle MISTHALO;
    public static FFCosmeticParticle CRIMSONHALO;

    public static boolean isCosmetic(int id) {
        return (id >= 1200 && id <= 1299);
    }

    public static void init() {
        ParticleManager mngr = ForgeFrontier.getInstance().getParticleManager();

        ParticleDesignHalo pdc = new ParticleDesignHalo();
        pdc.setNum_points(10);
        pdc.createStaticPoints();
        HEARTHALO = new FFCosmeticParticle(pdc);
        HEARTHALO.setParticle(Particle.HEART);
        HEARTHALO.id = "CSMHeartHalo";
        HEARTHALO.name = ChatColor.RED + "Heart Halo";
        HEARTHALO.prefixColor = ChatColor.RED.toString();
        HEARTHALO.cosmeticItem = Material.RED_CANDLE;
        mngr.registerCosmeticParticle(HEARTHALO);


        MISTHALO = new FFCosmeticParticle(pdc);
        MISTHALO.setParticle(Particle.WHITE_ASH);
        MISTHALO.id = "CSMMistHalo";
        MISTHALO.name = ChatColor.GRAY + "Mist Halo";
        MISTHALO.prefixColor = ChatColor.GRAY.toString();
        MISTHALO.cosmeticItem = Material.GRAY_CANDLE;
        mngr.registerCosmeticParticle(MISTHALO);

        CRIMSONHALO = new FFCosmeticParticle(pdc);
        CRIMSONHALO.setParticle(Particle.CRIMSON_SPORE);
        CRIMSONHALO.id = "CSMCrimHalo";
        mngr.registerCosmeticParticle(CRIMSONHALO);
        CRIMSONHALO.cosmeticItem = Material.BLACK_CANDLE;
        CRIMSONHALO.prefixColor = ChatColor.DARK_RED.toString();
        CRIMSONHALO.name = ChatColor.DARK_RED + "Crimson Halo";
    }




}
