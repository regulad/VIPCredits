package com.gabrielhd.bcredits.Task;

import com.gabrielhd.bcredits.Main;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public class CountTask implements Runnable {
    @Getter
    private final @NotNull ProxiedPlayer proxiedPlayer;
    private final @NotNull Main main;

    public CountTask(final @NotNull Main main, final @NotNull ProxiedPlayer proxiedPlayer) {
        this.proxiedPlayer = proxiedPlayer;
        this.main = main;
    }

    @Override
    public void run() {
        final int existingPoints = main.getMySQL().getPoints(proxiedPlayer.getUniqueId());

        main.getMySQL().setPoints(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), existingPoints + main.getConfigUtils().getConfig("Settings").getInt("Counts.Reward"));

        proxiedPlayer.sendMessage(Main.replaceAmpersand(main.getConfigUtils().getConfig("Settings").getString("CreditsReceived")));
    }
}