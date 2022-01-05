package com.gabrielhd.credits.Managers;

import com.gabrielhd.common.MySQL;
import com.gabrielhd.credits.Main;
import com.gabrielhd.credits.Player.CPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    @Getter
    private final Map<UUID, CPlayer> players = new HashMap<>();

    public CPlayer getCPlayer(Player player) {
        return this.players.get(player.getUniqueId());
    }

    public void createCPlayer(Player player) {
        CPlayer cPlayer = new CPlayer(player);
        if (!this.players.containsKey(player.getUniqueId())) {
            this.players.put(player.getUniqueId(), cPlayer);
        }
    }

    public void removeCPlayer(Player player) {
        if (this.players.containsKey(player.getUniqueId())) {
            CPlayer cPlayer = this.players.get(player.getUniqueId());

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                    MySQL.getInstance().setPoints(player.getUniqueId(), cPlayer.getCredits()));

            this.players.remove(player.getUniqueId());
        }
    }
}
