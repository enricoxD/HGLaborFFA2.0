package de.hglabor.plugins.ffa.config;

import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.kitapi.util.Localization;

import java.util.Locale;

public class Config {
    private Config() {
    }

    public static void load() {
        Main.getPlugin().getConfig().addDefault("ffa.size", 100);
        Main.getPlugin().getConfig().addDefault("ffa.duration", 1800);
        Main.getPlugin().getConfig().addDefault("border.skyborder.damage", 5);
        Main.getPlugin().getConfig().options().copyDefaults(true);
        Main.getPlugin().saveConfig();
    }

    public static int getInteger(String key) {
        return Main.getPlugin().getConfig().getInt(key);
    }

    public static String getString(String key) {
        return Main.getPlugin().getConfig().getString(key);
    }

    public static boolean getBoolean(String key) {
        return Main.getPlugin().getConfig().getBoolean(key);
    }

    public static String getPrefix() {
        return Localization.getMessage("hglabor.prefix", Locale.ENGLISH) + " ";
    }
}
