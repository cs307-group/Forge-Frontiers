package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarEntry;
import com.forgefrontier.forgefrontier.bazaarshop.BazaarStash;
import com.forgefrontier.forgefrontier.connections.wrappers.*;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
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

    public boolean insertListing(BazaarEntry entry) {
        String query = "INSERT INTO bazaar_orders " +
                "(order_id, order_type, lister_id, slot_id, amount, price, listdate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = dbConn.prepareStatement(query);

            preparedStatement.setString(1, entry.getEntryID().toString());
            preparedStatement.setBoolean(2, entry.getBType());
            preparedStatement.setString(3, entry.getListerID().toString());
            preparedStatement.setInt(4, entry.getSlotID());
            preparedStatement.setInt(5, entry.getAmount());
            preparedStatement.setDouble(6, entry.getPrice());
            preparedStatement.setTimestamp(7,entry.getListdate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[QUERY FAILURE] Bazaar Insert Order\n" + e.getMessage());
        }

        return true;
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


    public ArrayList<BazaarEntry> loadListings() {
        ArrayList<BazaarEntry> listings = new ArrayList<>();
        String query = "SELECT order_id, order_type, lister_id, slot_id, amount, price, listdate FROM bazaar_orders;";
        ConnectionSet cs = createQuery(query);
        try {
            while (cs.getResult().next()) {
                ResultSet rs = cs.getResult();
                UUID orderID = UUID.fromString(rs.getString("order_id"));
                BazaarEntry.EntryType orderType = rs.getBoolean("order_type") ?
                        BazaarEntry.EntryType.BUY : BazaarEntry.EntryType.SELL;
                UUID listerID = UUID.fromString(rs.getString("lister_id"));
                int slotID = rs.getInt("slot_id");
                int amount = rs.getInt("amount");
                double price = rs.getDouble("price");
                Timestamp listDate = rs.getTimestamp("listdate");

                BazaarEntry bzrEntry = new BazaarEntry(orderID,orderType,slotID,amount,price,listerID,listDate);
                listings.add(bzrEntry);
            }
            return listings;


        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[QUERY FAILURE IN LOADING BAZAAR LOOKUP]\n" + e.getMessage());
            return listings;
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[FAILURE IN LOADING BAZAAR LOOKUP]\n" + e.getMessage());
            return listings;
        }
    }

    /**
     * Load lookup items of Bazaar
     * @param size Non-Inclusive, max index. Lookup [0, size)
     * @return Arraylist of lookup items
     */
    public ArrayList<ItemStack> loadLookup(int size) {
        ArrayList<ItemStack> out = new ArrayList<>(Collections.nCopies(size, null));

        String query = "SELECT slot_id, item_material, item_name, item_lore, custom_data FROM bazaar_lookup;";
        ConnectionSet cs = createQuery(query);
        try {
            while (cs.getResult().next()) {
                ResultSet rs = cs.getResult();
                int slotID = rs.getInt("slot_id");
                String material = rs.getString("item_material");
                String name = rs.getString("item_name");
                String lore = rs.getString("item_lore");
                String custom_data = rs.getString("custom_data");
                if (custom_data.isEmpty()) {
                    out.set(slotID, new ItemStackBuilder(material).setDisplayName(name).setFullLore(lore).build());
                } else {
                    // Create custom item
                    CustomItemInstance cii = CustomItemManager.getInstanceFromData(custom_data);
                    if (cii == null) continue;
                    out.set(slotID, cii.asItemStack());
                }
            }
            return out;
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().log(Level.SEVERE,
                    "[QUERY FAILURE IN LOADING BAZAAR LOOKUP]\n" + e.getMessage());
            return out;
        }

    }

    public boolean updateOrder(UUID old, BazaarEntry updated) {
        UpdateQueryWrapper wrapper = new UpdateQueryWrapper();
        if (updated.getListdate() == null) updated.setListdate(new Timestamp(System.currentTimeMillis()));
        wrapper.setTable("public.bazaar_orders");
        //wrapper.fullInsert("order_id", updated.getEntryID().toString());
        wrapper.fullInsert("order_type", updated.getBType());
        wrapper.fullInsert("lister_id", updated.getListerID().toString());
        wrapper.fullInsert("slot_id", updated.getSlotID());
        wrapper.fullInsert("amount", updated.getAmount());
        wrapper.fullInsert("price", updated.getPrice());
        wrapper.fullInsert("listdate", updated.getListdate());
        wrapper.addCondition("order_id = %oid%", "oid");
        wrapper.insertValue("oid", old.toString());

        return wrapper.executeSyncQuery(this.dbConn);
    }
    public boolean massDeleteOrders(ArrayList<BazaarEntry> toDelete) {
        if (toDelete.size() == 0) return true;
        MassDeleteQueryWrapper wrapper = new MassDeleteQueryWrapper();
        wrapper.setTable("public.bazaar_orders");
        for (BazaarEntry be : toDelete)
            wrapper.addDeleteable("order_id", be.getEntryID().toString());
        return wrapper.executeSyncQuery(this.dbConn);
    }

    public HashMap<UUID, ArrayList<BazaarStash>> loadStash() {
        HashMap<UUID, ArrayList<BazaarStash>> pStashes = new HashMap<>();
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.bazaar_redeem");
        wrapper.setFields("order_id", "player_id", "item_id", "amount");
        ResultSet rs = wrapper.executeSyncQuery(dbConn);

        try {
            while (rs != null && rs.next()) {
                UUID orderID = UUID.fromString(rs.getString("order_id"));
                UUID playerID = UUID.fromString(rs.getString("player_id"));
                int item_id = rs.getInt("item_id");
                int amount = rs.getInt("amount");
                pStashes.computeIfAbsent(playerID, k -> new ArrayList<>());
                pStashes.get(playerID).add(new BazaarStash(orderID, playerID, amount, item_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return pStashes;
        }
        return pStashes;
    }

    public boolean stashInsertUpdate(BazaarStash bs) {
        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.bazaar_redeem");
        wrapper.setFields("order_id", "player_id", "item_id", "amount");
        wrapper.addCondition("order_id = %orderid%","orderid");
        wrapper.addValue("orderid", bs.getOrderID().toString());
        ResultSet rs = wrapper.executeSyncQuery(dbConn);
        int prevTotal = 0;
        try {
            if (rs != null && rs.next()) {
                prevTotal += rs.getInt("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (prevTotal != 0) {
            // UPDATE
            UpdateQueryWrapper uwrap = new UpdateQueryWrapper();
            uwrap.setTable("public.bazaar_redeem");
            uwrap.fullInsert("amount", prevTotal + bs.getAmount());
            uwrap.addCondition("order_id = %order_id%", "order_id");
            uwrap.insertValue("order_id", bs.getAmount());
            return uwrap.executeSyncQuery(dbConn);
        } else {
            // INSERT
            InsertQueryWrapper iwrap = new InsertQueryWrapper();
            iwrap.setTable("public.bazaar_redeem");
            iwrap.fullInsert("order_id", bs.getOrderID().toString());
            iwrap.fullInsert("player_id", bs.getPlayerID().toString());
            iwrap.fullInsert("item_id", bs.getItemID());
            iwrap.fullInsert("amount", bs.getAmount());
            InsertQueryWrapper.InsertResult ir = iwrap.executeSyncQuery(dbConn);
            return ir.isSuccess();
        }
    }

    public void removeStash(BazaarStash stash, Consumer<Boolean> c) {
        DeleteQueryWrapper wrapper = new DeleteQueryWrapper();
        wrapper.setTable("public.bazaar_redeem");
        wrapper.addCondition("order_id = %id%", "id");
        wrapper.addValue("id", stash.getOrderID().toString());
        wrapper.executeAsyncQuery(dbConn,c);
    }

    public void removeOrder(BazaarEntry entry, Consumer<Boolean> c) {
        DeleteQueryWrapper wrapper = new DeleteQueryWrapper();
        wrapper.setTable("public.bazaar_orders");
        wrapper.addCondition("order_id = %id%", "id");
        wrapper.addValue("id", entry.getEntryID().toString());
        wrapper.executeAsyncQuery(this.dbConn, c);
    }

    public boolean removeOrderSync(BazaarEntry entry) {
        DeleteQueryWrapper wrapper = new DeleteQueryWrapper();
        wrapper.setTable("public.bazaar_orders");
        wrapper.addCondition("order_id = %id%", "id");
        wrapper.addValue("id", entry.getEntryID().toString());
        return wrapper.executeSyncQuery(this.dbConn);
    }

}
