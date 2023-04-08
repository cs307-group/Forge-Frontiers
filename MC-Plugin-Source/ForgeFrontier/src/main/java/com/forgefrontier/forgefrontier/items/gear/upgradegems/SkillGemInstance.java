package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.skills.Skill;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SkillGemInstance extends CustomItemInstance {

    Skill skill;

    public SkillGemInstance(@Nullable ItemStack itemStack) {
        super(itemStack);
    }

    public Skill getSkill() {return skill;}

}