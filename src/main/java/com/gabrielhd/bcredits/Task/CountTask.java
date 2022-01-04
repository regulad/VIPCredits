package com.gabrielhd.bcredits.Task;

import com.gabrielhd.bcredits.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.TimeUnit;

public class CountTask implements Runnable {
    @Override
    public void run() {
        Configuration config = Main.getInstance().getConfigUtils().getConfig("Settings");
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (Main.getInstance().getSchedulers().containsKey(player.getUniqueId())) {
                long calculated = System.currentTimeMillis() - Main.getInstance().getSchedulers().get(player.getUniqueId());
                int time = (int) TimeUnit.MILLISECONDS.toMinutes(calculated);
                if (time >= config.getInt("Counts.Time")) {
                    if (Main.getInstance().getMySQL() != null) {
                        int result = config.getInt("Counts.Reward");
                        new Thread(() -> Main.getInstance().getMySQL().addPoints(player.getUniqueId(), player.getName(), result)).start();

                        player.sendMessage(Main.Color(Main.getInstance().getConfigUtils().getConfig("Settings").getString("CreditsReceived")));
                        long oldTime = Main.getInstance().getSchedulers().get(player.getUniqueId());
                        Main.getInstance().getSchedulers().replace(player.getUniqueId(), oldTime, System.currentTimeMillis());
                    }
                }
            }
        }
    }
}
