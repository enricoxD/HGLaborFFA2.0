package de.hglabor.plugins.ffa;

import de.hglabor.plugins.ffa.gamemechanics.Feast;
import de.hglabor.plugins.ffa.world.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class FFARunnable extends BukkitRunnable {
    private final int resetDuration;
    private int timer;
    private final World world;

    public FFARunnable(World world, int resetDuration) {
        this.timer = resetDuration;
        this.resetDuration = resetDuration;
        this.world = world;
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }
        ArenaManager arenaManager = Main.getArenaManager();
        timer--;
        if (timer == resetDuration / 2) {
            arenaManager.getFeast().spawn();
        }
        if (timer <= 0) {
            timer = resetDuration;
            arenaManager.setFeast(new Feast(world, arenaManager.randomSpawn(50), 20, 300, Material.GRASS_BLOCK));
            arenaManager.reloadMap();
        }
    }
}

