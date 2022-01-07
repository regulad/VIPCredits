package com.gabrielhd.credits.Hook;

import com.gabrielhd.credits.Main;
import com.gabrielhd.credits.Player.CachedCreditPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        CachedCreditPlayer playerData = Main.getPlayerManager().getCPlayer(player);
        if (playerData == null) {
            return "";
        }
        if (s.equals("credits")) {
            return String.valueOf(playerData.getCredits());
        }
        if (s.equals("creditos")) {
            return String.valueOf(playerData.getCredits());
        }
        return "";
    }

    @Override
    public String getIdentifier() {
        return "vipcredits";
    }

    @Override
    public String getAuthor() {
        return "GabrielHD55";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
