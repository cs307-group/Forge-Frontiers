package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.apache.commons.lang.SystemUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;

public class DatabaseManager extends Manager {

    Connection dbConn;

    ShopDB shopDB;
    BazaarDB bazaarDB;
    GeneratorDB generatorDB;
    StashDB stashDB;
    PlayerDB playerDB;
    FishDB fishDB;
    ConfigDB configDB;
    CraftingRecipeDB recipeDB;
    ManagementDB managementDB;

    public boolean connected = false;
    public DatabaseManager(ForgeFrontier plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        connected = this.setupDatabaseConnection();
        if (!connected)
            return;
        this.shopDB = new ShopDB(dbConn);
        this.bazaarDB = new BazaarDB(dbConn);
        this.generatorDB = new GeneratorDB(dbConn);
        this.stashDB = new StashDB(dbConn);
        this.playerDB = new PlayerDB(dbConn);
        this.fishDB = new FishDB(dbConn);
        this.configDB = new ConfigDB(dbConn);
        this.recipeDB = new CraftingRecipeDB(dbConn);
        this.managementDB = new ManagementDB(dbConn);
    }

    @Override
    public void disable() {

    }

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
            while (rs != null && rs.next()) {
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

    public ShopDB getShopDB() {
        return shopDB;
    }

    public BazaarDB getBazaarDB() {
        return bazaarDB;
    }

    public GeneratorDB getGeneratorDB() {
        return generatorDB;
    }

    public StashDB getStashDB() {
        return stashDB;
    }

    public PlayerDB getPlayerDB() {
        return playerDB;
    }

    public FishDB getFishDB() { return fishDB; }

    public ConfigDB getConfigDB() { return configDB; }

    public CraftingRecipeDB getRecipeDB() { return recipeDB; }

    public ManagementDB getManagementDB() { return managementDB; }
}
