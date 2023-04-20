package com.forgefrontier.forgefrontier.items.gear.skills;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.gameparticles.SkillParticles;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.Collection;

public class InfernoSkill extends Skill {

    public InfernoSkill() {
        super(
                "inferno",
                "&4Inferno",
                "&eBurst into a fiery &4&lInferno\n&r&6Burning &eall nearby mobs in an area around you.",
                1000*60,
                SkillType.WEAPON
        );
    }

    @Override
    public void activate(Player p) {
        SkillParticles.INFERNO.playParticleAtLocation
                (p.getWorld(), p.getLocation().clone());
        new SimpleRepeatParticleSpawner(
                () -> SkillParticles.INFERNO.playAtPlayer(p), SkillParticles.INFERNO.delay, 400).run();
        int t = Bukkit.getScheduler().scheduleSyncRepeatingTask(ForgeFrontier.getInstance(),
                () -> doInferno(p), 0,20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(ForgeFrontier.getInstance(),
                () -> Bukkit.getScheduler().cancelTask(t), 400);

    }
    public void doInferno(Player p) {
        Collection<Entity> entityList = p.getLocation().getWorld()
                .getNearbyEntities(p.getLocation(), 5, 5, 5);
        for(Entity e: entityList) {
            if(e == p)
                continue;
            if(e instanceof Mob mob) {
                mob.damage(10);
                mob.setFireTicks(20);
            }
        }
    }



}
