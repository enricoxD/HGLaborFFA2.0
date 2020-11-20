package de.hglabor.plugins.ffa.player;

import de.hglabor.plugins.ffa.util.ScoreboardFactory;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.util.Localization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PlayerData extends FFAPlayer {
    private Scoreboard scoreboard;
    private Objective objective;

    protected PlayerData(UUID uuid) {
        super(uuid);
    }

    @Override
    public List<AbstractKit> getKits() {
        return null;
    }

    @Override
    public boolean hasKit(AbstractKit abstractKit) {
        return false;
    }

    @Override
    public boolean areKitsDisabled() {
        return false;
    }

    @Override
    public boolean isInCombat() {
        return false;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public Objective getObjective() {
        return objective;
    }

    @Override
    public Locale getLocale() {
        return Localization.getPlayerLocale(uuid);
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
