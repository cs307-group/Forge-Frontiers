package com.forgefrontier.forgefrontier.stashes;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Locatable;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StashInstance implements Locatable {

    Island island;
    String databaseId;
    Stash stash;
    Location location;
    Map<String, Integer> stashContents;

    private static final JSONParser parser;

    static {
        parser = new JSONParser();
    }

    public StashInstance(Stash stash, Location location) {
        this.stash = stash;
        this.location = location;
        this.stashContents = new HashMap<>();
    }


    public StashInstance(Stash stash, Location location, String jsonContents, String databaseId) {
        this.databaseId = databaseId;
        this.stash = stash;
        this.location = location;
        this.setContentsJson(jsonContents);
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

    public String getJsonContentsString() {
        return JSONObject.toJSONString(stashContents);
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseId() {
        return this.databaseId;
    }

    public void setContentsJson(String jsonContents) {
        JSONObject contentsObj = null;
        try {
            contentsObj = (JSONObject) parser.parse(jsonContents);
        } catch (ParseException e) {
            e.printStackTrace();
            ForgeFrontier.getInstance().getLogger().severe("Unable to import stash contents with JSON string: " + jsonContents);
        }
        this.stashContents = (Map<String, Integer>) contentsObj;

    }

    public Island getIsland() {
        if(island == null) {
            Location l = this.getLocation().clone();
            l.setY(0);
            Optional<Island> island = BentoBox.getInstance().getIslandsManager().getIslandAt(l);
            if(!island.isPresent())
                return null;
            this.island = island.get();
        }
        return this.island;
    }

}
