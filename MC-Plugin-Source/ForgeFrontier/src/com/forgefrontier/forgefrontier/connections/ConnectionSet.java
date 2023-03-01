package com.forgefrontier.forgefrontier.connections;

import com.forgefrontier.forgefrontier.ForgeFrontier;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;

class ConnectionSet {
    private ResultSet result;
    private Statement statement;

    public ConnectionSet(Statement s, ResultSet r) {
        this.statement = s;
        this.result = r;
    }

    public ResultSet getResult() {
        return result;
    }

    public Statement getStatement() {
        return statement;
    }

    public void close() {
        try {
            this.result.close();
            this.statement.close();
        } catch (Exception e) {
            ForgeFrontier.getInstance().getLogger().log
                    (Level.WARNING, "Failed to close query.\n" + e.getMessage());
        }
    }
}