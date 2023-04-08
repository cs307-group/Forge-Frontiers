package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.DeleteQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.InsertQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.SelectQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.UpdateQueryWrapper;
import com.forgefrontier.forgefrontier.stashes.Stash;
import com.forgefrontier.forgefrontier.stashes.StashInstance;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public class StashDB extends DBConnection {

    public enum Status {
        UPDATE_NEEDED,
        UPDATE_NOT_NEEDED,
        ERROR
    }

    public StashDB(Connection dbConn) {
        super(dbConn);
    }

    public void importStashes() {
        new Thread(() -> {
            SelectQueryWrapper wrapper = new SelectQueryWrapper();
            wrapper.setTable("public.stash_instances");
            wrapper.setFields(
                "id_",
                "stash_id",
                "location_x",
                "location_y",
                "location_z",
                "location_world",
                "contents_json"
            );
            ResultSet rs = wrapper.executeSyncQuery(dbConn);
            try {
                while(rs != null && rs.next()) {
                    String databaseId = rs.getString("id_");
                    String stashId = rs.getString("stash_id");
                    int x = rs.getInt("location_x");
                    int y = rs.getInt("location_y");
                    int z = rs.getInt("location_z");
                    String world = rs.getString("location_world");
                    String jsonContents = rs.getString("contents_json");
                    Location location = new Location(Bukkit.getWorld(world), x, y, z);
                    Stash stash = ForgeFrontier.getInstance().getStashManager().getStash(stashId);
                    StashInstance instance = new StashInstance(stash, location, jsonContents, databaseId);
                    boolean success = ForgeFrontier.getInstance().getStashManager().addStashInstance(instance);
                    //System.out.println("Adding stash " + instance.getLocation() + ": success: " + success);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void insertStash(StashInstance instance, Consumer<InsertQueryWrapper.InsertResult> callback) {
        InsertQueryWrapper wrapper = new InsertQueryWrapper();
        wrapper.setTable("public.stash_instances");
        wrapper.fullInsert("id_", UUID.randomUUID().toString());
        wrapper.fullInsert("stash_id", instance.getStash().getId());
        wrapper.fullInsert("location_x", instance.getX());
        wrapper.fullInsert("location_y", instance.getY());
        wrapper.fullInsert("location_z", instance.getZ());
        wrapper.fullInsert("location_world", instance.getLocation().getWorld().getName());
        wrapper.fullInsert("contents_json", instance.getJsonContentsString());
        wrapper.fullInsert("island_id", instance.getIsland().getUniqueId());
        wrapper.executeAsyncQuery(this.dbConn, (insertResult) -> {
            if(insertResult.isSuccess()) {
                instance.setDatabaseId(insertResult.getInsertId());
            }
            callback.accept(insertResult);
        });
    }

    public void removeStash(StashInstance instance) {
        DeleteQueryWrapper wrapper = new DeleteQueryWrapper();
        wrapper.setTable("public.stash_instances");
        wrapper.addCondition("id_ = %id%", "id");
        wrapper.addValue("id", instance.getDatabaseId());
        wrapper.executeAsyncQuery(this.dbConn, (success) -> {
            if(!success)
                ForgeFrontier.getInstance().getLogger().severe("Unable to remove stash instance from database. An error occurred when trying to do so.");
        });
    }

    public void updateStash(StashInstance instance, Consumer<Status> updateNeededConsumer) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();

        wrapper.setTable("public.stash_instances");
        wrapper.setFields(
            "contents_json"
        );
        wrapper.addCondition("id_ = %id%", "id");
        wrapper.addValue("id", instance.getDatabaseId());
        wrapper.executeAsyncQuery(dbConn, (resultSet) -> {
            try {
                if(resultSet != null && resultSet.next()) {
                    String jsonContents = resultSet.getString("contents_json");
                    if(jsonContents.equals(instance.getJsonContentsString())) {
                        updateNeededConsumer.accept(Status.UPDATE_NOT_NEEDED);
                    } else {
                        instance.setContentsJson(jsonContents);
                        updateNeededConsumer.accept(Status.UPDATE_NEEDED);
                    }
                } else {
                    updateNeededConsumer.accept(Status.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                updateNeededConsumer.accept(Status.ERROR);
            }
        });
    }

    public void sendStashUpdate(StashInstance instance, final Consumer<Boolean> callback) {
        UpdateQueryWrapper wrapper = new UpdateQueryWrapper();


        wrapper.setTable("public.stash_instances");
        wrapper.fullInsert("contents_json", instance.getJsonContentsString());
        wrapper.addCondition("id_ = %id%", "id");
        wrapper.insertValue("id", instance.getDatabaseId());
        wrapper.executeAsyncQuery(dbConn, callback);

    }

}
