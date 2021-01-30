package de.hglabor.plugins.ffa;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.hglabor.plugins.ffa.commands.ReloadMapCommand;
import de.hglabor.plugins.ffa.commands.SuicideCommand;
import de.hglabor.plugins.ffa.config.FFAConfig;
import de.hglabor.plugins.ffa.gamemechanics.*;
import de.hglabor.plugins.ffa.kit.KitAbilityListener;
import de.hglabor.plugins.ffa.kit.KitItemListener;
import de.hglabor.plugins.ffa.kit.KitItemSupplierImpl;
import de.hglabor.plugins.ffa.kit.KitSelectorFFA;
import de.hglabor.plugins.ffa.listener.*;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.ffa.util.PacketByteBuf;
import de.hglabor.plugins.ffa.util.ScoreboardFactory;
import de.hglabor.plugins.ffa.util.ScoreboardManager;
import de.hglabor.plugins.ffa.world.ArenaManager;
import de.hglabor.plugins.ffa.world.ArenaSettings;
import de.hglabor.plugins.kitapi.config.KitApiConfig;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.KitManager;
import de.hglabor.utils.localization.Localization;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.StandardMessenger;

import java.nio.file.Paths;

public final class Main extends JavaPlugin {
    private static Main plugin;
    private static ArenaManager arenaManager;
    private static ProtocolManager protocolManager;
    private static FFARunnable ffaRunnable;

    public static FFARunnable getFFARunnable() {
        return ffaRunnable;
    }

    public static ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.loadLocalizationFiles();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "hglabor:blockhit");
        KitApiConfig.getInstance().register(Main.getPlugin().getDataFolder());
        KitManager.getInstance().register(PlayerList.getInstance(), KitItemSupplierImpl.INSTANCE, this);
        FFAConfig.load();
        World world = Bukkit.getWorld("world");
        arenaManager = new ArenaManager(world, FFAConfig.getInteger("ffa.size"));
        ffaRunnable = new FFARunnable(world, FFAConfig.getInteger("ffa.duration"));
        ffaRunnable.runTaskTimer(this, 0, 20);
        ScoreboardManager scoreboardManager = new ScoreboardManager();
        scoreboardManager.runTaskTimer(this, 0, 20);
        KitSelectorFFA.getInstance().register();


        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.CUSTOM_PAYLOAD) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packetContainer = event.getPacket();
                PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) packetContainer.getHandle();
                Player player = event.getPlayer();
                System.out.println(packet.tag);

                if (packet.tag.toString().equalsIgnoreCase("noriskclient:blockhit")) {
                    System.out.println(player.getName() + " started blockhitting");
                    PacketPlayOutCustomPayload newPacket = new PacketPlayOutCustomPayload(
                            new MinecraftKey(StandardMessenger.validateAndCorrectChannel("noriskclient:blockhit")),
                            new PacketDataSerializer(new PacketByteBuf(Unpooled.buffer()).writeString(player.getUniqueId().toString())));

                    Bukkit.getScheduler().runTask(Main.getPlugin(), () -> {
                        for (Player nearbyPlayer : player.getWorld().getNearbyPlayers(player.getLocation(), 15)) {
                            ((CraftPlayer) nearbyPlayer).getHandle().playerConnection.sendPacket(newPacket);
                        }
                    });

                }
                else if (packet.tag.toString().equalsIgnoreCase("noriskclient:release")) {
                    System.out.println(player.getName() + " stopped blockhitting");
                    PacketPlayOutCustomPayload newPacket = new PacketPlayOutCustomPayload(
                            new MinecraftKey(StandardMessenger.validateAndCorrectChannel("noriskclient:release")),
                            new PacketDataSerializer(new PacketByteBuf(Unpooled.buffer()).writeString(player.getUniqueId().toString())));

                    Bukkit.getScheduler().runTask(Main.getPlugin(), () -> {
                        for (Player nearbyPlayer : player.getWorld().getNearbyPlayers(player.getLocation(), 15)) {
                            ((CraftPlayer) nearbyPlayer).getHandle().playerConnection.sendPacket(newPacket);
                        }
                    });

                }
            }
        });

        this.registerListeners();
        this.registerCommands();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            FFAPlayer player = PlayerList.getInstance().getPlayer(onlinePlayer);
            PlayerList.getInstance().add(player);
            ScoreboardFactory.create(player);
            Bukkit.getOnlinePlayers().forEach(newPlayer -> {
                ScoreboardFactory.addPlayerToNoCollision(newPlayer, player);
            });
            arenaManager.prepareKitSelection(onlinePlayer);
        }
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(KitSelectorFFA.getInstance(), this);
        for (AbstractKit enabledKit : KitManager.getInstance().getEnabledKits()) {
            if (enabledKit instanceof Listener) {
                pluginManager.registerEvents((Listener) enabledKit, this);
            }
        }
        pluginManager.registerEvents(new ArenaSettings(), this);
        pluginManager.registerEvents(new KitAbilityListener(), this);
        pluginManager.registerEvents(new KitItemListener(), this);
        pluginManager.registerEvents(new FFAJoinListener(), this);
        pluginManager.registerEvents(new FFAQuitListener(), this);
        pluginManager.registerEvents(new FFADeathListener(), this);
        pluginManager.registerEvents(new FFAChatListener(), this);
        //mechanics
        pluginManager.registerEvents(new SoupHealing(), this);
        pluginManager.registerEvents(new Tracker(), this);
        pluginManager.registerEvents(new DamageNerf(), this);
        pluginManager.registerEvents(new DurabilityFix(), this);
        pluginManager.registerEvents(new Feast(), this);
        pluginManager.registerEvents(new RemoveHitCooldown(), this);
        pluginManager.registerEvents(new LastHitDetection(), this);
    }

    private void loadLocalizationFiles() {
        try {
            Localization.INSTANCE.loadLanguageFiles(Paths.get(Main.getPlugin().getDataFolder() + "/lang"), "§");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        this.getCommand("suicide").setExecutor(new SuicideCommand());
        this.getCommand("reloadmap").setExecutor(new ReloadMapCommand());
    }
}
