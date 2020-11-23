package de.hglabor.plugins.ffa.util;

import de.hglabor.plugins.kitapi.config.KitApiConfig;
import de.hglabor.plugins.kitapi.util.Localization;
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
            scoreboardPlayer.setObjective(scoreboardPlayer.getScoreboard().registerNewObjective("score", "dummy", ChatColor.BOLD + "HGLabor"));
            scoreboardPlayer.getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
            setBasicScoreboardLayout(scoreboardPlayer);
        }
        Player player = scoreboardPlayer.getPlayer();
        player.setScoreboard(scoreboardPlayer.getScoreboard());
        System.out.println("Bekommt scoreboard lol");
    }

    private static void setBasicScoreboardLayout(ScoreboardPlayer scoreboardPlayer) {
        int kitAmount = KitApiConfig.getInstance().getInteger("kit.amount");
        int lowestPosition = 7;
        int highestPosition = lowestPosition + kitAmount;
        addEntry(scoreboardPlayer, "reset", Localization.getMessage("scoreboard.mapReset", scoreboardPlayer.getLocale()), "", highestPosition + 3);
        //TODO  addEntry(scoreboardPlayer, "resetValue", TimeConverter.toShortTimeString(Main.getPhase().getTimer()), "", highestPosition + 2);
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
        addEntry(scoreboardPlayer, "players", Localization.getMessage("scoreboard.players", scoreboardPlayer.getLocale()), "", 5);
        addEntry(scoreboardPlayer, "playersValue", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), "", 4);
        addEntry(scoreboardPlayer, "3", "", "", 3);
    }

    public static void addEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, String suffix, int score) {
        if (scoreboardPlayer.getScoreboard().getTeam(name) != null) {
            Team team = scoreboardPlayer.getScoreboard().registerNewTeam(name);
            team.addEntry(ChatColor.values()[score] + "" + ChatColor.WHITE);
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            scoreboardPlayer.getObjective().getScore(ChatColor.values()[score] + "" + ChatColor.WHITE).setScore(score);
        }
    }

    public static void updateEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, String suffix) {
        Team team = scoreboardPlayer.getScoreboard().getTeam(name);
        if (team != null) {
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }

    public static void removeEntry(ScoreboardPlayer scoreboardPlayer, String name, int score) {
        Team team = scoreboardPlayer.getScoreboard().getTeam(name);
        if (team != null) {
            team.removeEntry(ChatColor.values()[score] + "" + ChatColor.WHITE);
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

