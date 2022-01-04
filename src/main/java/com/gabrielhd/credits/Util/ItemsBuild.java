package com.gabrielhd.credits.Util;

import com.gabrielhd.credits.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemsBuild {

    public static ItemStack crearItem(Material material, int amount, int data, String name, List<String> lore, boolean glow) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.Color(name));
        if (lore != null) {
            ArrayList<String> color = new ArrayList<>();
            for (String b : lore) {
                color.add(Main.Color(b));
            }
            meta.setLore(color);
        }
        item.setItemMeta(meta);

        if (glow) EnchantGlow.addGlow(item);
        return item;
    }
}