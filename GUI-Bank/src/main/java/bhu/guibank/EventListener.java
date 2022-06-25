package bhu.guibank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;

/**
 * The EventListener class handles in-game events as they occur
 */
public class EventListener implements Listener {
    private Bank plugin;
    private HashSet<String> depositingList;
    private HashSet<String> withdrawingList;

    /**
     * Constructor
     */
    public EventListener(Bank plugin) {
        this.plugin = plugin;
        depositingList = new HashSet<String>();
        withdrawingList = new HashSet<String>();
    }

    /**
     * Defines interaction logic for each GUI menu component
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(Menu.getMainMenu())) { return; }
        if (event.getCurrentItem() == null) { return; }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        String id = player.getUniqueId().toString();
        if (event.getCurrentItem().getType().equals(Material.EMERALD)) {
            player.closeInventory();
            double bal = (double)plugin.getDataFile().getConfig().get("players." + id + ".balance");
            player.sendMessage(ChatColor.GREEN + "Current balance: $" + bal);
        }
        if (event.getCurrentItem().getType().equals(Material.CHEST)) {
            player.closeInventory();
            player.sendMessage("Enter deposit amount: ");
            depositingList.add(id);
        }
        if (event.getCurrentItem().getType().equals(Material.DISPENSER)) {
            player.closeInventory();
            player.sendMessage("Enter withdrawal amount: ");
            withdrawingList.add(id);
        }
        if (event.getCurrentItem().getType().equals(Material.ENDER_CHEST)) {
            Inventory vaultInv = Bukkit.createInventory(player, 54, ChatColor.DARK_GRAY + "Personal Vault");
            if (plugin.getStorageItems().containsKey(id)) {
                vaultInv.setContents(plugin.getStorageItems().get(id));
            }
            player.openInventory(vaultInv);
        }
    }

    /**
     * Saves a player's personal vault contents into the item storage Hashmap upon closure of the inventory
     */
    @EventHandler
    public void onGUIClose(InventoryCloseEvent event) {
        String id = event.getPlayer().getUniqueId().toString();
        if (event.getView().getTitle().contains("Personal Vault")) {
            plugin.getStorageItems().put(id, event.getInventory().getContents());
        }
    }

    /**
     * Reads and processes player input of deposit or withdrawal amounts through the in-game chat channel
     */
    @EventHandler
    public void chatCheck(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String id = event.getPlayer().getUniqueId().toString();

        if (depositingList.contains(id)) {
            double amt;
            try {
                amt = Double.parseDouble(message);
            } catch(NumberFormatException e) {
                // Not a double
                return;
            }
            double bal = (double)plugin.getDataFile().getConfig().get("players." + id + ".balance");
            plugin.getDataFile().getConfig().set("players." + id + ".balance", bal + amt);
            plugin.getDataFile().saveConfig();
            depositingList.remove(id);
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Your new balance is: $" + (bal + amt));
        }
        if (withdrawingList.contains(id)) {
            double amt;
            try {
                amt = Double.parseDouble(message);
            } catch(NumberFormatException e) {
                // Not a double
                return;
            }
            double bal = (double)plugin.getDataFile().getConfig().get("players." + id + ".balance");
            plugin.getDataFile().getConfig().set("players." + id + ".balance", bal - amt);
            plugin.getDataFile().saveConfig();
            withdrawingList.remove(id);
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Your new balance is: $" + (bal - amt));
        }
    }

    /**
     * Activates an interest accrual schedule for players when they join the server
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String id = event.getPlayer().getUniqueId().toString();
        if ((double)plugin.getDataFile().getConfig().get("players." + id + ".balance") > 0) {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    // Round to two decimal places
                    double bal = (double)plugin.getDataFile().getConfig().get("players." + id + ".balance");
                    double rate = plugin.getConfig().getDouble("interest-rate") / 100;
                    bal *= 100;
                    bal = Math.round(bal);
                    bal /= 100;
                    rate *= 100;
                    rate = Math.round(rate);
                    rate /= 100;
                    double newBal = bal+bal*rate;
                    newBal *= 100;
                    newBal = Math.round(newBal);
                    newBal /= 100;
                    double interestAmt = (newBal-bal);
                    interestAmt *= 100;
                    interestAmt = Math.round(interestAmt);
                    interestAmt /= 100;
                    plugin.getDataFile().getConfig().set("players." + id + ".balance", newBal);
                    plugin.getDataFile().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "You have received $" + interestAmt + " in interest");
                }
            }, 0L, plugin.getConfig().getLong("interest-period"));
        }
    }
}
