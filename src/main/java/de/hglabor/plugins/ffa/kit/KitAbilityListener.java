package de.hglabor.plugins.ffa.kit;

import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEventHandler;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class KitAbilityListener extends KitEventHandler implements Listener {
    public KitAbilityListener() {
        super(PlayerList.getInstance());
    }

    @EventHandler
    public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getDamager());
            useKit(kitPlayer, kit -> kit.onPlayerAttacksLivingEntity(event, kitPlayer, (LivingEntity) event.getEntity()));
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getEntity());
            useKit(kitPlayer, kit -> kit.onEntityDamageByEntityEvent(event));
        }
    }

    @EventHandler
    public void onNinjaSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer(event.getPlayer());
            useKit(kitPlayer, kit -> kit.onNinjaSneak(event));
        }
    }

    @EventHandler
    public void onPlayerRightClickPlayerWithKitItem(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer(event.getPlayer());
            useKitItem(kitPlayer, kit -> kit.onPlayerRightClickPlayerWithKitItem(event));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        KitPlayer kitPlayer = playerSupplier.getKitPlayer(event.getPlayer());
        useKit(kitPlayer, kit -> {
            kit.onPlayerMove(event);
        });
    }

    @EventHandler
    public void onPlayerRightClickKitItem(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer(event.getPlayer());
            useKitItem(kitPlayer, kit -> kit.onPlayerRightClickKitItem(event));
        }
    }

    public void useKit(KitPlayer kitPlayer, KitExecutor kitExecutor) {
        kitPlayer.getKits().stream().filter(kit -> canUseKit(kitPlayer, kit)).forEach(kitExecutor::execute);
    }

    public void useKitItem(KitPlayer kitPlayer, KitExecutor kitExecutor) {
        kitPlayer.getKits().stream().filter(kit -> canUseKitItem(kitPlayer, kit)).forEach(kitExecutor::execute);
    }

    @FunctionalInterface
    interface KitExecutor {
        void execute(AbstractKit kit);
    }
}
