package me.koridwen.frostlife.listeners;

import me.koridwen.frostlife.FrostLife;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {
    public OnPlayerLeave() {
    }

    public void onLeave(PlayerQuitEvent evt) {
        Player p = evt.getPlayer();
        if (FrostLife.s.getTeam("firtlife").getEntries().contains(p.getName())) {
            FrostLife.s.getTeam("firstlife").removeEntry(p.getName());
        }

        if (FrostLife.s.getTeam("secondlife").getEntries().contains(p.getName())) {
            FrostLife.s.getTeam("secondlife").removeEntry(p.getName());
        }

        if (FrostLife.s.getTeam("thirdlife").getEntries().contains(p.getName())) {
            FrostLife.s.getTeam("thirdlife").removeEntry(p.getName());
        }

        if (FrostLife.s.getTeam("spectator").getEntries().contains(p.getName())) {
            FrostLife.s.getTeam("spectator").removeEntry(p.getName());
        }

    }
}