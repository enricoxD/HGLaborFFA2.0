package de.hglabor.plugins.ffa.kit;

import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.KitEventHandler;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KitAbilityListener extends KitEventHandler implements Listener {
    public KitAbilityListener() {
        super(PlayerList.getInstance());
    }

    @EventHandler
    public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getDamager());
            for (AbstractKit kit : kitPlayer.getKits()) {
                if (canUseKit(kitPlayer, kit)) {
                    kit.onPlayerAttacksLivingEntity(event, kitPlayer, (LivingEntity) event.getEntity());
                }
            }
        }
    }
}
