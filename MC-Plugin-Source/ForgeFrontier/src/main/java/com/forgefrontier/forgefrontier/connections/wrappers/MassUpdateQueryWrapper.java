package com.forgefrontier.forgefrontier.connections.wrappers;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.io.Console;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class MassUpdateQueryWrapper {

    private String conditionStr;
    String table;
    List<String> fields;
    Map<String, Object> values;

    public MassUpdateQueryWrapper() {
        this.fields = new ArrayList<>();
        this.values = new HashMap<>();
        conditionStr = "false";         // WHERE FALSE == do nothing by default
    }

    /**
     *
     * @param conditionStr WHERE ( conditionStr ) <- do not need parenthesis in your condition string!
     */
    public void setConditionString(String conditionStr) {
        this.conditionStr = conditionStr;
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
            callback.accept(this.executeSyncQuery(databaseConnection));
        }).start();
    }

    public boolean executeSyncQuery(Connection databaseConnection) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ");
            queryBuilder.append(this.table);
            queryBuilder.append(" SET ");
            for(int i = 0; i < fields.size(); i++) {
                queryBuilder.append(fields.get(i)).append(" = ").append("?");
                if(i != fields.size() - 1)
                    queryBuilder.append(", ");
            }
            queryBuilder.append(" WHERE ( ").append(conditionStr).append(" )");

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
                else if (value instanceof Timestamp)
                    preparedStatement.setTimestamp(i + 1, (Timestamp) value);
                else
                    ForgeFrontier.getInstance().getLogger().severe("Unable to create sql query. Value is not a primitive type: " + value);
            }
            ForgeFrontier.getInstance().getLogger().log(Level.INFO, "Executing Mass Update:\n\n" + preparedStatement.toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
