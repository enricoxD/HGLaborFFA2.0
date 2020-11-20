package de.hglabor.plugins.ffa.listener;

import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerData;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FFADeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        Player player = event.getEntity();
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(player);
        World world = player.getWorld();

        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            FFAPlayer ffaKiller = PlayerList.getInstance().getPlayer(killer);
            ffaKiller.increaseKills();
        }

        List<ItemStack> playerItems = new ArrayList<>(event.getDrops());
        List<ItemStack> itemsToDrop = new ArrayList<>(event.getDrops());

        //REMOVE KIT ITEMS FROM DROP
        for (AbstractKit kit : ffaPlayer.getKits()) {
            Consumer<? super ItemStack> itemStackConsumer = drop -> {
                for (ItemStack kitItem : kit.getKitItems()) {
                    if (drop.isSimilar(kitItem)) {
                        itemsToDrop.remove(drop);
                    }
                }
            };
            playerItems.forEach(itemStackConsumer);
        }

        itemsToDrop.forEach(drop -> world.dropItem(player.getLocation(), drop));
        world.dropItem(player.getLocation(), new ItemStack(Material.RED_MUSHROOM, 16));
        world.dropItem(player.getLocation(), new ItemStack(Material.BROWN_MUSHROOM, 16));
        world.dropItem(player.getLocation(), new ItemStack(Material.BOWL, 16));

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Main.getArenaManager().prepareKitSelection(player), 0);
    }
}

