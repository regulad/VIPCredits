package com.gabrielhd.bcredits.Configurations;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;

public class ConfigCreator {
    private static ConfigCreator instance;
    private File pluginDir;
    private File configFile;

    public static ConfigCreator get() {
        if (ConfigCreator.instance == null) {
            ConfigCreator.instance = new ConfigCreator();
        }
        return ConfigCreator.instance;
    }

    public void setup(Plugin p, String configName) {
        this.pluginDir = p.getDataFolder();
        this.configFile = new File(this.pluginDir, configName + ".yml");
        if (!this.configFile.exists()) {
            this.configFile.mkdir();
        }
    }

    public void setupBungee(Plugin p, String configName) {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        File configFile = new File(p.getDataFolder(), configName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                Throwable t = null;
                try {
                    try (InputStream is = p.getResourceAsStream(configName + ".yml")) {
                        try (OutputStream os = new FileOutputStream(configFile)) {
                            ByteStreams.copy(is, os);
                        }
                        if (is != null) {
                            is.close();
                        }
                    } finally {
                        {
                            Throwable t2;
                        }
                    }
                } finally {
                    {
                        Throwable t3;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error on create " + configName + " please contact with Developer: GabrielHD55", e);
            }
        }
    }
}
