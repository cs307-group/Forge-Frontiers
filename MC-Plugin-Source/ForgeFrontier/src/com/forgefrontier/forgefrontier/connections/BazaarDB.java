package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class BazaarDB extends DBConnection{

    public BazaarDB(Connection dbConn) {
        super(dbConn);
    }

    public boolean setListing() {
        // Check if slot exists in database


        return true;
    }

    public boolean indexExists() throws SQLException {

        String query = "SELECT id_, item_id, item_material, item_name, item_lore, " +
                "price, amount, lister_player_id, custom_data, player_name FROM public.shop WHERE buyer_id IS NULL";
        int success = 0;
        int total = 0;
        ConnectionSet cs = createQuery(query);
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
        if(cs != null)
            cs.close();
        return false;
    }


}
