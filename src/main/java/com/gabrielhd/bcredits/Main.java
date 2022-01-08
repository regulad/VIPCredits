package com.gabrielhd.bcredits;

import com.gabrielhd.bcredits.Configurations.ConfigCreator;
import com.gabrielhd.bcredits.Configurations.ConfigUtils;
import com.gabrielhd.bcredits.Task.CountTask;
import com.gabrielhd.common.MySQL;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin implements Listener {
    @Getter
    private static Main instance;
    @Getter
    private final ConfigUtils configUtils = new ConfigUtils();
    @Getter
    private final Map<ProxiedPlayer, ScheduledTask> playerBonusTasks = new ConcurrentHashMap<>(200);
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

        this.getProxy().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        if (this.mySQL != null && this.mySQL.running()) {
            this.mySQL.close();
        }

        for (final @NotNull ScheduledTask task : playerBonusTasks.values()) {
            task.cancel();
        }

        playerBonusTasks.clear();
    }

    @EventHandler
    public void onPlayerJoin(final @NotNull PostLoginEvent postLoginEvent) {
        final @NotNull ProxiedPlayer player = postLoginEvent.getPlayer();
        final @NotNull CountTask countTask = new CountTask(this, player);
        final int minutes = this.getConfigUtils().getConfig("Settings").getInt("Counts.Time");
        playerBonusTasks.put(player, this.getProxy().getScheduler().schedule(this, countTask, minutes, minutes, TimeUnit.MINUTES));
    }

    @EventHandler
    public void onPlayerLeave(final @NotNull PlayerDisconnectEvent playerDisconnectEvent) {
        final @Nullable ScheduledTask task = playerBonusTasks.remove(playerDisconnectEvent.getPlayer());
        if (task != null) {
            task.cancel();
        }
    }
}
