package com.wolfco.main.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.wolfco.main.Core;

public class DatabaseHandler {
    final MysqlDataSource dataSource;
    final Core core;

    public DatabaseHandler(Core core) throws SQLException {
        this.core = core;
        String host = core.getConfig().getString("database.host");
        int port = core.getConfig().getInt("database.port");
        String name = core.getConfig().getString("database.database");
        String user = core.getConfig().getString("database.user");
        String password = core.getConfig().getString("database.password");

        if (core.getLogger().isLoggable(java.util.logging.Level.INFO)) {
            core.getLogger().info(String.format("%s %s %s %s", password, user, port, host));
        }

        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(host);
        dataSource.setPort(port);
        dataSource.setDatabaseName(name);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                core.log("Connection is valid");
            } else {
                core.log("Connection is not valid");
                throw new SQLException("Connection is not valid");
            }
        }
    }

    public void addSchematic(String name) throws FileNotFoundException {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
        if (plugin == null) {
            return;
        }
        File dataFolder = plugin.getDataFolder();
        File schematicsFolder = new File(dataFolder, "schematics");
        File schematicFile = new File(schematicsFolder, name + ".schematic");
        if (!schematicFile.exists()) {
            return;
        }
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO schematics (name, schematic) VALUES (?, ?)");
            FileInputStream fis = new FileInputStream(schematicFile);

            pstmt.setString(1, name);
            pstmt.setBinaryStream(2, fis, (int) schematicFile.length());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            core.log(e.getMessage());
        }
    }

    public void removeSchematic(String name) {
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM schematics WHERE name = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            core.log(e.getMessage());
        }
    }

    public void getSchematic(String name) throws IOException {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
        if (plugin == null) {
            return;
        }
        File dataFolder = plugin.getDataFolder();
        File schematicsFolder = new File(dataFolder, "schematics");
        File schematicFile = new File(schematicsFolder, name + ".schematic");
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT schematic FROM schematics WHERE name = ?");
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                try (InputStream is = rs.getBinaryStream("schematic")) {
                    try (FileOutputStream fos = new FileOutputStream(schematicFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            core.log(e.getMessage());
        }
    }

    public List<String> listSchematics() {
        List<String> schematics = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM schematics");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                schematics.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            core.log(e.getMessage());
        }
        return schematics;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
