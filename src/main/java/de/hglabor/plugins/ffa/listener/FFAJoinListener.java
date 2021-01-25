package de.hglabor.plugins.ffa.listener;

import com.google.common.collect.ImmutableMap;
import de.hglabor.Localization.Localization;
import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.ffa.util.ScoreboardFactory;
import de.hglabor.plugins.kitapi.util.Utils;
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
        Utils.broadcastMessage("hglabor.ffa.joinMessage", ImmutableMap.of("playerName", player.getName()));
        player.sendTitle(
                Localization.INSTANCE.getMessage("hglabor.ffa.joinTitle", Utils.getPlayerLocale(player)),
                Localization.INSTANCE.getMessage("hglabor.ffa.lowerJoinTitle", Utils.getPlayerLocale(player)),
                20, 20, 20);
        ScoreboardFactory.create(ffaPlayer);
        Main.getArenaManager().prepareKitSelection(player);
    }
}

