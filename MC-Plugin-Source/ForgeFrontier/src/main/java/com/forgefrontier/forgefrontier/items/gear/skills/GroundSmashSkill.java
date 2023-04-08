package com.forgefrontier.forgefrontier.items.gear.skills;

import org.bukkit.entity.Player;

public class GroundSmashSkill extends Skill {

    public GroundSmashSkill() {
        super(
        "ground-smash",
        "&6Ground Smash",
        "&7Perform a smash onto the ground damaging\n&7all nearby mobs in an area around you.",
        20*30,
            SkillType.WEAPON
        );
    }

    @Override
    public void activate(Player p) {
        p.sendMessage("Ground smash!");
    }

}
