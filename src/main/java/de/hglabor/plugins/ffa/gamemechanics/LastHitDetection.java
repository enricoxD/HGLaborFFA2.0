package de.hglabor.plugins.ffa.gamemechanics;

import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LastHitDetection implements Listener {

    @EventHandler
    public void onPlayerHitOtherPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            KitPlayer attacker = PlayerList.getInstance().getKitPlayer((Player) event.getDamager());
            KitPlayer enemy = PlayerList.getInstance().getKitPlayer((Player) event.getEntity());
            attacker.setLastHittedTimeStamp(System.currentTimeMillis());
            attacker.setLastHittedPlayer(enemy);
        }
    }
}
