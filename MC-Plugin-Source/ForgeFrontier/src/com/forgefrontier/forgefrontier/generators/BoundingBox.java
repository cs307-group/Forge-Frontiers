package com.forgefrontier.forgefrontier.generators;

import org.bukkit.Location;

public class BoundingBox {

    Location location;

    public BoundingBox(Location location) {
        this.location = location;
    }

    public boolean isColliding(Location location) {
        if(this.location.equals(location))
            return true;
        return false;
    }

}
