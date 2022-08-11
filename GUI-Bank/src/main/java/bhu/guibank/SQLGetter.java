package bhu.guibank;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {
    private Bank plugin;

    /**
     * Initializes an instance of Bank
     */
    public SQLGetter(Bank plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a table in the connected SQL database
     */
    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.getSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerdata "
                    + "(NAME VARCHAR(100),UUID VARCHAR(100),BALANCE FLOAT(53),PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a row entry for a player in the table of the connected SQL database
     */
    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if (!exists(uuid)) {
                PreparedStatement ps2 = plugin.getSQL().getConnection()
                        .prepareStatement("INSERT IGNORE INTO playerdata" + " (Name,UUID) VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();
                return;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the UUID associated with a player is present in the table of the connected SQL database
     */
    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = plugin.getSQL().getConnection()
                    .prepareStatement("SELECT + FROM playerdata WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the float value representing a player's bank balance in the table of the connected SQL database
     */
    public void setBalance(UUID uuid, float balance) {
        try {
            PreparedStatement ps = plugin.getSQL().getConnection()
                    .prepareStatement("UPDATE playerdata SET BALANCE=? WHERE UUID=?");
            ps.setFloat(1, balance);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}

