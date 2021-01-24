package de.hglabor.plugins.ffa.util;

import de.hglabor.plugins.ffa.Main;
import de.hglabor.plugins.ffa.player.FFAPlayer;
import de.hglabor.plugins.ffa.player.PlayerList;
import de.hglabor.plugins.kitapi.config.KitApiConfig;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.kits.CopyCatKit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public final class ScoreboardManager extends BukkitRunnable {

    @Override
    public void run() {
        for (FFAPlayer ffaPlayer : PlayerList.getInstance().getPlayers()) {
            ScoreboardFactory.updateEntry(ffaPlayer, "playersValue", SPACE() + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), "");
            ScoreboardFactory.updateEntry(ffaPlayer, "killsValue", ChatColor.AQUA + "" + ChatColor.BOLD + "Kills: " + ChatColor.RESET + ffaPlayer.getKills(), "");
            ScoreboardFactory.updateEntry(ffaPlayer, "resetValue", TimeConverter.stringify(Main.getFFARunnable().getTimer()), "");

            boolean kitDisabled = ffaPlayer.areKitsDisabled();

            //could possibly be none -> name check
            if (KitApiConfig.getInstance().getInteger("kit.amount") > 0) {
                int index = 1;
                for (AbstractKit kit : ffaPlayer.getKits()) {
                    if (kit.equals(CopyCatKit.INSTANCE)) {
                        AbstractKit copiedKit = ffaPlayer.getKitAttribute(CopyCatKit.INSTANCE);
                        ScoreboardFactory.updateEntry(ffaPlayer,
                                "kitValue" + index, ChatColor.BLUE + "" + ChatColor.BOLD + "Kit" + (index == 1 ? "" : index) + ": " + ChatColor.RESET +
                                        (kitDisabled ? ChatColor.STRIKETHROUGH : ChatColor.RESET) + kit.getName() +
                                        "(" + (copiedKit != null ? ((AbstractKit) ffaPlayer.getKitAttribute(CopyCatKit.INSTANCE)).getName() : "None") + ")", "");
                    } else {
                        ScoreboardFactory.updateEntry(ffaPlayer, "kitValue" + index, ChatColor.BLUE + "" + ChatColor.BOLD + "Kit" + (KitApiConfig.getInstance().getInteger("kit.amount") == 1 ? "" : index) + ": " + ChatColor.RESET + (kitDisabled ? ChatColor.STRIKETHROUGH : ChatColor.RESET) + kit.getName(), "");
                    }
                    index++;
                }
            }
        }
    }

    private String SPACE() {
        return " ";
    }
}
