package dev.ahmet.economy;

import dev.ahmet.economy.commands.*;
import dev.ahmet.economy.database.Database;
import dev.ahmet.economy.listeners.PlayerJoinListener;
import dev.ahmet.economy.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Economy extends JavaPlugin {

    private static Economy instance;
    private Database database;
    private EconomyManager manager;

    @Override
    public void onEnable() {
        instance = this;
        database = new Database();
        database.createTables();
        manager = new EconomyManager();
        saveDefaultConfig();
        getLogger().info("Economy by 169ahmet");
        register(Bukkit.getPluginManager());
    }

    public static Economy getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public EconomyManager getManager() {
        return manager;
    }

    public String getPrefix() {return this.getConfig().getString("prefix");}

    public void register(PluginManager pm) {
        pm.registerEvents(new PlayerJoinListener(), this);
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("payall").setExecutor(new PayAllCommand());
        getCommand("paytoggle").setExecutor(new PayToggleCommand());
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
    }
}
