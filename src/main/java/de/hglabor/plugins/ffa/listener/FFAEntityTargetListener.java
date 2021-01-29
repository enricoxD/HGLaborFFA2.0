
package de.hglabor.plugins.ffa.listener;

import de.hglabor.plugins.kitapi.kit.KitManager;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class FFAEntityTargetListener implements Listener {
    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(event.getTarget() instanceof Player)) return;
        Player target = (Player) event.getTarget();
        KitPlayer kitPlayer = KitManager.getInstance().getPlayer(target);
        if (!kitPlayer.isValid()) {
            event.setCancelled(true);
        }
    }
}
