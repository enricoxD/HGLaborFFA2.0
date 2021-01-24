package de.hglabor.plugins.ffa.kit;

import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.kit.events.KitItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class KitItemListener extends KitItemHandler implements Listener {
    public KitItemListener() {
        super(PlayerList.getInstance());
    }

    @EventHandler
    public void disableHandSwapForOffHandKits(PlayerSwapHandItemsEvent event) {
        super.disableHandSwapForOffHandKits(event);
    }

    @EventHandler
    public void disableOffHandInventoryClick(InventoryClickEvent event) {
        super.disableOffHandInventoryClick(event);
    }

    @EventHandler
    public void cancelKitItemPlace(BlockPlaceEvent event) {
        super.cancelKitItemPlace(event);
    }

    @EventHandler
    public void cancelKitItemDrop(PlayerDropItemEvent event) {
        super.cancelKitItemDrop(event);
    }

    @EventHandler
    public void avoidKitItemDropOnPlayerDeath(ItemSpawnEvent event) {
        super.avoidKitItemDropOnPlayerDeath(event);
    }
}
