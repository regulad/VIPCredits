package com.gabrielhd.credits.Util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EnchantGlow extends EnchantmentWrapper {
    private static Enchantment glow;

    public EnchantGlow(String name) {
        super(Enchantment.getByName(name).getId());
    }

    public static Enchantment getGlow() {
        if (EnchantGlow.glow != null) {
            return EnchantGlow.glow;
        }
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ignored) {
        }
        Enchantment.registerEnchantment(EnchantGlow.glow = new EnchantGlow("unbreaking"));
        return EnchantGlow.glow;
    }

    public static void addGlow(ItemStack item) {
        Enchantment glows = getGlow();
        item.addEnchantment(glows, 1);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
}
