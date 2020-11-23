package de.hglabor.plugins.ffa.util;

import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.util.Localization;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class HideUtils implements Listener {
    private static final HideUtils instance = new HideUtils();
    private final Map<Locale, BossBar> bossBars = new HashMap<>();

    private HideUtils() {
        for (Locale supportedLanguage : Localization.getSupportedLanguages()) {
            bossBars.put(supportedLanguage, Bukkit.createBossBar(Localization.getMessage("bossBar.hideUtils", supportedLanguage), BarColor.RED, BarStyle.SOLID));
        }
    }

    public static HideUtils getInstance() {
        return instance;
    }

    public void hidePlayersInKitSelection(Player player) {
        for (FFAPlayer playerInKitSelection : PlayerList.getInstance().getPlayersInKitSelection()) {
            Player playerToHide = playerInKitSelection.getPlayer();
            player.hidePlayer(Main.getPlugin(), playerToHide);
        }
    }

    public void showPlayersInKitSelection(Player player) {
        for (FFAPlayer playerInKitSelection : PlayerList.getInstance().getPlayersInKitSelection()) {
            Player playerToShow = playerInKitSelection.getPlayer();
            player.showPlayer(Main.getPlugin(), playerToShow);
        }
    }

    public void hideToInGamePlayers(Player playerToHide) {
        bossBars.get(Localization.getPlayerLocale(playerToHide.getUniqueId())).addPlayer(playerToHide);
        for (FFAPlayer playerInArena : PlayerList.getInstance().getPlayersInArena()) {
            Player player = playerInArena.getPlayer();
            player.hidePlayer(Main.getPlugin(), playerToHide);
        }
    }

    public void makeVisibleToInGamePlayers(Player playerToShow) {
        bossBars.get(Localization.getPlayerLocale(playerToShow.getUniqueId())).removePlayer(playerToShow);
        for (FFAPlayer playerInArena : PlayerList.getInstance().getPlayersInArena()) {
            Player player = playerInArena.getPlayer();
            player.showPlayer(Main.getPlugin(), playerToShow);
        }
    }

    public void removeBossBars() {
        bossBars.values().forEach(BossBar::removeAll);
    }
}
