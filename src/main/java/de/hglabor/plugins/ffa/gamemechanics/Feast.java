package de.hglabor.plugins.ffa.gamemechanics;

import com.google.common.collect.ImmutableMap;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.regions.CylinderRegion;
import de.hglabor.Localization.Localization;
import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.util.TimeConverter;
import de.hglabor.plugins.kitapi.util.RandomCollection;
import de.hglabor.plugins.kitapi.util.Utils;
import de.hglabor.plugins.kitapi.util.WorldEditUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Feast implements Listener {
    private World world;
    private Location feastCenter;
    private Material platformMaterial;
    private int radius, timer, totalTime;
    private boolean inPreparation, isFinished;
    private BossBar feastBossBar;
    private final Set<Block> feastBlocks = new HashSet<>();
    private final Random random = new Random();

    public Feast() {
    }

    public Feast(World world, Location feastCenter, int radius, int seconds, Material platformMaterial) {
        this.world = world;
        this.radius = radius;
        this.timer = seconds;
        this.totalTime = seconds;
        this.platformMaterial = platformMaterial;
        this.feastCenter = feastCenter;
        this.feastBossBar = Bukkit.createBossBar("", BarColor.PURPLE, BarStyle.SOLID);
    }

    public void spawn() {
        inPreparation = true;
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 1, 1));

        WorldEditUtils.createCylinder(world, feastCenter, radius, true, 1, platformMaterial);
        int airHeight = 10;
        WorldEditUtils.createCylinder(world, feastCenter.clone().add(0, 1, 0), radius, true, airHeight, Material.AIR);

        CylinderRegion cylinderRegion = new CylinderRegion(BukkitAdapter.adapt(world),
                BukkitAdapter.asBlockVector(feastCenter), Vector2.at(radius, radius),
                feastCenter.getBlockY(), feastCenter.getBlockY() + airHeight);

        for (BlockVector3 blockVector3 : cylinderRegion) {
            feastBlocks.add(world.getBlockAt(BukkitAdapter.adapt(world, blockVector3)));
        }
        this.feastBlocks.forEach(feastBlock -> feastBlock.setMetadata("feastBlock", new FixedMetadataValue(Main.getPlugin(), "")));
        this.setBossBarForEveryone();
        this.startCountDown();
    }

    private void spawnFeastLoot() {

        feastCenter.clone().add(0, 1, 0).getBlock().setType(Material.ENCHANTING_TABLE);

        Location[] chestLocations = new Location[12];

        chestLocations[0] = feastCenter.clone().add(1, 1, 1);
        chestLocations[1] = feastCenter.clone().add(-1, 1, 1);
        chestLocations[2] = feastCenter.clone().add(-1, 1, -1);
        chestLocations[3] = feastCenter.clone().add(1, 1, -1);
        chestLocations[4] = feastCenter.clone().add(2, 1, 2);
        chestLocations[5] = feastCenter.clone().add(0, 1, 2);
        chestLocations[6] = feastCenter.clone().add(-2, 1, 2);
        chestLocations[7] = feastCenter.clone().add(2, 1, 0);
        chestLocations[8] = feastCenter.clone().add(-2, 1, 0);
        chestLocations[9] = feastCenter.clone().add(2, 1, -2);
        chestLocations[10] = feastCenter.clone().add(0, 1, -2);
        chestLocations[11] = feastCenter.clone().add(-2, 1, -2);

        Arrays.stream(chestLocations).forEach(chestLocation -> chestLocation.getBlock().setType(Material.CHEST));

        //FEAST ITEMS

        RandomCollection<ItemStack> diamondItems = new RandomCollection<>();
        diamondItems.add(1, new ItemStack(Material.DIAMOND_HELMET));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_CHESTPLATE));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_LEGGINGS));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_BOOTS));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_SWORD));

        RandomCollection<ItemStack> netheriteItems = new RandomCollection<>();
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_HELMET));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_CHESTPLATE));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_LEGGINGS));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_BOOTS));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_SWORD));

        RandomCollection<ItemStack> sizeableItems = new RandomCollection<>();
        sizeableItems.add(1, new ItemStack(Material.SPECTRAL_ARROW));
        sizeableItems.add(1, new ItemStack(Material.LAPIS_LAZULI));
        sizeableItems.add(1, new ItemStack(Material.COOKED_BEEF));
        sizeableItems.add(1, new ItemStack(Material.COOKED_PORKCHOP));
        sizeableItems.add(1, new ItemStack(Material.COOKED_CHICKEN));
        sizeableItems.add(1, new ItemStack(Material.MUSHROOM_STEW));

        RandomCollection<ItemStack> singleItems = new RandomCollection<>();
        singleItems.add(1, new ItemStack(Material.BOW));
        singleItems.add(1, new ItemStack(Material.COBWEB));
        singleItems.add(1, new ItemStack(Material.FLINT_AND_STEEL));
        singleItems.add(1, new ItemStack(Material.TNT));
        singleItems.add(1, new ItemStack(Material.ENDER_PEARL));
        singleItems.add(1, new ItemStack(Material.LAVA_BUCKET));
        singleItems.add(1, new ItemStack(Material.WATER_BUCKET));

        RandomCollection<RandomCollection<ItemStack>> lootPool = new RandomCollection<>();
        lootPool.add(20, diamondItems);
        lootPool.add(33, sizeableItems);
        lootPool.add(33, singleItems);
        lootPool.add(10, netheriteItems);

        final int ITEMS_TO_PLACE = 6;

        for (Location chestLocation : chestLocations) {
            Chest chest = (Chest) chestLocation.getBlock().getState();
            for (int i = 0; i < ITEMS_TO_PLACE; i++) {
                RandomCollection<ItemStack> randomItemCollection = lootPool.getRandom();
                ItemStack item = randomItemCollection.getRandom();

                if (randomItemCollection.equals(sizeableItems)) {
                    item.setAmount(random.nextInt(10) + 1);
                }

                //Lowers Duration of Armor in FFA
                if (randomItemCollection.equals(netheriteItems) || randomItemCollection.equals(diamondItems)) {
                    Damageable damageable = (Damageable) item.getItemMeta();
                    int maxDurability = item.getType().getMaxDurability();
                    damageable.setDamage(maxDurability - random.nextInt(maxDurability / 4));
                    item.setItemMeta((ItemMeta) damageable);
                }


                chest.getInventory().setItem(random.nextInt(26 - 1) + 1, item);
            }
        }
    }

    private void startCountDown() {
        new BukkitRunnable() {
            @Override
            public void run() {

                feastBossBar.setTitle( Localization.INSTANCE.getMessage("feast.broadcastMessage", ImmutableMap.of("centerLocation", printFeastCenter(), "timeString", TimeConverter.stringify(timer)), Locale.ENGLISH));
                feastBossBar.setProgress(timer / totalTime);

                if (timer % 60 == 0 || timer < 10) {
                    Utils.broadcastMessage("feast.broadcastMessage", ImmutableMap.of("centerLocation", printFeastCenter(), "timeString", TimeConverter.stringify(timer)));
                }

                timer--;

                if (timer <= 0) {
                    //CHEST SPAWNING
                    inPreparation = false;
                    isFinished = true;
                    feastBossBar.removeAll();
                    feastBlocks.forEach(feastBlock -> feastBlock.removeMetadata("feastBlock", Main.getPlugin()));
                    spawnFeastLoot();
                    cancel();
                }

            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public String printFeastCenter() {
        return "[" + (int) feastCenter.getX() + ", " + (int) feastCenter.getY() + ", " + (int) feastCenter.getZ() + "]";
    }

    private void setBossBarForEveryone() {
        Bukkit.getOnlinePlayers().forEach(player -> feastBossBar.addPlayer(player));
    }

    @EventHandler
    public void onBreakFeastBlock(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata("feastBlock")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceFeastBlock(BlockPlaceEvent event) {
        if (event.getBlockAgainst().hasMetadata("feastBlock")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosionFeastEvent(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (block.hasMetadata("feastBlock")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void setFeastBossBar(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Feast feast = Main.getArenaManager().getFeast();
        if (feast != null && feast.inPreparation) {
            feast.feastBossBar.addPlayer(player);
        }
    }

    public void removeBossBars() {
        feastBossBar.removeAll();
    }
}

