package com.forgefrontier.forgefrontier.connections.wrappers;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

public class InsertQueryWrapper {

    public static class InsertResult {
        public static final InsertResult FAILURE = new InsertResult(false, null);
        private boolean success;
        private String id;
        public InsertResult(String id) {
            this.success = true;
            this.id = id;
        }
        public InsertResult(boolean success, String id) {
            this.success = success;
            this.id = id;
        }
        public boolean isSuccess() {
            return this.success;
        }
        public String getInsertId() {
            return this.id;
        }
    }

    String query = "INSERT INTO bazaar_orders " +
            "(order_id, order_type, lister_id, slot_id, amount, price, listdate) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    String table;
    List<String> fields;
    Map<String, Object> values;

    public InsertQueryWrapper() {
        this.fields = new ArrayList<>();
        this.values = new HashMap<>();
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

    public void executeAsyncQuery(Connection databaseConnection, Consumer<InsertResult> callback) {
        new Thread(() -> {
            callback.accept(this.executeSyncQuery(databaseConnection));;
        }).start();
    }

    public InsertResult executeSyncQuery(Connection databaseConnection) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ");
            queryBuilder.append(this.table);
            queryBuilder.append(" (");
            for(int i = 0; i < fields.size(); i++) {
                queryBuilder.append(fields.get(i));
                if(i != fields.size() - 1)
                    queryBuilder.append(", ");
            }
            queryBuilder.append(") VALUES (");
            for(int i = 0; i < fields.size(); i++) {
                queryBuilder.append("?");
                if(i != fields.size() - 1)
                    queryBuilder.append(", ");
            }
            queryBuilder.append(")");
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(queryBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < fields.size(); i++) {
                Object value = values.get(fields.get(i));
                if(value == null) throw new RuntimeException("Unable to insert null value into database for field \"" + fields.get(i) + "\".");
                if(value instanceof String)
                    preparedStatement.setString(i + 1, (String) value);
                else if(value instanceof Long)
                    preparedStatement.setLong(i + 1, (Long) value);
                else if(value instanceof Double)
                    preparedStatement.setDouble(i + 1, (Double) value);
                else if(value instanceof Integer)
                    preparedStatement.setInt(i + 1, (Integer) value);
                else if(value instanceof Boolean)
                    preparedStatement.setBoolean(i + 1, (Boolean) value);
                else
                    ForgeFrontier.getInstance().getLogger().severe("Unable to create sql query. Value is not a primitive type: " + value);
            }
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            if(result.next()) {
                return new InsertResult(result.getString(1));
            } else {
                return InsertResult.FAILURE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return InsertResult.FAILURE;
        }
    }
}
