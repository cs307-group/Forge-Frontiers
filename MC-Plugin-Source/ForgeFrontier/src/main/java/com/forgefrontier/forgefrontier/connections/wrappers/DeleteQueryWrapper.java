package com.forgefrontier.forgefrontier.connections.wrappers;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

public class DeleteQueryWrapper {

    String table;
    List<String> conditions;
    TreeMap<Integer, String> keyIndices;
    Map<String, Object> values;

    public DeleteQueryWrapper() {
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

    public void addValue(String key, Object value) {
        this.values.put(key, value);
    }

    public boolean executeSyncQuery(Connection databaseConnection) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("DELETE FROM ");
            queryBuilder.append(this.table);
            if(this.conditions.size() != 0) {
                queryBuilder.append(" WHERE (");
                for (int i = 0; i < conditions.size(); i++) {
                    queryBuilder.append(conditions.get(i));
                    if (i != conditions.size() - 1)
                        queryBuilder.append(") AND (");
                }
                queryBuilder.append(")");
            }
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(queryBuilder.toString());
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
