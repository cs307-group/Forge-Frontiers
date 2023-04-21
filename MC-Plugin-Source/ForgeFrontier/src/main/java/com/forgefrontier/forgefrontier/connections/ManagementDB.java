package com.forgefrontier.forgefrontier.connections;


import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.SelectQueryWrapper;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Read DB values for overall management of the game
 */
public class ManagementDB extends DBConnection {

    public ManagementDB(Connection existingConn) {
        super(existingConn);
    }

    public void getAsyncFeatureStates(Consumer<ResultSet> afterResult) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.feature_flags");
        wrapper.setFields("value");
        wrapper.executeAsyncQuery(dbConn, afterResult);
    }

    public HashMap<String, Boolean> getSyncFeatureStates() {
        HashMap<String, Boolean> hmap = new HashMap<>();
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.feature_flags");
        wrapper.setFields("value");
        ResultSet rs = wrapper.executeSyncQuery(dbConn);
        try {
            if (!rs.next()) { ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[ManagementDB] Could not load feature states."); return null;}
            String jsonVal = rs.getString("value");
            JSONWrapper jsonWrapper = new JSONWrapper(jsonVal);
            List<String> keys = jsonWrapper.getStringKeys();
            for (String k : keys) {
                hmap.put(k, jsonWrapper.getBool(k));
            }
        } catch (SQLException e) { e.printStackTrace(); return null; }
        return  hmap;
    }


}
