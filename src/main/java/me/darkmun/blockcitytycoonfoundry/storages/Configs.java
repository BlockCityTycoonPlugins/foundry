package me.darkmun.blockcitytycoonfoundry.storages;

import me.darkmun.blockcitytycoonfoundry.BlockCityTycoonFoundry;
import me.darkmun.blockcitytycoonfoundry.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class Configs {
    public static final FileConfiguration mainConfig = BlockCityTycoonFoundry.getPlugin().getConfig();
    public static final Config playersFurnacesDataConfig = BlockCityTycoonFoundry.getPlayersFurnacesDataConfig();
}
