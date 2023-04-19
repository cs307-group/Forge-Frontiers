package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class ShopDB extends DBConnection {

    public ShopDB(Connection dbConn) {
        super(dbConn);
    }

    public boolean setListingSold(UUID listing, UUID buyer, long time_bought) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(
                    "UPDATE public.shop SET buyer_id = ?, date_sold = ? WHERE public.shop.id_ = ?");
            preparedStatement.setString(1, buyer.toString());
            preparedStatement.setLong(2, time_bought);
            preparedStatement.setString(3, listing.toString());
            ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                    "Updated Listing " + listing + " as sold.");
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
                    "Deleting listing: " + listing);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY SHOP REMOVE FAILURE]\n" + e.getMessage());
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


}
