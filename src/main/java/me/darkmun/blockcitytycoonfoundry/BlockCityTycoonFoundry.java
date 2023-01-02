package me.darkmun.blockcitytycoonfoundry;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.darkmun.blockcitytycoonfoundry.commands.TestCommand;
import me.darkmun.blockcitytycoonfoundry.commands.FoundryFurnaceCommand;
import me.darkmun.blockcitytycoonfoundry.listeners.BlockChangeListener;
import me.darkmun.blockcitytycoonfoundry.listeners.FoundryFurnaceInteractionListener;
import me.darkmun.blockcitytycoonfoundry.listeners.JoinAndQuitListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockCityTycoonFoundry extends JavaPlugin {

    private static BlockCityTycoonFoundry plugin;
    private static final Config playersFurnacesData = new Config();
    public static Economy econ = null;
    public static Permission permission = null;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;

        if (getConfig().getBoolean("enable")) {
            playersFurnacesData.setup(getDataFolder(), "players-furnaces-data");

            hookToVault();
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();

            getCommand("testfurnace").setExecutor(new TestCommand());
            getCommand("foundryfurnace").setExecutor(new FoundryFurnaceCommand());

            getServer().getPluginManager().registerEvents(new JoinAndQuitListener(), this);
            getServer().getPluginManager().registerEvents(new FoundryFurnaceInteractionListener(), this);
            manager.addPacketListener(new BlockChangeListener(this, PacketType.Play.Server.BLOCK_CHANGE));

            getLogger().info("Plugin enabled.");
        } else {
            getLogger().info("Plugin not enabled.");
        }
    }

    private void hookToVault() {
        if (!setupEconomy() || !setupPermissions()) {
            getPlugin().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
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
