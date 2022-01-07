package com.gabrielhd.credits.Player;

import com.gabrielhd.common.MySQL;
import com.gabrielhd.credits.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CachedCreditPlayer {
    @Getter
    private final Player player;
    @Getter
    private final UUID uuid;
    private int credits;

    public CachedCreditPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();

        this.update();
    }

    public void setCredits(int credits) {
        this.credits = credits;
        commit();
    }

    public int getCredits() {
        update();
        return this.credits;
    }

    public void asyncUpdate() {
        this.credits = MySQL.getInstance().getPoints(uuid);
    }

    public void update() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), this::asyncUpdate);
    }

    public void asyncCommit() {
        MySQL.getInstance().setPoints(player.getUniqueId(), player.getName(), this.credits);
    }

    public void commit() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), this::asyncCommit);
    }
}
