package de.hglabor.plugins.ffa.gamemechanics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DurabilityFix implements Listener {

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();
        if (item.getType().name().endsWith("_SWORD") || item.getType().name().endsWith("_AXE")) {
            if (new Random().nextBoolean()) e.setCancelled(true);
        }
    }
}
