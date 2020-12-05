package de.hglabor.plugins.ffa.gamemechanics;

import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.util.Localization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Tracker implements Listener {

    @EventHandler
    public void onUseTracker(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Entity target = searchForCompassTarget(player);
        if (event.getMaterial() == Material.COMPASS) {
            if (target == null) {
                player.sendMessage(Localization.getMessage("hglabor.tracker.noTarget", player));
            } else {
                player.setCompassTarget(target.getLocation());
                player.sendMessage(Localization.getMessage("hglabor.tracker.target", ImmutableMap.of("targetName", target.getName()), player));
            }
        }
    }

    private Entity searchForCompassTarget(Player tracker) {
        for (FFAPlayer ffaPlayer : PlayerList.getInstance().getPlayersInArena()) {
            Entity possibleTarget = Bukkit.getEntity(ffaPlayer.getUUID());
            if (possibleTarget == null) continue;
            if (tracker == possibleTarget) continue;
            if (possibleTarget.getLocation().distanceSquared(tracker.getLocation()) > 30.0) {
                return possibleTarget;
            }
        }
        return null;
    }
}



