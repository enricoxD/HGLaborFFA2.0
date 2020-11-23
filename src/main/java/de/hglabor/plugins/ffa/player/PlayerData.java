package de.hglabor.plugins.ffa.player;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.KitManager;
import de.hglabor.plugins.kitapi.util.Localization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PlayerData extends FFAPlayer {
    private final List<AbstractKit> kits;
    private Scoreboard scoreboard;
    private Objective objective;

    protected PlayerData(UUID uuid) {
        super(uuid);
        kits = KitManager.getInstance().empty();
    }

    @Override
    public List<AbstractKit> getKits() {
        return kits;
    }

    @Override
    public boolean hasKit(AbstractKit abstractKit) {
        return kits.contains(abstractKit);
    }

    @Override
    public boolean areKitsDisabled() {
        return false;
    }

    @Override
    public void setKit(AbstractKit abstractKit, int i) {
        kits.set(i, abstractKit);
    }

    @Override
    public boolean hasKitCooldown(AbstractKit abstractKit) {
        return false;
    }

    @Override
    public boolean isInCombat() {
        return false;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public Objective getObjective() {
        return objective;
    }

    @Override
    public void setObjective(Objective objective) {
        this.objective = objective;
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
