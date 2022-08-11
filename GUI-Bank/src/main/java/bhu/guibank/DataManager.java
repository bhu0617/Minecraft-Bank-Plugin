package bhu.guibank;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * The DataManager class facilitates use of the data.yml file
 */
public class DataManager {
    private Bank plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    /**
     * Initializes an instance of Bank and the default configuration of data.yml
     */
    public DataManager(Bank plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    /**
     * Assigns data.yml as a Bukkit configuration file if unassigned
     */
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "data.yml");
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource("data.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Returns data.yml as a Bukkit configuration file
     */
    public FileConfiguration getConfig() {
        if (dataConfig == null) {
            reloadConfig();
        }
        return dataConfig;
    }

    /**
     * Saves any changes made to data.yml
     */
    public void saveConfig() {
        if (dataConfig == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch(IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    /**
     * Initializes data.yml with the default configuration in the plugin jar
     */
    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile= new File(plugin.getDataFolder(), "data.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
    }
}
