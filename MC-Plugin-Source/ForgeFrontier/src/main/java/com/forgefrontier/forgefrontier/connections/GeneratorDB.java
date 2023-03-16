package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.generators.GeneratorInstance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Consumer;

public class GeneratorDB extends DBConnection {

    public GeneratorDB(Connection dbConn) {
        super(dbConn);
    }

    public void importGenerators() {
        new Thread(() -> {
            SelectQueryWrapper wrapper = new SelectQueryWrapper();
            wrapper.setTable("public.generator_instances");
            wrapper.setFields(
                "level",
                "last_collection_time",
                "collected_amount",
                "location_x",
                "location_y",
                "location_z",
                "location_world",
                "owner_uuid"
            );
            ResultSet rs = wrapper.executeSyncQuery(dbConn);
        }).start();
    }

    public void insertGenerator(GeneratorInstance instance, Consumer<Boolean> callback) {
        InsertQueryWrapper wrapper = new InsertQueryWrapper();
        wrapper.setTable("public.generator_instances");
        wrapper.fullInsert("level", instance.getGeneratorLevel());
        wrapper.fullInsert("last_collection_time", instance.getLastCollectTime());
        wrapper.fullInsert("collected_amount", 0);
        wrapper.fullInsert("location_x", instance.getX());
        wrapper.fullInsert("location_x", instance.getY());
        wrapper.fullInsert("location_x", instance.getZ());
        wrapper.fullInsert("location_world", instance.getBoundingBox().getLocation().getWorld().getName());
        wrapper.fullInsert("owner_uuid", "");
        wrapper.executeAsyncQuery(this.dbConn, callback);
    }


}
