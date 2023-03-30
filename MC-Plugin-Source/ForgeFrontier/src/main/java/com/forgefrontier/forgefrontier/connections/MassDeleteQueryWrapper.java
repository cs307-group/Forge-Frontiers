package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * DELETE FROM __table__ WHERE
 * (_col = _val || _col2 = _val2 || _col3 = _val3 || .., || ) AND (CONDITIONS)
 */
public class MassDeleteQueryWrapper {
    private class DeletePair {
        public DeletePair(String col, String val) { this.col = col; this.val = val;}
        public String col;
        public String val;
    }
    String table;
    List<String> conditions;
    ArrayList<DeletePair> deleteables;
    TreeMap<Integer, String> keyIndices;
    Map<String, Object> values;

    public MassDeleteQueryWrapper() {
        this.deleteables = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.keyIndices = new TreeMap<>();
        this.values = new HashMap<>();
    }

    public void setTable(String table) {
        this.table = table;
    }
    public void addCondition(String condition, String... keys) {
        for(String key: keys) {
            int index;
            while((index = condition.indexOf("%" + key  + "%")) != -1) {
                keyIndices.put(index, key);
                condition = condition.replaceFirst("%" + key + "%", "?");
            }
        }
        this.conditions.add(condition);
    }

    public void addDeleteable(String col, String value) {
        deleteables.add(new DeletePair(col, value));
    }

    public void addValue(String key, Object value) {
        this.values.put(key, value);
    }

    public boolean executeSyncQuery(Connection databaseConnection) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("DELETE FROM ");
            queryBuilder.append(this.table);

            queryBuilder.append(" WHERE (");
            for (int i = 0; i < deleteables.size(); i++) {
                DeletePair dp = deleteables.get(i);
                queryBuilder.append(dp.col).append(" = '").append(dp.val).append("'");
                if (i != deleteables.size() - 1)
                    queryBuilder.append(" OR ");
            }
            queryBuilder.append(")");
            if (conditions.size() > 0) {
                queryBuilder.append(" AND (");
                for (int i = 0; i < conditions.size(); i++) {
                    queryBuilder.append(conditions.get(i));
                    if (i != conditions.size() - 1)
                        queryBuilder.append(") AND (");
                }
                queryBuilder.append(")");
            }
            String finalStatement = queryBuilder.toString();
            ForgeFrontier.getInstance().getLogger()
                    .log(Level.INFO,"Executing Mass Delete Query:\n\n" + finalStatement);
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(finalStatement);
            int index = 1;
            while(!keyIndices.isEmpty()) {
                String key = keyIndices.pollFirstEntry().getValue();
                Object value = values.get(key);
                if(value == null) throw new RuntimeException("Unable to insert null value into condition for key \"" + key + "\".");
                if(value instanceof String)
                    preparedStatement.setString(index, (String) value);
                if(value instanceof Long)
                    preparedStatement.setLong(index, (Long) value);
                if(value instanceof Double)
                    preparedStatement.setDouble(index, (Double) value);
                if(value instanceof Integer)
                    preparedStatement.setInt(index, (Integer) value);
                if(value instanceof Boolean)
                    preparedStatement.setBoolean(index, (Boolean) value);
                index += 1;
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void executeAsyncQuery(Connection databaseConnection, Consumer<Boolean> callback) {
        new Thread(() -> {
            callback.accept(executeSyncQuery(databaseConnection));
        }).start();
    }

}
