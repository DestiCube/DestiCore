package com.desticube.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.plugin.Plugin;

public class Database {

    private Connection connection;

    public Database(Plugin main, String name) throws Exception {
    	File folder = new File(main.getDataFolder() + File.separator + "databases");
        folder.mkdirs();
        Class.forName("org.sqlite.JDBC").newInstance();
        connection = DriverManager.getConnection("jdbc:sqlite:" + new File(folder, name + ".db"));
    }

    public Database(Plugin main, boolean reconnect, String host, String database, String username, String password, int port) throws Exception {
        Properties info = new Properties();
        info.setProperty("useSSL", "true");

        if (reconnect) {
            info.setProperty("autoReconnect", "true");
        }
        info.setProperty("trustServerCertificate", "true");
        info.setProperty("user", username);
        info.setProperty("password", password);

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, info);
    }

    public void createTable(String sqlURL) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlURL)) {
            statement.executeUpdate();
        }
    }
    
    public Connection connection() {return connection;}
    
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
