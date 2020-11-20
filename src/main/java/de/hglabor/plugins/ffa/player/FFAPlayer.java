package de.hglabor.plugins.ffa.player;

import de.hglabor.plugins.ffa.util.ScoreboardFactory;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;

import java.util.UUID;

public abstract class FFAPlayer implements KitPlayer, ScoreboardFactory.ScoreboardPlayer {
    protected final UUID uuid;
    protected final String name;
    protected int kills;
    protected Status status;

    protected FFAPlayer(UUID uuid) {
        this.uuid = uuid;
        this.status = Status.KITSELECTION;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
    }

    public void increaseKills() {
        this.kills++;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public boolean isInKitSelection() {
        return status == Status.KITSELECTION;
    }

    public boolean isInArena() {
        return status == Status.ARENA;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public abstract boolean isInCombat();

    public enum Status {
        KITSELECTION,ARENA,SPECTATOR,
    }
}
