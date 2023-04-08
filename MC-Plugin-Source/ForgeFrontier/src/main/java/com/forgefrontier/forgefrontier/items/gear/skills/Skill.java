package com.forgefrontier.forgefrontier.items.gear.skills;

import com.forgefrontier.forgefrontier.stashes.Stash;
import org.bukkit.entity.Player;

public abstract class Skill {

    String id;
    String name;
    String descrption;
    int cooldown;
    SkillType type;

    public Skill(String id, String name, String description, int cooldown, SkillType type) {
        this.id = id;
        this.name = name;
        this.descrption = description;
        this.cooldown = cooldown;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescrption() {
        return descrption;
    }

    public int getCooldown() {
        return cooldown;
    }

    public abstract void activate(Player p);

    public SkillType getType() {
        return this.type;
    }
}
