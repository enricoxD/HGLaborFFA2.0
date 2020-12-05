package de.hglabor.plugins.ffa.gamemechanics;

import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyBorder extends BukkitRunnable {
    private final int damage;

    public SkyBorder(int damage) {
        this.damage = damage;
    }

    @Override
    public void run() {
        for (FFAPlayer ffaPlayer : PlayerList.getInstance().getPlayersInArena()) {
            Player player = Bukkit.getPlayer(ffaPlayer.getUUID());
            if (player != null) {
                if (player.getLocation().getY() >= player.getWorld().getMaxHeight()) {
                    player.damage(damage);
                }
            }
        }
    }
}
