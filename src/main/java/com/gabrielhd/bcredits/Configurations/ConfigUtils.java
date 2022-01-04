package com.gabrielhd.bcredits.Configurations;

import com.gabrielhd.bcredits.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigUtils {
    public void saveConfig(String configName) throws IOException {
        File pluginDir = Main.getInstance().getDataFolder();
        File configFile = new File(pluginDir, configName + ".yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public Configuration getConfig(String configName) {
        File pluginDir = Main.getInstance().getDataFolder();
        File configFile = new File(pluginDir, configName + ".yml");
        if (!configFile.exists()) return null;

        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Configuration getConfigurationSection(String configName, String section) {
        return this.getConfig(configName).getSection(section);
    }

    public File getFile(String configName) {
        File pluginDir = Main.getInstance().getDataFolder();
        return new File(pluginDir, configName + ".yml");
    }
}
