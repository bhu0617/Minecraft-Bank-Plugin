package bhu.guibank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The Menu class represents the plugin GUI as an interactive inventory
 */
public class Menu {
    private Bank plugin;
    private static Inventory mainMenu;

    /**
     * Constructor
     */
    public Menu(Bank plugin) {
        this.plugin = plugin;
        createInv();
    }

    /**
     * Returns the menu representing the main GUI
     */
    public static Inventory getMainMenu() {
        return mainMenu;
    }

    /**
     * Creates a menu with clickable item components
     */
    public void createInv() {
        mainMenu = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Bank");

        ItemStack item1 = new ItemStack(Material.EMERALD);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName(ChatColor.GREEN + "Personal Account");
        List<String> lore1 = new ArrayList<String>();
        lore1.add(ChatColor.GRAY + "Click to get account info.");
        meta1.setLore(lore1);
        item1.setItemMeta(meta1);
        mainMenu.setItem(4, item1);

        ItemStack item2 = new ItemStack(Material.CHEST);
        ItemMeta meta2 = item1.getItemMeta();
        meta1.setDisplayName(ChatColor.GREEN + "Deposit Coins");
        List<String> lore2 = new ArrayList<String>();
        lore2.add(ChatColor.GRAY + "Store coins in the bank.");
        meta2.setLore(lore2);
        item2.setItemMeta(meta2);
        mainMenu.setItem(11, item2);

        ItemStack item3 = new ItemStack(Material.DISPENSER);
        ItemMeta meta3 = item3.getItemMeta();
        meta3.setDisplayName(ChatColor.GREEN + "Withdraw Coins");
        List<String> lore3 = new ArrayList<String>();
        lore3.add(ChatColor.GRAY + "Take coins out of the bank.");
        meta3.setLore(lore3);
        item3.setItemMeta(meta3);
        mainMenu.setItem(13, item3);

        ItemStack item4 = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta4 = item1.getItemMeta();
        meta4.setDisplayName(ChatColor.GREEN + "Personal Vault");
        List<String> lore4 = new ArrayList<String>();
        lore4.add(ChatColor.GRAY + "Access to store items securely.");
        meta4.setLore(lore4);
        item4.setItemMeta(meta4);
        mainMenu.setItem(15, item4);
    }
}
