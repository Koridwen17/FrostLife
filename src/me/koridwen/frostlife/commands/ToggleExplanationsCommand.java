package me.koridwen.frostlife.commands;

import me.koridwen.frostlife.FrostLife;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ToggleExplanationsCommand implements CommandExecutor {

    public ToggleExplanationsCommand() {
        FrostLife.getInstance().getCommand("toggleexplanations").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!label.equalsIgnoreCase("toggleexplanations")) {
            return true;
        }
        else if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "This is a player only command");
            return true;
        }
        else {
            if (!FrostLife.explanations.containsKey(p.getUniqueId())) {
                FrostLife.explanations.put(p.getUniqueId(), true);
                p.sendMessage(ChatColor.GOLD + "toggled explanations on");
            }
            else if (FrostLife.explanations.get(p.getUniqueId())) {
                FrostLife.explanations.replace(p.getUniqueId(), false);
                p.sendMessage(ChatColor.GOLD + "toggled explanations off");
            }
            else if (!FrostLife.explanations.get(p.getUniqueId())) {
                FrostLife.explanations.replace(p.getUniqueId(), true);
                p.sendMessage(ChatColor.GREEN + "toggled explanations on");
            }
            return true;
        }
    }
}
