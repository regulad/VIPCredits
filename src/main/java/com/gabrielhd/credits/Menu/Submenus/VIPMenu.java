package com.gabrielhd.credits.Menu.Submenus;

import com.gabrielhd.credits.Main;
import com.gabrielhd.credits.Managers.ConfigManager;
import com.gabrielhd.credits.Menu.Menu;
import com.gabrielhd.credits.Player.CPlayer;
import com.gabrielhd.credits.Util.ItemsBuild;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VIPMenu extends Menu {
    public VIPMenu(Player player) {
        super(ConfigManager.getSettings().getString("Menu.Title"), ConfigManager.getSettings().getInt("Menu.Rows"));
        this.update(player);
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {
    }

    @Override
    public void onOpen(Player player, InventoryOpenEvent event) {
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        FileConfiguration config = ConfigManager.getSettings();
        CPlayer playerData = Main.getPlayerManager().getCPlayer(player);
        if (playerData != null) {
            for (int i = 0; i < 54; i++) {
                if (config.isSet("Menu.Contents." + i) && config.isSet("Menu.Contents." + i + ".Price")) {
                    if (item.getType() == Material.getMaterial(config.getString("Menu.Contents." + i + ".ID")) && event.getSlot() == config.getInt("Menu.Contents." + i + ".Slot")) {
                        if (config.isSet("Menu.Contents." + i + ".Commands") && playerData.getCredits() >= config.getInt("Menu.Contents." + i + ".Price")) {
                            playerData.setCredits(playerData.getCredits() - config.getInt("Menu.Contents." + i + ".Price"));
                            for (String s : config.getStringList("Menu.Contents." + i + ".Commands")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", player.getName()));
                            }
                            player.sendMessage(Main.Color(config.getString("Messages.PurchaseStore")));
                            return;
                        }
                        player.sendMessage(Main.Color(config.getString("Messages.InsufficientPoints")));
                        return;
                    }
                }
            }
        }
    }

    public void update(Player player) {
        FileConfiguration config = ConfigManager.getSettings();
        for (int i = 0; i < config.getConfigurationSection("Menu").getKeys(true).size(); i++) {
            if (config.isSet("Menu.Contents." + i)) {
                List<String> lore = Lists.newArrayList();
                for (String s : config.getStringList("Menu.Contents." + i + ".Description")) {
                    s = s.replace("%player%", player.getName());
                    lore.add(s);
                }
                Material material = Material.getMaterial(config.getString("Menu.Contents." + i + ".ID"));
                if (material != null) {
                    this.setItem(config.getInt("Menu.Contents." + i + ".Slot"), ItemsBuild.crearItem(material, 1, config.getInt("Menu.Contents." + i + ".Data"), config.getString("Menu.Contents." + i + ".Name"), lore, config.getBoolean("Menu.Contents." + i + ".Glow")));
                } else {
                    System.out.println("Material null in " + i + " section");
                }
            }
        }
    }
}
