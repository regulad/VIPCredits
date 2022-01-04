package com.gabrielhd.credits;

import com.gabrielhd.credits.Commands.CreditsCmd;
import com.gabrielhd.credits.Hook.PlaceholderAPIHook;
import com.gabrielhd.credits.Listeners.LoginListener;
import com.gabrielhd.credits.Managers.ConfigManager;
import com.gabrielhd.credits.Managers.PlayerManager;
import com.gabrielhd.common.TopPlayer;
import com.gabrielhd.common.MySQL;
import com.gabrielhd.credits.Tasks.SaveDataTask;
import com.gabrielhd.credits.Tasks.TopTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private static PlayerManager playerManager;
    @Getter
    @Setter
    private static Map<UUID, TopPlayer> top;

    public static String Color(String message) {
        return message.replace("&", "§");
    }

    @Override
    public void onEnable() {
        instance = this;

        new ConfigManager(this);
        FileConfiguration data = ConfigManager.getSettings();
        new MySQL(data.getString("MySQL.Host"), data.getInt("MySQL.Port"), data.getString("MySQL.Database"), data.getString("MySQL.Username"), data.getString("MySQL.Password"));

        playerManager = new PlayerManager();

        this.getCommand("creditos").setExecutor(new CreditsCmd());
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        this.getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), new SaveDataTask(), 1200L, 1200L);
        this.getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), new TopTask(), 1200L, 1200L);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook().register();
        }

        top = MySQL.getInstance().getTop();
    }

    @Override
    public void onDisable() {
        MySQL.getInstance().close();
    }
}
