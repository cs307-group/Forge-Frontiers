package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SelectQueryWrapper {

    public static class SelectField {
        String name;
        String value;
        public SelectField(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    String table;
    List<SelectField> fields;
    List<String> conditions;
    TreeMap<Integer, String> keyIndices;
    Map<String, Object> values;

    public SelectQueryWrapper() {
        this.conditions = new ArrayList<>();
        this.keyIndices = new TreeMap<>();
        this.values = new HashMap<>();
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setFields(String... fields) {
        this.fields = Arrays.stream(fields)
            .map(field -> new SelectField(field, field))
            .collect(Collectors.toList());
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

    public ResultSet executeSyncQuery(Connection databaseConnection) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ");
            for(int i = 0; i < fields.size(); i++) {
                SelectField field = fields.get(i);

                if(field.value.equals(field.name))
                    queryBuilder.append(field.name);
                else
                    queryBuilder.append("(").append(field.value).append(") AS ").append(field.name);

                if(i == fields.size() - 1)
                    queryBuilder.append(", ");
            }
            queryBuilder.append(" FROM ");
            queryBuilder.append(this.table);
            if(this.conditions.size() != 0) {
                queryBuilder.append(" WHERE (");
                for (int i = 0; i < conditions.size(); i++) {
                    queryBuilder.append(conditions.get(i));
                    if (i == conditions.size() - 1)
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
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            ForgeFrontier.getInstance().getLogger().severe(e.getMessage());
            return null;
        }
    }

}
