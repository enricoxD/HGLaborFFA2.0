package de.hglabor.plugins.ffa.world;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.gamemechanics.Feast;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.ffa.util.HideUtils;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.util.ItemBuilder;
import de.hglabor.plugins.kitapi.util.WorldEditUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

public class ArenaManager {
    private final World world;
    private final int size;
    private final File schematic;
    private final Location center;
    private Feast feast;

    public ArenaManager(World world, int mapSize) {
        this.size = mapSize;
        this.center = new Location(world, 0, 0, 0);
        this.schematic = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/arena.schem");
        this.feast = new Feast(world, randomSpawn(50), 20, 300, Material.GRASS_BLOCK);
        this.world = world;
        this.world.setTime(1000);
        this.world.setWeatherDuration(0);
        this.world.setThundering(false);
        this.world.getWorldBorder().setCenter(this.center);
        this.world.getWorldBorder().setDamageAmount(6);
        this.world.getWorldBorder().setSize(200);
        this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        this.world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        this.world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        this.copyMap();
    }

    public void prepareKitSelection(Player player) {
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(player);

       /* ffaPlayer.setStatus(NEW);
        ffaPlayer.setKills(0);
        ffaPlayer.getKits().forEach(kit -> kit.disable(player));
        ffaPlayer.setKits(KitManager.emptyKitList());
        ffaPlayer.stopCombatTimer();
        ffaPlayer.setKitCooldowns(new HashMap<>());
        ffaPlayer.setKitCooldownStarts(new HashMap<>());
        ffaPlayer.setKitAttributes(new HashMap<>()); */

        HideUtils.hideToInGamePlayers(player);
        HideUtils.showPlayersInKitSelection(player);

        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setGliding(false);
        player.setGlowing(false);
        player.setTotalExperience(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        //TODO Arrays.stream(MetaDatas.values()).forEach(metaData -> player.removeMetadata(metaData.getKey(), Main.getPlugin()));
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);

        player.closeInventory();
        player.getInventory().clear();

        //TODO:  KitSelector.getKIT_SELECTOR_ITEM().forEach(kitSelector -> player.getInventory().addItem(kitSelector));

        Location location = randomSpawn(40).clone().add(0, 20, 0);
        player.teleport(location);
    }

    public void teleportToArena(Player player) {
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(player);
        ffaPlayer.setStatus(FFAPlayer.Status.ARENA);
        HideUtils.hidePlayersInKitSelection(player);
        HideUtils.makeVisibleToInGamePlayers(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.teleport(randomSpawn(50).clone().add(0, 1, 0));
        this.giveArenaEquipment(player);
    }

    private void giveArenaEquipment(Player player) {
        FFAPlayer ffaPlayer = PlayerList.getInstance().getPlayer(player);
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(0, new ItemBuilder(Material.STONE_SWORD).setUnbreakable().build());
        inventory.setItem(1, new ItemBuilder(Material.COMPASS).setName("§cTracker").build());
        inventory.setItem(13, new ItemStack(Material.BOWL, 32));
        inventory.setItem(14, new ItemStack(Material.RED_MUSHROOM, 32));
        inventory.setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 32));
        int itemCount = 0;
        for (AbstractKit kit : ffaPlayer.getKits()) {
           /* if (!kit.isUsesOffHand()) {
                for (ItemStack kitItem : kit.getKitItems()) {
                    itemCount++;
                    inventory.setItem(1 + itemCount, kitItem);
                }
            } else {
                player.getInventory().setItemInOffHand(kit.getMainKitItem());
            }
            kit.enable(player);
        } */

            IntStream.range(0, 31 - itemCount).mapToObj(i -> new ItemStack(Material.MUSHROOM_STEW)).forEach(inventory::addItem);
        }
    }

    public void reloadMap() {
        for (Entity entity : world.getNearbyEntities(new BoundingBox(size, 0, size, -size, 120, -size))) {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }
        WorldEditUtils.pasteSchematic(world, center, schematic);
    }


    private void copyMap() {
        CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(this.world),
                BukkitAdapter.asBlockVector(new Location(this.world, this.size, 0, this.size)),
                BukkitAdapter.asBlockVector(new Location(this.world, -this.size, world.getMaxHeight(), -this.size)));

        BlockArrayClipboard blockArrayClipboard = new BlockArrayClipboard(region);
        blockArrayClipboard.setOrigin(BukkitAdapter.asBlockVector(this.center));

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, blockArrayClipboard, region.getMinimumPoint());
            forwardExtentCopy.setCopyingEntities(true);
            Operations.complete(forwardExtentCopy);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schematic))) {
            writer.write(blockArrayClipboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location randomSpawn(int spread) {
        Random ran = new Random();
        int randomX = ran.nextInt(spread + spread) - spread;
        int randomZ = ran.nextInt(spread + spread) - spread;
        int highestY = getHighestBock(world, randomX, randomZ).getBlockY();
        return new Location(world, randomX, highestY, randomZ);
    }

    public Location getHighestBock(World world, int x, int z) {
        int i = 255;
        while (i > 0) {
            if (new Location(world, x, i, z).getBlock().getType() != Material.AIR)
                return new Location(world, x, i, z).add(0, 1, 0);
            i--;
        }
        return new Location(world, x, 1, z);
    }

    public Feast getFeast() {
        return feast;
    }

    public void setFeast(Feast feast) {
        this.feast = feast;
    }
}

