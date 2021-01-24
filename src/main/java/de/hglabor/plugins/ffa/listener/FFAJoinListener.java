package de.hglabor.plugins.ffa.listener;

import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerData;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.ffa.util.ScoreboardFactory;
import de.hglabor.plugins.kitapi.util.Localization;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
        Main.getArenaManager().prepareKitSelection(player);
    }
}

