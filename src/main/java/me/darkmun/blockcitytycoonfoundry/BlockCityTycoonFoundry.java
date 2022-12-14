package me.darkmun.blockcitytycoonfoundry;

import me.darkmun.blockcitytycoonfoundry.commands.TestCommand;
import me.darkmun.blockcitytycoonfoundry.commands.FoundryFurnaceCommand;
import me.darkmun.blockcitytycoonfoundry.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockCityTycoonFoundry extends JavaPlugin {

    private static BlockCityTycoonFoundry plugin;
    private static Config playersFurnacesData = new Config();
    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;

        if (getConfig().getBoolean("enable")) {
            playersFurnacesData.setup(getDataFolder(), "players-furnaces-data");

            getCommand("testfurnace").setExecutor(new TestCommand());
            getCommand("foundryfurnace").setExecutor(new FoundryFurnaceCommand());

            getServer().getPluginManager().registerEvents(new JoinListener(), this);

            getLogger().info("Plugin enabled.");
        } else {
            getLogger().info("Plugin not enabled.");
        }
    }

    @Override
    public void onDisable() {

        getLogger().info("Plugin disabled.");
    }

    public static BlockCityTycoonFoundry getPlugin() {
        return plugin;
    }

    public static Config getPlayersFurnacesDataConfig() {
        return playersFurnacesData;
    }
}
