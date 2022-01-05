package com.gabrielhd.credits.Tasks;

import com.gabrielhd.common.MySQL;
import com.gabrielhd.credits.Main;
import com.gabrielhd.credits.Player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SaveDataTask implements Runnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CPlayer cPlayer = Main.getPlayerManager().getCPlayer(player);
            if (cPlayer != null) {
                MySQL.getInstance().setPoints(player.getUniqueId(), player.getName(), cPlayer.getCredits());

                cPlayer.setCredits(MySQL.getInstance().getPoints(player.getUniqueId()));
            }
        }
    }
}
