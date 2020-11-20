package de.hglabor.plugins.ffa.listener;

import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerData;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.ffa.util.ScoreboardFactory;
import de.hglabor.plugins.kitapi.util.Localization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class FFAJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(player);
        PlayerList.getInstance().add(ffaPlayer);
        Localization.broadcastMessage("hglabor.ffa.joinMessage", ImmutableMap.of("playerName", player.getName()));
        player.sendTitle("FFA Warp", "choose a kit", 20, 20, 20);
        ScoreboardFactory.create(ffaPlayer);
        //TODO:  ((FFAPhase) Main.getPhase()).getArenaWorldLogic().prepareFFAKitSelection(player);
    }
}

