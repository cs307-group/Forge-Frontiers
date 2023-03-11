package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class BazaarDB extends DBConnection {

    public BazaarDB(Connection dbConn) {
        super(dbConn);
    }

    public boolean setLookup(int slotID, String itemMat, String name, String lore, String data) {
        String query = "INSERT INTO bazaar_lookup " +
                "(slot_id, item_material, item_name, item_lore, custom_data) " +
                "VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(query);
            preparedStatement.setInt(1, slotID);
            preparedStatement.setString(2, itemMat);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lore);
            preparedStatement.setString(5, data);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[QUERY FAILURE] Bazaar Set Lookup\n" + e.getMessage());
            return false;
        }
    }

    public boolean deleteSlotID(int slot) {
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(
                    "DELETE FROM public.bazaar_lookup WHERE bazaar_lookup.slot_id = ?");
            preparedStatement.setInt(1, slot);

            ForgeFrontier.getInstance().getLogger().log(Level.INFO,
                    "Clearing Bazaar Slot: " + slot);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY BAZAAR LOOKUP CLEAR FAILURE]\n" + e.getMessage());
            return false;
        }

    }


    public ArrayList<Integer> indexList() throws SQLException {
        String query = "SELECT bazaar_lookup.\"SlotID\" FROM public.bazaar_lookup ORDER BY \"SlotID\" ASC;";
        ConnectionSet cs = createQuery(query);
        ArrayList<Integer> idxs = new ArrayList<>();
        while (cs.getResult().next()) {
                ResultSet rs = cs.getResult();
                int itemid = rs.getInt("SlotID");
                idxs.add(itemid);
        }
        if(cs != null)
            cs.close();
        return null;
    }


}
