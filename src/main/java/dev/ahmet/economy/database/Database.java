package dev.ahmet.economy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    public void createTables() {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS economy (uuid VARCHAR(255),coins BIGINT, toggle BOOLEAN)")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred", e);
        }
    }
}
