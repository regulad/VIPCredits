package com.gabrielhd.credits.Player;

import com.gabrielhd.common.MySQL;
import com.gabrielhd.credits.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CPlayer {

    @Getter
    private final Player player;
    @Getter
    private final UUID uuid;
    @Getter
    @Setter
    private int credits;

    public CPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            this.credits = MySQL.getInstance().getPoints(uuid, getPlayer().getName());
        });
    }
}
