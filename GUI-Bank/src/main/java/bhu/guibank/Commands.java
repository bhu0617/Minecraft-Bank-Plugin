package bhu.guibank;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Commands class handles player chat inputs matching "/" followed by a defined label
 */
public class Commands implements CommandExecutor {
    private Bank plugin;

    /**
     * Constructor
     */
    public Commands(Bank plugin) {
        this.plugin = plugin;
    }

    /**
     * Initializes player balances and opens the GUI menu
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("bank")) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player player = (Player) sender;
            String id = player.getUniqueId().toString();
            if (!plugin.getDataFile().getConfig().contains("players." + id + ".balance")) {
                plugin.getDataFile().getConfig().set("players." + id + ".balance", (double)0);
            }
            player.openInventory(Menu.getMainMenu());
            return true;
        }
        return false;
    }
}
