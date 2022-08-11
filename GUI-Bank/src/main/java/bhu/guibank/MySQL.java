package bhu.guibank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private Bank plugin;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private Connection connection;

    /**
     * Initializes an instance of Bank and retrieves SQL database credentials from config.yml
     */
    public MySQL(Bank plugin) {
        this.plugin = plugin;
        host = plugin.getConfig().getString("host");
        port = plugin.getConfig().getString("port");
        database = plugin.getConfig().getString("database");
        username = plugin.getConfig().getString("username");
        password = plugin.getConfig().getString("password");
    }

    /**
     * Checks if the plugin is connected to a SQL database
     */
    public boolean isConnected() {
        return (connection == null ? false : true);
    }

    /**
     * Connects to the specified SQL database
     */
    public void connect() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" +
                host + ":" + port + "/" + database + "?useSSL=false", username, password);
    }

    /**
     * Disconnects from the connected SQL database
     */
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns an instance of Connection
     */
    public Connection getConnection() {
        return connection;
    }
}
