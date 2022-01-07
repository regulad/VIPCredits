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
    private final ConfigUtils configUtils = new ConfigUtils();
    @Getter
    private final Map<UUID, Long> joinTimeMillis = new HashMap<>();
    @Getter
    private final Map<UUID, Long> lastPaidMillis = new HashMap<>();
    @Getter
    private MySQL mySQL;

    public static String replaceAmpersand(String message) {
        return message.replace("&", "§");
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigCreator.get().setupBungee(this, "Settings");

        String host = this.configUtils.getConfig("Settings").getString("MySQL.Host");
        int port = this.configUtils.getConfig("Settings").getInt("MySQL.Port");
        String database = this.configUtils.getConfig("Settings").getString("MySQL.Database");
        String username = this.configUtils.getConfig("Settings").getString("MySQL.Username");
        String password = this.configUtils.getConfig("Settings").getString("MySQL.Password");
        String tableName = this.configUtils.getConfig("Settings").getString("MySQL.TableName");
        this.mySQL = new MySQL(host, port, database, username, password, tableName);

        this.getProxy().getPluginManager().registerListener(this, new LoginListener());
        this.getProxy().getScheduler().schedule(this, new CountTask(), 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
        if (this.mySQL != null && this.mySQL.running()) {
            this.mySQL.close();
        }
        this.joinTimeMillis.clear();
    }
}
