package me.koridwen.frostlife.listeners;

import me.koridwen.frostlife.FrostLife;
import me.koridwen.frostlife.services.FrostbiteManager;
import me.koridwen.frostlife.services.LifeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerJoin implements Listener {
    public OnPlayerJoin() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        final Player p = evt.getPlayer();
        if (!FrostLife.lives.containsKey(p.getUniqueId())) {
            (new BukkitRunnable() {
                public void run() {
                    if (!p.hasPermission("frostlife.bypass")) {
                        LifeManager.setLife(p, 3);
                        LifeManager.setPlayerToRandomLife(p);
                    }
                }
            }).runTaskLaterAsynchronously(FrostLife.getInstance(), 5L);
        }
        else {
            if (!p.hasPermission("frostlife.bypass")) {
                LifeManager.setLife(p, FrostLife.lives.get(p.getUniqueId()));
            }

        }
        //if (p.getUniqueId() == FrostLife.frostbitten) {
            if (p.getUniqueId()==FrostLife.frostbitten)
            if (FrostbiteManager.waitingToStrike) {
                (new BukkitRunnable() {
                    public void run() {
                        FrostbiteManager.strikeCurse();
                    }
                }).runTaskLater(FrostLife.getInstance(), 100L);

            }
            else {
                (new BukkitRunnable() {
                    public void run() {
                        FrostbiteManager.putBackEffects(p);
                        if (FrostbiteManager.curseStage == 6) {
                            FrostbiteManager.freezeIncrease.runTaskTimer(FrostLife.getInstance(), 0L, 1L);
                        }

                    }
                }).runTaskLater(FrostLife.getInstance(), 100L);
            }
        //}
    }
}