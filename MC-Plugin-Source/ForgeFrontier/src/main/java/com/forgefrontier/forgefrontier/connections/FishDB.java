package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.InsertQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.SelectQueryWrapper;
import com.forgefrontier.forgefrontier.connections.wrappers.UpdateQueryWrapper;
import com.forgefrontier.forgefrontier.fishing.PlayerFishStat;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class FishDB extends DBConnection {

    private boolean currentlyUpdating;
    private Thread updateThread;

    public FishDB(Connection existingConn) {
        super(existingConn);
        currentlyUpdating = false;
        updateThread = null;
    }

    public boolean stillUpdating() {
        return currentlyUpdating;
    }

    public HashMap<UUID, PlayerFishStat> loadFishingStats() {
        HashMap<UUID, PlayerFishStat> hmap = new HashMap<>();

        SelectQueryWrapper wrapper = new SelectQueryWrapper();
        wrapper.setTable("public.fishing_stats");
        wrapper.setFields("player_id", "fishcaught", "fishlevel", "common", "uncommon", "rare", "sr", "ur", "legendary");
        ResultSet rs = wrapper.executeSyncQuery(dbConn);
        try {
            while (rs != null && rs.next()) {
                UUID playerID = UUID.fromString(rs.getString("player_id"));
                long fishcaught = rs.getLong("fishcaught");
                int fishlevel = rs.getInt("fishlevel");
                int c = rs.getInt("common");
                int uc = rs.getInt("uncommon");
                int rare = rs.getInt("rare");
                int sr = rs.getInt("sr");
                int ur = rs.getInt("ur");
                int leg = rs.getInt("legendary");
                hmap.put(playerID, new PlayerFishStat(playerID,fishcaught,fishlevel,c,uc,rare,sr,ur,leg, false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hmap;
    }

    public void insertNewPlayer(PlayerFishStat pfs) {
        InsertQueryWrapper wrapper = new InsertQueryWrapper();
        wrapper.setTable("public.fishing_stats");
        wrapper.fullInsert("player_id",pfs.getPlayerID().toString());
        wrapper.fullInsert("fishcaught",pfs.getFishCaught());
        wrapper.fullInsert("fishlevel",pfs.getLevel());
        wrapper.executeAsyncQuery(dbConn,(e) -> {
            if (!e.isSuccess()) {
                ForgeFrontier.getInstance().getLogger()
                        .log(Level.SEVERE,"Failed to insert player fish stats.");
            }
        });
    }
    public void saveFishJob(HashMap<UUID, PlayerFishStat> stats) {
        saveFishJob(stats, null);
    }

    public void saveFishJob(HashMap<UUID, PlayerFishStat> stats, CommandSender sender) {
        // Already doing job
        if (currentlyUpdating) {
            return;
        }

        this.updateThread = new Thread(() -> {
            for (PlayerFishStat pfs : stats.values()) {
                if (!pfs.isModified()) continue;
                UpdateQueryWrapper wrapper = new UpdateQueryWrapper();
                wrapper.setTable("public.fishing_stats");
                wrapper.fullInsert("player_id", pfs.getPlayerID().toString());
                wrapper.fullInsert("fishcaught", pfs.getFishCaught());
                wrapper.fullInsert("fishlevel", pfs.getLevel());
                wrapper.fullInsert("common", pfs.getCommon());
                wrapper.fullInsert("uncommon", pfs.getUncommon());
                wrapper.fullInsert("rare", pfs.getRare());
                wrapper.fullInsert("sr", pfs.getSuperrare());
                wrapper.fullInsert("ur", pfs.getUltrarare());
                wrapper.fullInsert("legendary", pfs.getLegendary());
                wrapper.addCondition("player_id = %id%","id");
                wrapper.insertValue("id",pfs.getPlayerID().toString());
                wrapper.executeSyncQuery(dbConn);
                pfs.setModified(false);
            }
            ForgeFrontier.getInstance().getLogger().log(Level.INFO,"Fish Stats sent to DB.");
            this.currentlyUpdating = false;
            if (sender != null)
                sender.sendMessage("Finished saving fishing stats.");

        });
        this.updateThread.start();
        this.currentlyUpdating = true;
    }


}
