package com.forgefrontier.forgefrontier.stashes;

import org.bukkit.Material;

public class Stash {

    String friendlyName;
    Material materialRepresentation;

    public Material getMaterialRepresentation() {
        return this.materialRepresentation;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }
}
