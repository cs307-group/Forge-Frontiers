package com.forgefrontier.forgefrontier.connections.wrappers;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateQueryWrapper {

    String table;
    List<String> conditions;
    TreeMap<Integer, String> keyIndices;
    List<String> fields;
    Map<String, Object> values;

    public UpdateQueryWrapper() {
        this.conditions = new ArrayList<>();
        this.keyIndices = new TreeMap<>();
        this.fields = new ArrayList<>();
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

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setFields(String... fields) {
        this.fields = Arrays.asList(fields);
    }

    public void addField(String field) {
        this.fields.add(field);
    }

    public void fullInsert(String field, Object value) {
        this.addField(field);
        this.insertValue(field, value);
    }

    public void insertValue(String field, Object value) {
        if(this.values.containsKey(field))
            throw new RuntimeException("Already inserted value for field \"" + field + "\".");
        this.values.put(field, value);
    }

    public void executeAsyncQuery(Connection databaseConnection, Consumer<Boolean> callback) {
        new Thread(() -> {
            callback.accept(this.executeSyncQuery(databaseConnection));;
        }).start();
    }

    public boolean executeSyncQuery(Connection databaseConnection) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ");
            queryBuilder.append(this.table);
            queryBuilder.append(" SET ");
            for(int i = 0; i < fields.size(); i++) {
                queryBuilder.append(fields.get(i));
                queryBuilder.append(" = ?");
                if(i != fields.size() - 1)
                    queryBuilder.append(", ");
            }
            if(this.conditions.size() != 0) {
                queryBuilder.append(" WHERE (");
                for (int i = 0; i < conditions.size(); i++) {
                    queryBuilder.append(conditions.get(i));
                    if (i != conditions.size() - 1)
                        queryBuilder.append(") AND (");
                }
                queryBuilder.append(")");
            }
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(queryBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
            int index;
            for(index = 1; index <= fields.size(); index++) {
                Object value = values.get(fields.get(index - 1));
                if(value == null) throw new RuntimeException("Unable to insert null value into database for field \"" + fields.get(index) + "\".");
                if(value instanceof String)
                    preparedStatement.setString(index, (String) value);
                else if(value instanceof Long)
                    preparedStatement.setLong(index, (Long) value);
                else if(value instanceof Double)
                    preparedStatement.setDouble(index, (Double) value);
                else if(value instanceof Integer)
                    preparedStatement.setInt(index, (Integer) value);
                else if(value instanceof Boolean)
                    preparedStatement.setBoolean(index, (Boolean) value);
                else if (value instanceof Timestamp)
                    preparedStatement.setTimestamp(index, (Timestamp) value);
                else
                    ForgeFrontier.getInstance().getLogger().severe("Unable to create sql query. Value is not a primitive type: " + value);
            }
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
                if (value instanceof Timestamp)
                    preparedStatement.setTimestamp(index, (Timestamp) value);
                index += 1;
            }
            //ForgeFrontier.getInstance().getLogger().log(Level.INFO,"UPDATE: " + preparedStatement.toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
