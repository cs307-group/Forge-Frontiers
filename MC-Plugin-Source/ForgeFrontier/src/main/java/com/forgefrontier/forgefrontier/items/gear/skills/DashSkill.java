package com.forgefrontier.forgefrontier.items.gear.skills;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.particles.gameparticles.SkillParticles;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;

public class DashSkill extends Skill{
    static double VELOCITY_MULT = 1.5;

    public DashSkill() {
        super(
                "dash-simple",
                "&fDash",
                "&7Dash in the direction you are looking",
                1000*5,
                SkillType.WEAPON
        );
    }
    @Override
    public void activate(Player p) {
        Vector lookDir = p.getLocation().getDirection().clone().normalize();
        p.setVelocity(lookDir.multiply(VELOCITY_MULT));
        new SimpleRepeatParticleSpawner(
                    () -> SkillParticles.DASHCLOUD.playAtPlayer(p),3,15).run();

    }
}
