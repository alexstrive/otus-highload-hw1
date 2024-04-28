package com.alexstrive.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractRepository<T> implements Repository<T> {
    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "pass";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
    }
}
