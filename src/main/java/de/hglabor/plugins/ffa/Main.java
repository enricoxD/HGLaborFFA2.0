package de.hglabor.plugins.ffa;

import de.hglabor.plugins.ffa.config.Config;
import de.hglabor.plugins.ffa.gamemechanics.Feast;
import de.hglabor.plugins.ffa.kit.KitItemListener;
import de.hglabor.plugins.ffa.kit.KitAbilityListener;
import de.hglabor.plugins.ffa.kit.KitSelectorFFA;
import de.hglabor.plugins.ffa.listener.FFADeathListener;
import de.hglabor.plugins.ffa.listener.FFAJoinListener;
import de.hglabor.plugins.ffa.listener.FFAQuitListener;
import de.hglabor.plugins.ffa.util.ScoreboardManager;
import de.hglabor.plugins.ffa.world.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main plugin;
    private static ArenaManager arenaManager;

    @Override
    public void onEnable() {
        plugin = this;
        Config.load();
        World world = Bukkit.getWorld("world");
        arenaManager = new ArenaManager(world, Config.getInteger("ffa.size"));
        FFARunnable ffaRunnable = new FFARunnable(world, Config.getInteger("ffa.duration"));
        ffaRunnable.runTaskTimer(this, 0, 20);
        ScoreboardManager scoreboardManager = new ScoreboardManager();
        scoreboardManager.runTaskTimer(this, 0, 20);
        this.registerListeners();
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(KitSelectorFFA.getInstance(), this);
        pluginManager.registerEvents(new KitAbilityListener(), this);
        pluginManager.registerEvents(new KitItemListener(), this);
        pluginManager.registerEvents(new FFAJoinListener(), this);
        pluginManager.registerEvents(new FFAQuitListener(), this);
        pluginManager.registerEvents(new FFADeathListener(), this);
        pluginManager.registerEvents(new Feast(), this);
    }

    public static ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static Main getPlugin() {
        return plugin;
    }
}
