package com.gabrielhd.credits.Managers;

import com.gabrielhd.credits.Player.CachedCreditPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private final Map<UUID, CachedCreditPlayer> players = new ConcurrentHashMap<>(200);

    public CachedCreditPlayer getCPlayer(Player player) {
        return this.players.get(player.getUniqueId());
    }

    public void createCPlayer(Player player) {
        CachedCreditPlayer cachedCreditPlayer = new CachedCreditPlayer(player);
        if (!this.players.containsKey(player.getUniqueId())) {
            this.players.put(player.getUniqueId(), cachedCreditPlayer);
        }
    }

    public void removeCPlayer(Player player) {
        if (this.players.containsKey(player.getUniqueId())) {
            CachedCreditPlayer cachedCreditPlayer = this.players.get(player.getUniqueId());

            cachedCreditPlayer.commit();

            this.players.remove(player.getUniqueId());
        }
    }
}
