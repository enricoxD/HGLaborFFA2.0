package de.hglabor.plugins.ffa.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class FFAChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!event.isCancelled()) {
            event.setCancelled(true);
            String message = event.getMessage();
            if(event.getPlayer().isOp()) {
                message = message.replace("&", "§");
            }
            Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " §f» §f" + message);
        }
    }

}
