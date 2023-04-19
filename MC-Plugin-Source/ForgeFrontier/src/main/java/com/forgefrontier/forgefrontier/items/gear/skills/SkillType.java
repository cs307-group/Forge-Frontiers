package com.forgefrontier.forgefrontier.items.gear.skills;

public enum SkillType {

    WEAPON("Weapon"),
    ARMOR("Armor");

    private final String friendlyName;

    SkillType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

}
