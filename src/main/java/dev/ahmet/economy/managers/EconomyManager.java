package dev.ahmet.economy.managers;

import dev.ahmet.economy.database.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EconomyManager {

    public void createPlayer(UUID uuid) {
            try(Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("INSERT INTO economy VALUES (?,?,?)")) {
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, 0);
                stmt.setBoolean(3, true);
                stmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException();
            }
    }

    public boolean isRegistered(UUID uuid) {
        try(Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT * FROM economy WHERE uuid=?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
        return false;
    }

    public Integer getCoins(UUID uuid) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT * FROM economy where uuid=?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("coins");
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
        return null;
    }

    public void setCoins(UUID uuid, int amount) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("UPDATE economy SET coins=? WHERE uuid=?")) {
            stmt.setInt(1, amount);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
    }

    public void addCoins(UUID uuid, int amount) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("UPDATE economy SET coins=? WHERE uuid=?")) {
            stmt.setInt(1, getCoins(uuid) + amount);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
    }

    public void removeCoins(UUID uuid, int amount) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("UPDATE economy SET coins=? WHERE uuid=?")) {
            stmt.setInt(1, getCoins(uuid) - amount);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
    }

    public void resetCoins(UUID uuid) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("UPDATE economy SET coins=? WHERE uuid=?")) {
            stmt.setInt(1, 0);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
    }

    public void updateToggle(UUID uuid, boolean bool) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("UPDATE economy SET toggle=? WHERE uuid=?")) {
            stmt.setBoolean(1, bool);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
    }

    public boolean getToggle(UUID uuid) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT * FROM economy where uuid=?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("toggle");
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
        return false;
    }
}
