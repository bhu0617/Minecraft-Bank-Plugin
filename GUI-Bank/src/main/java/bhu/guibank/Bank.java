package bhu.guibank;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This program is a plugin which uses Bukkit API to provide GUI-based banking functionality for Minecraft servers.
 * Players can make deposits or withdrawals to a bank account, store items in a personal vault,
 * and earn interest on their account balance.
 */
public final class Bank extends JavaPlugin {
    private static FileConfiguration config;
    private static DataManager data;
    private static Menu menu;
    private static Economy eco;
    private static Map<String, ItemStack[]> storageItems = new HashMap<String, ItemStack[]>();

    /**
     * Initializes classes and configurations on plugin startup
     */
    @Override
    public void onEnable() {
        config = getConfig();
        saveDefaultConfig();
        data = new DataManager(this);
        menu = new Menu(this);
        getCommand("bank").setExecutor(new Commands(this));
        PluginManager pm = getServer().getPluginManager();
        EventListener listener = new EventListener(this);
        pm.registerEvents(listener, this);

        if (!setupEconomy()) {
            System.out.println(ChatColor.RED + "You must have Vault and an economy plugin installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (data.getConfig().contains("personal-vault")) {
            restoreInvs();
        }
    }

    /**
     * Saves data on plugin shutdown
     */
    @Override
    public void onDisable() {
        if (!storageItems.isEmpty()) {
            saveInvs();
        }
    }

    /**
     * Saves personal vault item data for all players to data.yml
     */
    public void saveInvs() {
        for (Map.Entry<String, ItemStack[]> entry : storageItems.entrySet()) {
            data.getConfig().set("personal-vault." + entry.getKey(), entry.getValue());
        }
        data.saveConfig();
    }

    /**
     * Retrieves personal vault item data for all players from data.yml
     */
    public void restoreInvs() {
        data.getConfig().getConfigurationSection("personal-vault").getKeys(false).forEach(key ->{
            @SuppressWarnings("unchecked")
            ItemStack[] content = ((List<ItemStack>) data.getConfig().get("personal-vault." + key)).toArray(new ItemStack[0]);
            storageItems.put(key, content);
        });
    }

    /**
     * Checks for required plugin dependencies
     */
    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economy != null) {
            eco = economy.getProvider();
        }
        return (eco != null);
    }

    /**
     * Returns the config.yml file configuration
     */
    public static FileConfiguration getConfigFile() {
        return config;
    }

    /**
     * Returns a DataManager object containing the data.yml file configuration
     */
    public static DataManager getDataFile() {
        return data;
    }

    /**
     * Returns a HashMap containing player ids and item storage data
     */
    public static Map<String, ItemStack[]> getStorageItems() { return storageItems; }
}
