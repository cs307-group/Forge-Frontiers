package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.DeleteQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.InsertQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.SelectQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.UpdateQueryWrapper;
import com.forgefrontier.forgefrontier.generators.Generator;
import com.forgefrontier.forgefrontier.generators.GeneratorInstance;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public class GeneratorDB extends DBConnection {

    public enum Status {
        UPDATE_NEEDED,
        UPDATE_NOT_NEEDED,
        ERROR
    }

    public GeneratorDB(Connection dbConn) {
        super(dbConn);
    }

    public void importGenerators() {
        new Thread(() -> {
            SelectQueryWrapper wrapper = new SelectQueryWrapper();
            wrapper.setTable("public.generator_instances");
            wrapper.setFields(
                "id_",
                "level",
                "generator_id",
                "last_collection_time",
                "location_x",
                "location_y",
                "location_z",
                "location_world"
            );
            ResultSet rs = wrapper.executeSyncQuery(dbConn);
            try {
                while(rs != null && rs.next()) {
                    String databaseId = rs.getString("id_");
                    String generatorId = rs.getString("generator_id");
                    int level = rs.getInt("level");
                    long lastCollectionTime = rs.getLong("last_collection_time");
                    int x = rs.getInt("location_x");
                    int y = rs.getInt("location_y");
                    int z = rs.getInt("location_z");
                    String world = rs.getString("location_world");
                    Location location = new Location(Bukkit.getWorld(world), x, y, z);
                    Generator generator = ForgeFrontier.getInstance().getGeneratorManager().getGenerator(generatorId);
                    if(generator == null) {
                        ForgeFrontier.getInstance().getLogger().severe("Unable to find generator \"" + generatorId + "\" when importing an instance.");
                        return;
                    }
                    GeneratorInstance instance = new GeneratorInstance(generator, location, level, lastCollectionTime, databaseId);
                    boolean success = ForgeFrontier.getInstance().getGeneratorManager().addGeneratorInstance(instance);
                    //System.out.println("Adding generator " + instance.getLocation() + ": success: " + success);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void insertGenerator(GeneratorInstance instance, Consumer<InsertQueryWrapper.InsertResult> callback) {
        InsertQueryWrapper wrapper = new InsertQueryWrapper();
        wrapper.setTable("public.generator_instances");
        wrapper.fullInsert("id_", UUID.randomUUID().toString());
        wrapper.fullInsert("generator_id", instance.getGenerator().getId());
        wrapper.fullInsert("island_id", instance.getIsland().getUniqueId());
        wrapper.fullInsert("level", instance.getLevelInt());
        wrapper.fullInsert("last_collection_time", instance.getLastCollectTime());
        wrapper.fullInsert("location_x", instance.getX());
        wrapper.fullInsert("location_y", instance.getY());
        wrapper.fullInsert("location_z", instance.getZ());
        wrapper.fullInsert("location_world", instance.getLocation().getWorld().getName());
        wrapper.executeAsyncQuery(this.dbConn, (insertResult) -> {
            if(insertResult.isSuccess()) {
                instance.setDatabaseId(insertResult.getInsertId());
            }
            callback.accept(insertResult);
        });
    }

    public void removeGenerator(GeneratorInstance instance) {
        DeleteQueryWrapper wrapper = new DeleteQueryWrapper();
        wrapper.setTable("public.generator_instances");
        wrapper.addCondition("id_ = %id%", "id");
        wrapper.addValue("id", instance.getDatabaseId());
        wrapper.executeAsyncQuery(this.dbConn, (success) -> {
            if(!success)
                ForgeFrontier.getInstance().getLogger().severe("Unable to remove generator instance from database. An error occurred when trying to do so.");
        });
    }

    public void updateGenerator(GeneratorInstance instance, Consumer<Status> updateNeededConsumer) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();

        wrapper.setTable("public.generator_instances");
        wrapper.setFields(
            "last_collection_time"
        );
        wrapper.addCondition("id_ = %id%", "id");
        wrapper.addValue("id", instance.getDatabaseId());
        wrapper.executeAsyncQuery(dbConn, (resultSet) -> {
            try {
                if(resultSet != null && resultSet.next()) {
                    long lastCollectionTime = resultSet.getLong("last_collection_time");
                    if(lastCollectionTime == instance.getLastCollectTime()) {
                        updateNeededConsumer.accept(Status.UPDATE_NOT_NEEDED);
                    } else {
                        instance.setLastCollectTime(Math.max(instance.getLastCollectTime(), lastCollectionTime));
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

    public void sendGeneratorUpdate(GeneratorInstance instance, final Consumer<Boolean> callback) {
        UpdateQueryWrapper wrapper = new UpdateQueryWrapper();

        wrapper.setTable("public.generator_instances");
        wrapper.fullInsert("last_collection_time", instance.getLastCollectTime());
        wrapper.fullInsert("level", instance.getLevelInt());
        wrapper.addCondition("id_ = %id%", "id");
        wrapper.insertValue("id", instance.getDatabaseId());
        wrapper.executeAsyncQuery(dbConn, callback);

    }

}
