package de.hglabor.plugins.ffa.commands;

import de.hglabor.plugins.ffa.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadMapCommand extends AbstractCommand {
    public ReloadMapCommand() {
        super("reloadmap","hglabor.ffa.reloadmap");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (super.onCommand(sender, command, label, args)) {
            Main.getArenaManager().reloadMap();
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Reloaded Map");
            return true;
        }
        return false;
    }
}

