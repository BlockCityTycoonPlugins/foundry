package me.darkmun.blockcitytycoonfoundry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private File file;
    private FileConfiguration config;

    @SuppressWarnings("unused")
    public void setup(File configFolder, String configName) {

        file = new File(configFolder, configName + ".yml");
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @SuppressWarnings("unused")
    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
