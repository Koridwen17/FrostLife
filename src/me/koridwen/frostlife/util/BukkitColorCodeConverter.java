package me.koridwen.frostlife.util;

import org.bukkit.ChatColor;

public class BukkitColorCodeConverter {
    public BukkitColorCodeConverter() {
    }

    public static ChatColor getChatColor(String stg) {
        if (stg.equalsIgnoreCase("&0")) {
            return ChatColor.BLACK;
        } else if (stg.equalsIgnoreCase("&1")) {
            return ChatColor.DARK_BLUE;
        } else if (stg.equalsIgnoreCase("&2")) {
            return ChatColor.DARK_GREEN;
        } else if (stg.equalsIgnoreCase("&3")) {
            return ChatColor.DARK_AQUA;
        } else if (stg.equalsIgnoreCase("&4")) {
            return ChatColor.DARK_RED;
        } else if (stg.equalsIgnoreCase("&5")) {
            return ChatColor.DARK_PURPLE;
        } else if (stg.equalsIgnoreCase("&6")) {
            return ChatColor.GOLD;
        } else if (stg.equalsIgnoreCase("&7")) {
            return ChatColor.GRAY;
        } else if (stg.equalsIgnoreCase("&8")) {
            return ChatColor.DARK_GRAY;
        } else if (stg.equalsIgnoreCase("&9")) {
            return ChatColor.BLUE;
        } else if (stg.equalsIgnoreCase("&a")) {
            return ChatColor.GREEN;
        } else if (stg.equalsIgnoreCase("&b")) {
            return ChatColor.AQUA;
        } else if (stg.equalsIgnoreCase("&c")) {
            return ChatColor.RED;
        } else if (stg.equalsIgnoreCase("&d")) {
            return ChatColor.LIGHT_PURPLE;
        } else if (stg.equalsIgnoreCase("&e")) {
            return ChatColor.YELLOW;
        } else if (stg.equalsIgnoreCase("&f")) {
            return ChatColor.WHITE;
        } else if (stg.equalsIgnoreCase("&k")) {
            return ChatColor.MAGIC;
        } else if (stg.equalsIgnoreCase("&l")) {
            return ChatColor.BOLD;
        } else if (stg.equalsIgnoreCase("&m")) {
            return ChatColor.STRIKETHROUGH;
        } else if (stg.equalsIgnoreCase("&n")) {
            return ChatColor.UNDERLINE;
        } else if (stg.equalsIgnoreCase("&o")) {
            return ChatColor.ITALIC;
        } else {
            return stg.equalsIgnoreCase("&r") ? ChatColor.RESET : ChatColor.WHITE;
        }
    }
}
