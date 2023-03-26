package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class StashInstance implements Locatable {

    Stash stash;
    Location location;
    Map<String, Integer> stashContents;

    public StashInstance(Stash stash, Location location) {
        this.stash = stash;
        this.location = location;
        this.stashContents = new HashMap<>();
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int getX() {
        return location.getBlockX();
    }

    @Override
    public int getY() {
        return location.getBlockY();
    }

    @Override
    public int getZ() {
        return location.getBlockZ();
    }

    @Override
    public boolean isAt(int x, int y, int z) {
        return this.getX() == x && this.getY() == y && this.getZ() == z;
    }

    public Stash getStash() {
        return this.stash;
    }

    public int getAmount(StashItem stashItem) {
        if(stashContents.containsKey(stashItem.getItem().getCode())) {
            return stashContents.get(stashItem.getItem().getCode());
        }
        return 0;
    }

    public Inventory getInventory() {
        return new StashInventoryHolder(this).getInventory();
    }

    public void setAmount(StashItem stashItem, int amount) {
        stashContents.put(stashItem.getItem().getCode(), amount);
    }
}
