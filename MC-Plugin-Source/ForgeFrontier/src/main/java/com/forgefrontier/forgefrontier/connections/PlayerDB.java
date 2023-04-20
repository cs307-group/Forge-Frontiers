package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.connections.wrappers.UpdateQueryWrapper;
import com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import com.forgefrontier.forgefrontier.player.PlayerStat;
import com.forgefrontier.forgefrontier.player.StatHolder;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

import static com.forgefrontier.forgefrontier.items.gear.statistics.StatEnum.ORDERED_STAT_ENUMS;

public class PlayerDB extends DBConnection {

    public PlayerDB(Connection existingConn) {
        super(existingConn);
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
                if (rs != null && rs.next()) {
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
    /*
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
     */

    /**
     * Updates player stat information with new information.
     */
    public void updatePlayerStats(UUID uniqueId, double currentHealth, Map<StatEnum, Integer> stats, int ascLevel, int tier) {
        new Thread(() -> {
            try {

                String sql = "INSERT INTO public.stats (player_uuid, current_health, \"HP\", \"ATK\", \"STR\", \"DEX\", " +
                        "\"CRATE\", \"CDMG\", \"DEF\", \"AscensionLevel\", \"Tier\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (player_uuid) DO UPDATE SET " +
                        "current_health = ?, \"HP\" = ?, \"ATK\" = ?, \"STR\" = ?, \"DEX\" = ?, \"CRATE\" = ?, \"CDMG\" " +
                        "= ?, \"DEF\" = ?, \"AscensionLevel\" = ?, \"Tier\" = ?;";

                PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setDouble(2, currentHealth);
                for (int i = 0; i < 7; i++) {
                    preparedStatement.setInt(3 + i, stats.get(ORDERED_STAT_ENUMS[i]));
                }
                preparedStatement.setInt(10, ascLevel);
                preparedStatement.setInt(11, tier);
                preparedStatement.setDouble(12, currentHealth);
                for (int i = 0; i < 7; i++) {
                    preparedStatement.setInt(13 + i, stats.get(ORDERED_STAT_ENUMS[i]));
                }
                preparedStatement.setInt(20, ascLevel);
                preparedStatement.setInt(21, tier);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "[QUERY UPDATE PLAYER STATS FAILURE]\n" + e.getMessage());
            }
        }).start();
    }

    public void updatePlayerStats(FFPlayer ffPlayer) {
        updatePlayerStats(ffPlayer.playerID, ffPlayer.getCurrentHealth(), ffPlayer.getStats(),
                ffPlayer.getAscension(), ffPlayer.getTier());
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
                        "player_uuid, current_health, stats.base_stats, stats.tutorial_state, stats.\"AscensionLevel\", stats.\"Tier\"" +
                        "from public.stats WHERE player_uuid = ?"));
                ps.setString(1, playerUuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs != null && rs.next()) {
                    Map<String, Object> resultMap = new HashMap<>();
                    double currHealth = rs.getDouble("current_health");
                    int asc = rs.getInt("AscensionLevel");
                    int tier = rs.getInt("Tier");
                    resultMap.put("current_health", currHealth);
                    resultMap.put("base_stats", rs.getString("base_stats"));
                    resultMap.put("tutorial_state", rs.getString("tutorial_state"));
                    resultMap.put("AscensionLevel", asc);
                    resultMap.put("Tier", tier);
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

    public void updatePlayerIsland(UUID playerUUID, String islandId, final Consumer<Boolean> callback) {
        UpdateQueryWrapper wrapper = new UpdateQueryWrapper();

        wrapper.setTable("public.user");
        wrapper.fullInsert("island_id", islandId);
        wrapper.addCondition("mc_user = %player_uuid%", "player_uuid");
        wrapper.insertValue("player_uuid", playerUUID.toString());
        wrapper.executeAsyncQuery(dbConn, callback);

    }

    public void updatePlayerBaseStats(FFPlayer ffPlayer) {
        UpdateQueryWrapper wrapper = new UpdateQueryWrapper();
        wrapper.setTable("public.stats");
        JSONWrapper json = new JSONWrapper();
        StatHolder holder = ffPlayer.getStats(FFPlayer.StatEquipmentType.BASE);
        for(StatEnum statEnum: ORDERED_STAT_ENUMS) {
            json.setInt(statEnum.toString(), holder.get(statEnum));
        }
        wrapper.fullInsert("base_stats", json.toJSONString());
        wrapper.addCondition("player_uuid = %uuid%", "uuid");
        wrapper.insertValue("uuid", ffPlayer.playerID.toString());

        wrapper.executeAsyncQuery(dbConn, (success) -> {
            if(!success)
                ForgeFrontier.getInstance().getLogger().log(Level.SEVERE, "An error occurred in updating base stats.");
        });
    }
}
