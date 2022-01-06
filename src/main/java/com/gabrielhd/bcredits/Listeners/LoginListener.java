package com.gabrielhd.bcredits.Listeners;

import com.gabrielhd.bcredits.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {
    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Main.getInstance().getJoinTimeMillis().put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Main.getInstance().getJoinTimeMillis().remove(player.getUniqueId());
    }
}
