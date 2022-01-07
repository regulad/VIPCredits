package com.gabrielhd.bcredits.Task;

import com.gabrielhd.bcredits.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class CountTask implements Runnable {
    @Override
    public void run() {
        final Main main = Main.getInstance();
        Configuration config = main.getConfigUtils().getConfig("Settings");
        final int countTime = config.getInt("Counts.Time", 999999);
        final int countReward = config.getInt("Counts.Reward", 0);
        for (final @NotNull ProxiedPlayer player : main.getProxy().getPlayers()) {
            if (main.getJoinTimeMillis().containsKey(player.getUniqueId())) {
                final long joinMillis = main.getJoinTimeMillis().get(player.getUniqueId());

                final @Nullable Long maybeLastPaidMillis = main.getLastPaidMillis().remove(player.getUniqueId());
                final long lastPaidMillis = maybeLastPaidMillis != null ? maybeLastPaidMillis : joinMillis;

                final long sinceLastPaidMillis = System.currentTimeMillis() - lastPaidMillis;

                final long minsSinceLastPaid = TimeUnit.MILLISECONDS.toMinutes(sinceLastPaidMillis);

                if (minsSinceLastPaid >= countTime) {
                    main.getProxy().getScheduler().runAsync(main, () -> {
                        final int existingPoints = main.getMySQL().getPoints(player.getUniqueId());

                        main.getMySQL().setPoints(player.getUniqueId(), player.getName(), existingPoints + countReward);

                        player.sendMessage(Main.replaceAmpersand(config.getString("CreditsReceived")));

                        main.getLastPaidMillis().put(player.getUniqueId(), System.currentTimeMillis());
                    });
                }
            }
        }
    }
}
