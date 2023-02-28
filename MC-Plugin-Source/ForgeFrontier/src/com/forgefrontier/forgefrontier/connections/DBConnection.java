package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.apache.commons.lang.SystemUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;
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
             BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            String connStr = reader.readLine();
            String uname = reader.readLine();
            String password = reader.readLine();
            reader.close();
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, "[FF DATABASE] Attempting to connect to DB");
            Properties props = new Properties();
            props.setProperty("ssl", "true");
            props.setProperty("sslmode","verify-full");
            props.setProperty("user", uname);
            props.setProperty("password",password);
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
        }
        catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[FF DATABASE] " + e.getMessage());
            return false;
        }
    }

    public boolean test_connection() {
        try {
            Statement s = this.dbConn.createStatement();
            ResultSet rs = s.executeQuery("select table_name from information_schema.tables " +
                                            "WHERE table_schema = 'public';\n");
            StringBuilder tbls = new StringBuilder();
            tbls.append("\n[TEST CONNECTION - PUBLIC TABLES]:\n");
            while (rs.next()) {
                tbls.append("\t").append(rs.getString("table_name")).append("\n");
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

}
