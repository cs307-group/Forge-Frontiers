package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InsertQueryWrapper {

    String query = "INSERT INTO bazaar_orders " +
            "(order_id, order_type, lister_id, slot_id, amount, price, listdate) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    String table;
    List<String> fields;
    Map<String, Object> values;

    public InsertQueryWrapper() {
        this.table = null;
        this.fields = null;
        this.values = null;
    }

    public void setTable(String table) {
        this.table = table;
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
            queryBuilder.append("INSERT INTO ");
            queryBuilder.append(this.table);
            queryBuilder.append(" (");
            for(int i = 0; i < fields.size(); i++) {
                queryBuilder.append(fields.get(i));
                if(i == fields.size() - 1)
                    queryBuilder.append(", ");
            }
            queryBuilder.append(") VALUES (");
            for(int i = 0; i < fields.size(); i++) {
                queryBuilder.append("?");
                if(i == fields.size() - 1)
                    queryBuilder.append(", ");
            }
            queryBuilder.append(")");
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(queryBuilder.toString());
            for(int i = 0; i < fields.size(); i++) {
                Object value = values.get(fields.get(i));
                if(value == null) throw new RuntimeException("Unable to insert null value into database for field \"" + fields.get(i) + "\".");
                if(value instanceof String)
                    preparedStatement.setString(i + 1, (String) value);
                if(value instanceof Long)
                    preparedStatement.setLong(i + 1, (Long) value);
                if(value instanceof Double)
                    preparedStatement.setDouble(i + 1, (Double) value);
                if(value instanceof Integer)
                    preparedStatement.setInt(i + 1, (Integer) value);
                if(value instanceof Boolean)
                    preparedStatement.setBoolean(i + 1, (Boolean) value);
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().severe(e.getMessage());
            return false;
        }
    }
}
