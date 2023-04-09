package com.forgefrontier.forgefrontier.items.gear.skills;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;

public class GroundSmashSkill extends Skill {

    public GroundSmashSkill() {
        super(
        "ground-smash",
        "&6Ground Smash",
        "&7Perform a smash onto the ground damaging\n&7all nearby mobs in an area around you.",
        1000*10,
            SkillType.WEAPON
        );
    }

    @Override
    public void activate(Player p) {
        p.setVelocity(p.getVelocity().add(new Vector(0, 0.35, 0)));

        Bukkit.getScheduler().runTaskLater(ForgeFrontier.getInstance(), () -> {
            Collection<Entity> entityList = p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 10, 2.5, 10);

            for(Entity e: entityList) {
                if(e == p)
                    continue;
                e.setVelocity(e.getLocation().clone().subtract(p.getLocation()).toVector().setY(0).normalize().multiply(0.5).setY(0.5));
                if(e instanceof Mob mob) {
                    mob.damage(10);
                }
            }
        }, 10);
    }

}
