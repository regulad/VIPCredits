package com.gabrielhd.bcredits;

import com.gabrielhd.bcredits.Configurations.ConfigCreator;
import com.gabrielhd.bcredits.Configurations.ConfigUtils;
import com.gabrielhd.bcredits.Listeners.LoginListener;
import com.gabrielhd.bcredits.Task.CountTask;
import com.gabrielhd.common.MySQL;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {

    @Getter
    private static Main instance;
    @Getter
    private final ConfigUtils configUtils;
    @Getter
    private final Map<UUID, Long> schedulers = new HashMap<>();
    @Getter
    private MySQL mySQL;

    public Main() {
        this.configUtils = new ConfigUtils();
    }

    public static String Color(String message) {
        return message.replace("&", "§");
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigCreator.get().setupBungee(this, "Settings");

        if (this.configUtils.getConfig("Settings").getBoolean("MySQL.Enable")) {
            String host = this.configUtils.getConfig("Settings").getString("MySQL.Host");
            int port = this.configUtils.getConfig("Settings").getInt("MySQL.Port");
            String database = this.configUtils.getConfig("Settings").getString("MySQL.Database");
            String username = this.configUtils.getConfig("Settings").getString("MySQL.Username");
            String password = this.configUtils.getConfig("Settings").getString("MySQL.Password");
            String tableName = this.configUtils.getConfig("Settings").getString("MySQL.TableName");
            this.mySQL = new MySQL(host, port, database, username, password, tableName);
        }

        this.getProxy().getPluginManager().registerListener(this, new LoginListener());
        this.getProxy().getScheduler().schedule(this, new CountTask(), 10L, 10L, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        if (this.mySQL != null && this.mySQL.running()) {
            this.mySQL.close();
        }
    }
}
