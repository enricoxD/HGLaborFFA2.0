package de.hglabor.plugins.ffa.util;

import de.hglabor.utils.localization.Localization;
import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.kitapi.config.KitApiConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Locale;

public final class ScoreboardFactory {
    private ScoreboardFactory() {
    }

    public static void create(ScoreboardPlayer scoreboardPlayer) {
        if (scoreboardPlayer.getScoreboard() == null && scoreboardPlayer.getObjective() == null) {
            scoreboardPlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            Team collision = scoreboardPlayer.getScoreboard().registerNewTeam("collision");
            collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            scoreboardPlayer.setObjective(scoreboardPlayer.getScoreboard().registerNewObjective("ffa", "stats", ChatColor.BOLD + "HGLabor"));
            scoreboardPlayer.getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
            setBasicScoreboardLayout(scoreboardPlayer);
        }
        Player player = scoreboardPlayer.getPlayer();
        player.setScoreboard(scoreboardPlayer.getScoreboard());
    }

    private static void setBasicScoreboardLayout(ScoreboardPlayer scoreboardPlayer) {
        int kitAmount = KitApiConfig.getInstance().getInteger("kit.amount");
        int lowestPosition = 7;
        int highestPosition = lowestPosition + kitAmount;
        addEntry(scoreboardPlayer, "reset", Localization.INSTANCE.getMessage("scoreboard.mapReset", scoreboardPlayer.getLocale()), "", highestPosition + 3);
        addEntry(scoreboardPlayer, "resetValue", TimeConverter.stringify(Main.getFFARunnable().getTimer()), "", highestPosition + 2);
        addEntry(scoreboardPlayer, String.valueOf(highestPosition + 1), "", "", highestPosition + 1);
        if (kitAmount == 1) {
            addEntry(scoreboardPlayer, "kitValue" + 1, "Kit: None", "", highestPosition);
        } else if (kitAmount > 1) {
            for (int i = highestPosition; i > lowestPosition; i--) {
                addEntry(scoreboardPlayer, "kitValue" + (i - lowestPosition), "Kit" + (i - lowestPosition) + ": None", "", i);
            }
        }
        addEntry(scoreboardPlayer, "killsValue", "Kills: 0", "", lowestPosition);
        addEntry(scoreboardPlayer, "6", "", "", 6);
        addEntry(scoreboardPlayer, "players", Localization.INSTANCE.getMessage("scoreboard.players", scoreboardPlayer.getLocale()), "", 5);
        addEntry(scoreboardPlayer, "playersValue", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), "", 4);
        addEntry(scoreboardPlayer, "3", "", "", 3);
    }

    public static void addEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, String suffix, int score) {
        Team team = scoreboardPlayer.getScoreboard().registerNewTeam(name);
        team.addEntry(ChatColor.values()[score] + "" + ChatColor.WHITE);
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        scoreboardPlayer.getObjective().getScore(ChatColor.values()[score] + "" + ChatColor.WHITE).setScore(score);
    }

    public static void updateEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, String suffix) {
        if (scoreboardPlayer != null && scoreboardPlayer.getScoreboard() != null) {
            Team team = scoreboardPlayer.getScoreboard().getTeam(name);
            if (team != null) {
                team.setPrefix(prefix);
                team.setSuffix(suffix);
            }
        }
    }

    public static void removeEntry(ScoreboardPlayer scoreboardPlayer, String name, int score) {
        Team team = scoreboardPlayer.getScoreboard().getTeam(name);
        if (team != null) {
            team.removeEntry(ChatColor.values()[score] + "" + ChatColor.WHITE);
        }
    }

    public static void addPlayerToNoCollision(Player playerToAdd, ScoreboardPlayer scoreboardPlayer) {
        Team collision = scoreboardPlayer.getScoreboard().getTeam("collision");
        if (collision != null && !collision.hasEntry(playerToAdd.getName())) {
            collision.addEntry(playerToAdd.getName());
        }
    }

    public interface ScoreboardPlayer {
        Scoreboard getScoreboard();

        void setScoreboard(Scoreboard scoreboard);

        Objective getObjective();

        void setObjective(Objective objective);

        Locale getLocale();

        Player getPlayer();
    }
}

