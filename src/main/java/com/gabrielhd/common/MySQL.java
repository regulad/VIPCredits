package com.gabrielhd.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MySQL {
    @Getter
    public static @Nullable MySQL instance;

    private final String username;
    private final String password;
    private final String table;
    private final String host;
    private final String database;
    private final int port;
    private final HikariConfig hikariConfig = new HikariConfig();
    private @Nullable HikariDataSource hikariDataSource;

    public MySQL(String host, int port, String database, String username, String password, String tableName) {
        if (instance != null) {
            throw new RuntimeException("instance declared twice");
        } else {
            instance = this;
        }

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = tableName;

        setConnectionArguments();

        this.setupTable();
    }

    private synchronized void setConnectionArguments() throws RuntimeException {
        this.hikariConfig.setPoolName("Core MySQL");
        this.hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s%s/%s", this.host, port != 3306 ? ":" + port : "", this.database));
        System.out.println("Connecting to " + this.hikariConfig.getJdbcUrl());
        this.hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        this.hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        this.hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        this.hikariConfig.addDataSourceProperty("useUnicode", "true");
        this.hikariConfig.addDataSourceProperty("useSSL", "false");
        this.hikariConfig.setUsername(username);
        this.hikariConfig.setPassword(password);
        this.hikariDataSource = new HikariDataSource(this.hikariConfig);
        System.out.println("Connection arguments loaded, Hikari ConnectionPool ready!");
    }

    public void setupTable() {
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            Statement statement = connection.createStatement();
            Objects.requireNonNull(statement).executeUpdate("CREATE TABLE IF NOT EXISTS " + this.table + " (UUID VARCHAR(100), PlayerName VARCHAR(40))");
            DatabaseMetaData dm = connection.getMetaData();
            ResultSet points = dm.getColumns(null, null, this.table, "Credits");
            if (!points.next()) {
                statement.executeUpdate("ALTER TABLE " + this.table + " ADD COLUMN Credits int AFTER PlayerName;");
            }
            ResultSet bpoints = dm.getColumns(null, null, this.table, "BungeeCredits");
            if (!bpoints.next()) {
                statement.executeUpdate("ALTER TABLE " + this.table + " ADD COLUMN BungeeCredits int AFTER Credits;");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, TopPlayer> getTop() {
        Map<UUID, TopPlayer> top = new HashMap<>();
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM " + this.table + " ORDER BY Credits DESC");
            if (resultSet != null) {
                while (resultSet.next()) {
                    top.put(UUID.fromString(resultSet.getString("UUID")), new TopPlayer(resultSet.getString("PlayerName"), resultSet.getInt("Credits")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top;
    }

    public boolean playerExists(UUID uuid) {
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.table + " WHERE UUID='" + uuid.toString() + "'");
            return (rs != null && rs.next()) && rs.getString("UUID") != null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void createPlayer(UUID uuid, String playerName) {
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            if (!this.playerExists(uuid)) {
                connection.createStatement().executeUpdate("INSERT INTO " + this.table + " (UUID, PlayerName, Credits, BungeeCredits) VALUES ('" + uuid + "', '" + playerName + "', '0', '0');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPoints(UUID uuid, String playerName, int points) {
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            points += this.getPoints(uuid, playerName);

            connection.createStatement().executeUpdate("UPDATE " + this.table + " SET BungeeCredits='" + points + "' WHERE UUID='" + uuid + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPoints(UUID uuid, String playerName, int points) {
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            connection.createStatement().executeUpdate("UPDATE " + this.table + " SET BungeeCredits='" + points + "' WHERE UUID='" + uuid + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPoints(UUID uuid, String playerName) {
        try (final @NotNull Connection connection = this.hikariDataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + this.table + " WHERE UUID='" + uuid + "'");
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt("BungeeCredits");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.createPlayer(uuid, playerName);
        }
        return 0;
    }


    public void close() {
        this.hikariDataSource.close();
        instance = null;
    }

    public boolean running() {
        return this.hikariDataSource != null && this.hikariDataSource.isRunning();
    }
}
