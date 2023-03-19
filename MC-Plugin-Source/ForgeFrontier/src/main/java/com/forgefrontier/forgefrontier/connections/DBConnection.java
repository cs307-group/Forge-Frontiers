package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.player.PlayerStat;
import com.forgefrontier.forgefrontier.shop.ShopListing;
import org.apache.commons.lang.SystemUtils;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;


public class DBConnection {
    protected Connection dbConn;

    public DBConnection(Connection existingConn) {
        this.dbConn = existingConn;
    }

    /**
     * @param query Query to execute
     * @return queried data
     */
    public ConnectionSet createQuery(String query) {
        try {
            Statement s = this.dbConn.createStatement();
            ResultSet rs = s.executeQuery(query);
            return new ConnectionSet(s, rs);
            //ForgeFrontier.getInstance().getLogger().log(Level.INFO, "Query Executed");
        } catch (SQLException se) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[FATAL] SQL Exception. \n" + se.getMessage());
            return null;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[FATAL] Error querying database!\n" + e.getMessage());
            return null;
        }
    }

}
