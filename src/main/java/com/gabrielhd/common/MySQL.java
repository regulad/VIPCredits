package com.gabrielhd.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.HashMap;
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
        try (final @NotNull Connection connection = Objects.requireNonNull(this.hikariDataSource).getConnection()) {
            Statement statement = connection.createStatement();
            Objects.requireNonNull(statement).executeUpdate(String.format("CREATE TABLE IF NOT EXISTS %s (UUID CHAR(36) UNIQUE, Credits INT DEFAULT 0, PRIMARY KEY (UUID));", this.table));
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<UUID, Integer> getTop() {
        HashMap<UUID, Integer> top = new HashMap<>();
        try (final @NotNull Connection connection = Objects.requireNonNull(this.hikariDataSource).getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(String.format("SELECT * FROM %s ORDER BY Credits DESC;", this.table));
            if (resultSet != null) {
                while (resultSet.next()) {
                    top.put(UUID.fromString(resultSet.getString("UUID")), resultSet.getInt("Credits"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top;
    }

    public boolean playerExists(UUID uuid) {
        try (final @NotNull Connection connection = Objects.requireNonNull(this.hikariDataSource).getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(String.format("SELECT * FROM %s WHERE UUID = ?;", this.table));
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            return (rs != null && rs.next()) && rs.getString("UUID") != null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void addPoints(UUID uuid, int points) {
        this.setPoints(uuid, this.getPoints(uuid) + points);
    }

    public void setPoints(UUID uuid, int points) {
        try (final @NotNull Connection connection = Objects.requireNonNull(this.hikariDataSource).getConnection()) {
            points += this.getPoints(uuid);

            PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %s (UUID, Credits) VALUES(?, ?) ON DUPLICATE KEY UPDATE Credits=?;", this.table));
            statement.setString(1, uuid.toString());
            statement.setInt(2, points);
            statement.setInt(3, points);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPoints(UUID uuid) {
        try (final @NotNull Connection connection = Objects.requireNonNull(this.hikariDataSource).getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(String.format("SELECT * FROM %s WHERE UUID = ?;", this.table));
            stmt.setString(1, uuid.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt("Credits");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            if (!e.getMessage().equals("Illegal operation on empty result set.") && !e.getMessage().equals("ResultSet closed")) {
                e.printStackTrace();
            }
            return 0;
        }
    }


    public void close() {
        if (this.hikariDataSource != null) {
            this.hikariDataSource.close();
        }
        instance = null;
    }

    public boolean running() {
        return this.hikariDataSource != null && this.hikariDataSource.isRunning();
    }
}
