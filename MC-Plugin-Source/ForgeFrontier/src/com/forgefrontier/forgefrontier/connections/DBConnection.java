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
    public ShopDB shopDB;
    public BazaarDB bazaarDB;

    public DBConnection(Connection existingConn) {
        this.dbConn = existingConn;
    }

    public DBConnection() {}

    public boolean setupDatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[FF DATABASE] COULD NOT FIND POSTGRES DRIVER!");
            return false;
        }
        try (InputStream in = getClass().getResourceAsStream("/.env");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String connStr = reader.readLine();
            String uname = reader.readLine();
            String password = reader.readLine();
            reader.close();
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, "[FF DATABASE] Attempting to connect to DB");
            Properties props = new Properties();
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", "verify-full");
            props.setProperty("user", uname);
            props.setProperty("password", password);
            String home_dir;
            if (SystemUtils.IS_OS_LINUX) {
                home_dir = System.getProperty("HOME");
                home_dir += "/.postgresql/root.crt";

            } else if (SystemUtils.IS_OS_WINDOWS) {
                home_dir = System.getenv("UserProfile");
                home_dir += "\\.postgresql\\root.crt";
            } else {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[FF DATABASE] COULD NOT FIND SSL ROOT CERTIFICATE...");
                return false;
            }
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, "[FF DATABASE] Loading SSL Cert from " + home_dir);

            props.setProperty("sslcert", home_dir);
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, "[FF DATABASE] Establishing connection...");
            Class.forName("org.postgresql.Driver");
            this.dbConn = DriverManager.getConnection(connStr, uname, password);

            if (dbConn != null) {
                ForgeFrontier.getInstance().getLogger().log(Level.INFO, "[FF DATABASE] Connected to Database.");
            } else {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[FF DATABASE] Connection to Database FAILED...");
                return false;
            }
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[FF DATABASE] " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean init() {
        boolean connected = this.setupDatabaseConnection();
        if (!connected) return false;
        this.shopDB = new ShopDB(dbConn);
        this.bazaarDB = new BazaarDB(dbConn);
        return true;
    }

    /**
     * Queries database tables, if returned successfully, the database is probably connected.
     *
     * @return success
     */
    public boolean testConnection() {
        try {
            Statement s = this.dbConn.createStatement();
            ResultSet rs = s.executeQuery("select table_name from information_schema.tables " +
                    "WHERE table_schema = 'public';\n");
            StringBuilder tbls = new StringBuilder();
            tbls.append("\n\t[TEST CONNECTION - PUBLIC TABLES]:\n");
            while (rs.next()) {
                tbls.append("\t- ").append(rs.getString("table_name")).append("\n");
            }
            rs.close();
            s.close();
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, tbls.toString());

        } catch (SQLException se) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[FATAL] SQL Exception. \n" + se.getMessage());
            return false;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[FATAL] Error querying database!\n" + e.getMessage());
            return false;
        }
        return true;
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




    /**
     * Gets existing player link information before creating a new one.
     *
     * Upon completion, runs the consumer giving the map of values of existing link, if it exists.
     */
    public void getExistingPlayerLink(UUID playerUuid, Consumer<Map<String, Object>> consumer) {
        new Thread(() -> {
            try {
                PreparedStatement ps = this.dbConn.prepareStatement(("SELECT link_id, player_uuid, link_code, bool_used from public.links " +
                        "WHERE player_uuid = ?"));
                ps.setString(1, playerUuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Map<String, Object> resultMap = new HashMap<>();
                    long id = rs.getLong("link_id");
                    String linkCode = rs.getString("link_code");
                    boolean boolUsed = rs.getBoolean("bool_used");
                    resultMap.put("link_id", id);
                    resultMap.put("link_code", UUID.fromString(linkCode));
                    resultMap.put("bool_used", boolUsed);
                    consumer.accept(resultMap);
                } else {
                    consumer.accept(null);
                }
                rs.close();
                ps.close();
            } catch (SQLException se) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                        "[DB] SQL Exception. \n" + se.getMessage());
                consumer.accept(null);
            }
        }).start();
    }

    /**
     * Gets existing player link information before creating a new one.
     *
     * Upon completion, runs the consumer giving whether or not the query succeeded
     */
    public void createPlayerLink(UUID uniqueId, UUID linkCode, Consumer<Boolean> consumer) {
        new Thread(() -> {
            try {
                PreparedStatement preparedStatement = dbConn.prepareStatement("INSERT INTO public.links " +
                        "(player_uuid, link_code) " +
                        "VALUES (?, ?);");
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, linkCode.toString());
                preparedStatement.executeUpdate();
                consumer.accept(true);
            } catch (SQLException e) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                        "[DB] SQL Exception. \n" + e.getMessage());
                consumer.accept(false);
            }
        }).start();
    }


    /**
     * Gets existing player stat information before creating new information.
     */
    public boolean createPlayerStats(UUID uniqueId, PlayerStat[] stats) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement("INSERT INTO public.stats " +
                    "(player_uuid, current_health, HP, ATK, STR, DEX, CRATE, CDMG, DEF) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, uniqueId.toString());
            preparedStatement.setDouble(2, stats[0].getStatValue());
            preparedStatement.setInt(3, stats[0].getStatValue());
            preparedStatement.setInt(4, stats[1].getStatValue());
            preparedStatement.setInt(5, stats[2].getStatValue());
            preparedStatement.setInt(6, stats[3].getStatValue());
            preparedStatement.setInt(7, stats[4].getStatValue());
            preparedStatement.setInt(8, stats[5].getStatValue());
            preparedStatement.setInt(9, stats[6].getStatValue());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[DB] SQL Exception. \n" + e.getMessage());
            return false;
        }
    }

    /**
     * Updates player stat information with new information.
     */
    public boolean updatePlayerStats(UUID uniqueId, double currentHealth, PlayerStat[] stats) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(
                    "UPDATE public.stats " +
                            "(player_uuid, current_health, stats.\"HP\", stats.\"ATK\", stats.\"STR\", stats.\"DEX\", stats.\"CRATE\", stats.\"CDMG\", stats.\"DEF\") " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, uniqueId.toString());
            preparedStatement.setDouble(2, currentHealth);
            preparedStatement.setInt(3, stats[0].getStatValue());
            preparedStatement.setInt(4, stats[1].getStatValue());
            preparedStatement.setInt(5, stats[2].getStatValue());
            preparedStatement.setInt(6, stats[3].getStatValue());
            preparedStatement.setInt(7, stats[4].getStatValue());
            preparedStatement.setInt(8, stats[5].getStatValue());
            preparedStatement.setInt(9, stats[6].getStatValue());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY UPDATE PLAYER STATS FAILURE]\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Gets existing player link information before creating a new one.
     *
     * Upon completion, runs the consumer giving the map of values of existing link, if it exists.
     */
    public void getExistingPlayerStats(UUID playerUuid, Consumer<Map<String, Object>> consumer) {
        new Thread(() -> {
            try {
                PreparedStatement ps = this.dbConn.prepareStatement(("SELECT " +
                        "player_uuid, current_health, stats.\"HP\", stats.\"ATK\", stats.\"STR\", stats.\"DEX\", stats.\"CRATE\", stats.\"CDMG\", stats.\"DEF\" from public.stats " +
                        "WHERE player_uuid = ?"));
                ps.setString(1, playerUuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Map<String, Object> resultMap = new HashMap<>();
                    double currHealth = rs.getDouble("current_health");
                    int hp = rs.getInt("HP");
                    int atk = rs.getInt("ATK");
                    int str = rs.getInt("STR");
                    int dex = rs.getInt("DEX");
                    int crate = rs.getInt("CRATE");
                    int cdmg = rs.getInt("CDMG");
                    int def = rs.getInt("DEF");
                    resultMap.put("current_health", currHealth);
                    resultMap.put("HP", hp);
                    resultMap.put("ATK", atk);
                    resultMap.put("STR", str);
                    resultMap.put("DEX", dex);
                    resultMap.put("CRATE", crate);
                    resultMap.put("CDMG", cdmg);
                    resultMap.put("DEF", def);
                    consumer.accept(resultMap);
                } else {
                    consumer.accept(null);
                }
                rs.close();
                ps.close();
            } catch (SQLException se) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                        "[DB] SQL Exception Existing Player Stat. \n" + se.getMessage());
                consumer.accept(null);
            }
        }).start();
    }
}
