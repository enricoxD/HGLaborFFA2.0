package de.hglabor.plugins.ffa.listener;

import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.util.Localization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class FFAQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(event.getPlayer());
        ffaPlayer.getKits().forEach(kit -> kit.disable(ffaPlayer));
        if (ffaPlayer.isInCombat()) {
            event.getPlayer().setHealth(0);
        }
        Localization.broadcastMessage("hglabor.quitMessage", ImmutableMap.of("playerName", ffaPlayer.getName()));
        PlayerList.getInstance().remove(event.getPlayer());
    }
}

