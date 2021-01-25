package de.hglabor.plugins.ffa.kit;

import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEventHandler;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
    public void onNinjaSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer(event.getPlayer());
            useKit(kitPlayer, kit -> kit.onNinjaSneak(event));
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getInventory().getViewers().get(0));
        useKit(kitPlayer, kit -> kit.onCraftItem(event));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getEntity());
            useKit(kitPlayer, kit -> kit.onEntityDamage(event));
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity() instanceof Player) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getEntity());
            useKit(kitPlayer, kit -> kit.onEntityDeath(event));
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
    public void onPlayerKillsLivingEntity(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer(killer);
            useKitItem(kitPlayer, kit -> kit.onPlayerKillsLivingEntity(event));
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if(event.getEntity() instanceof Player) {
            KitPlayer kitPlayer = playerSupplier.getKitPlayer((Player) event.getEntity());
            useKit(kitPlayer, kit -> kit.onEntityResurrect(event));
        }
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
