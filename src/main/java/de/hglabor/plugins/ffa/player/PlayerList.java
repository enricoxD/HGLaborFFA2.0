package de.hglabor.plugins.ffa.player;

import de.hglabor.plugins.kitapi.player.KitPlayer;
import de.hglabor.plugins.kitapi.player.KitPlayerSupplier;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class PlayerList implements KitPlayerSupplier {
    private static final PlayerList instance = new PlayerList();
    private final Map<UUID, FFAPlayer> players;

    private PlayerList() {
        this.players = new HashMap<>();
    }

    public FFAPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public FFAPlayer getPlayer(UUID uuid) {
        return players.computeIfAbsent(uuid, PlayerData::new);
    }

    public void add(FFAPlayer ffaPlayer) {
        players.put(ffaPlayer.getUUID(), ffaPlayer);
    }

    public void remove(Player player) {
        remove(player.getUniqueId());
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

    @Override
    public KitPlayer getKitPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public List<FFAPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public List<FFAPlayer> getPlayersInKitSelection() {
        return players.values().stream().filter(FFAPlayer::isInKitSelection).collect(Collectors.toList());
    }

    public List<FFAPlayer> getPlayersInArena() {
        return players.values().stream().filter(FFAPlayer::isInArena).collect(Collectors.toList());
    }

    public static PlayerList getInstance() {
        return instance;
    }
}
