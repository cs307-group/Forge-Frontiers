package com.forgefrontier.forgefrontier.particles;

import com.forgefrontier.forgefrontier.particles.designs.ParticleDesign;
import com.forgefrontier.forgefrontier.particles.designs.StaticParticleDesign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FFCosmeticParticle extends FFParticle {
    public Material cosmeticItem = Material.WHITE_CANDLE;
    public String prefixColor = ChatColor.AQUA.toString();
    public boolean eye = true;
    public FFCosmeticParticle(ParticleDesign pd) {
        super(pd);
        delay = 100;

    }



    public void playParticleAtLocation(Player p) {
        World w = p.getWorld();
        Location l;
        if (eye) l = p.getEyeLocation();
        else l = p.getLocation();
        if (!(this.pd instanceof StaticParticleDesign)) {
            staticParticles = pd.getNext();
        }
        for (Vector v : staticParticles) {
            spawnOneParticle(w, l.clone().add(v));
        }
    }

    public String getLoreLine() {
        return "&7Particle&8: " + name;
    }

}
