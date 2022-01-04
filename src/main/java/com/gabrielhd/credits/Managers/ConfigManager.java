package com.gabrielhd.credits.Managers;

import com.gabrielhd.credits.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    @Getter
    private static FileConfiguration settings;
    @Getter
    private static File settingFile;

    public ConfigManager(Main plugin) {
        settingFile = new File(plugin.getDataFolder(), "Settings.yml");
        if (!settingFile.exists()) {
            plugin.saveResource("Settings.yml", false);
        }
        settings = YamlConfiguration.loadConfiguration(settingFile);
    }
}
