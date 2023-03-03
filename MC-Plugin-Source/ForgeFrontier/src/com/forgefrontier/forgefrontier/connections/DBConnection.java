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
    Connection dbConn;

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

    public boolean insertShopListing(
            String id, String itemId, String material, String name, String lore, String price, int amount,
            String listerID, String buyerID, long dateSold, long dateListed, String customData, String playerName) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement("INSERT INTO public.shop " +
                    "(id_, item_id, item_material, item_name, item_lore, price, " +
                    "amount, lister_player_id, buyer_id, date_sold, created_at, custom_data, player_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, itemId);
            preparedStatement.setString(3, material);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, lore);
            preparedStatement.setString(6, price);
            preparedStatement.setInt(7, amount);
            preparedStatement.setString(8, listerID);
            preparedStatement.setString(9, buyerID);
            preparedStatement.setLong(10, dateSold);
            preparedStatement.setLong(11, dateListed);
            preparedStatement.setString(12, customData);
            preparedStatement.setString(13, playerName);
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, "[QUERY]\n" + preparedStatement);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY FAILURE]\n" + e.getMessage());
            return false;
        }
    }

    public void loadListings() {
        ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                "Attempting to load shop database...");
        String query = "SELECT id_, item_id, item_material, item_name, item_lore, " +
                "price, amount, lister_player_id, custom_data, player_name FROM public.shop WHERE buyer_id IS NULL";
        int success = 0;
        int total = 0;
        ConnectionSet cs = createQuery(query);
        try {
            while (cs.getResult().next()) {
                ResultSet rs = cs.getResult();
                String listingidStr = rs.getString("id_");
                if (listingidStr.equals("") || listingidStr.equals("-1")) continue;
                UUID listingid;
                try {
                    listingid = UUID.fromString(listingidStr);
                } catch (IllegalArgumentException e) {
                    ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "[LOAD FAIL]\n" + e.getMessage());
                    continue;
                }
                String itemid = rs.getString("item_id");
                String material = rs.getString("item_material");
                String name = rs.getString("item_name");
                String lore = rs.getString("item_lore");
                Double price = Double.parseDouble(rs.getString("price"));
                int amount = rs.getInt("amount");
                String listerStr = rs.getString("lister_player_id");
                UUID lister = null;
                try {
                    lister = UUID.fromString(listerStr);
                } catch (IllegalArgumentException e) {
                    ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "[Invalid ListerID]\n" + e.getMessage());
                }
                String custom_data = rs.getString("custom_data");
                String playerName = rs.getString("player_name");
                total++;
                if (ForgeFrontier.getInstance().getPlayerShop().loadListing(
                        listingid, itemid, material, name, lore,
                        price, amount, lister, custom_data, playerName)) {
                    ForgeFrontier.getInstance().getLogger().log(Level.INFO, "Listing Loaded: " + listingid);
                    success++;
                } else {
                    ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "[LOAD FAIL]\n" + listingid + " - " + name);
                }
            }
            ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                    "Loaded: " + success + "/" + total + " items from DB into shop.");
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY FAILURE]\n" + e.getMessage());
            cs.close();
            return;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.WARNING, "[LOAD ERROR]\n" + e.getMessage());
        }
        if(cs != null)
            cs.close();

    }

    public boolean setListingSold(UUID listing, UUID buyer, long time_bought) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(
                    "UPDATE public.shop SET buyer_id = ?, date_sold = ? WHERE public.shop.id_ = ?");
            preparedStatement.setString(1, buyer.toString());
            preparedStatement.setLong(2, time_bought);
            preparedStatement.setString(3, listing.toString());
            ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                    "Updated Listing " + listing.toString() + " as sold.");
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY SHOP SOLD FAILURE]\n" + e.getMessage());
            return false;
        }
    }

    public boolean removeListing(UUID listing) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(
                    "DELETE FROM public.shop WHERE shop.id_ = ?");
            preparedStatement.setString(1, listing.toString());

            ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                    "Deleting listing: " + listing.toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY SHOP REMOVE FAILURE]\n" + e.getMessage());
            return false;
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
                    "(player_uuid, current_health, hp, atk, str, dex, crate, cdmg, def) " +
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
                            "(player_uuid, current_health, hp, atk, str, dex, crate, cdmg, def) " +
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
                        "player_uuid, current_health, hp, atk, str, dex, crate, cdmg, def from public.stats " +
                        "WHERE player_uuid = ?"));
                ps.setString(1, playerUuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Map<String, Object> resultMap = new HashMap<>();
                    double currHealth = rs.getDouble("current_health");
                    int hp = rs.getInt("hp");
                    int atk = rs.getInt("atk");
                    int str = rs.getInt("str");
                    int dex = rs.getInt("dex");
                    int crate = rs.getInt("crate");
                    int cdmg = rs.getInt("cdmg");
                    int def = rs.getInt("def");
                    resultMap.put("current_health", currHealth);
                    resultMap.put("hp", hp);
                    resultMap.put("atk", atk);
                    resultMap.put("str", str);
                    resultMap.put("dex", dex);
                    resultMap.put("crate", crate);
                    resultMap.put("cdmg", cdmg);
                    resultMap.put("def", def);
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
}
