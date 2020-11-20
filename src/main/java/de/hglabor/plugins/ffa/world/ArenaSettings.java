package de.hglabor.plugins.ffa.world;

import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ArenaSettings implements Listener {

    @EventHandler
    public void cancelDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer((Player) event.getDamager());
            if (ffaPlayer.isInKitSelection()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer((Player) event.getWhoClicked());
            if (ffaPlayer.isInKitSelection()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer((Player) event.getEntity());
            if (ffaPlayer.isInKitSelection()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelItemPickUp(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer((Player) event.getEntity());
            if (ffaPlayer.isInKitSelection()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelDropping(PlayerDropItemEvent event) {
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(event.getPlayer());
        if (ffaPlayer.isInKitSelection()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void cancelFoodLoose(FoodLevelChangeEvent event) {
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer((Player) event.getEntity());
        if (ffaPlayer.isInKitSelection()) {
            event.setCancelled(true);
        }
    }
}
