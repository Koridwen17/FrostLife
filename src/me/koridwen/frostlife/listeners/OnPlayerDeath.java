package me.koridwen.frostlife.listeners;

import me.koridwen.frostlife.FrostLife;
import me.koridwen.frostlife.services.FrostbiteManager;
import me.koridwen.frostlife.services.LifeManager;
import me.koridwen.frostlife.services.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerDeath implements Listener {
    //public static List<UUID> killers = new ArrayList();

    public OnPlayerDeath() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Bukkit.broadcastMessage("test death 1");
        if (FrostLife.hasSessionRunning && !p.hasPermission("frostlife.bypass")) {
            if (FrostLife.lives.get(p.getUniqueId()) > 0) {
                if (FrostLife.lives.get(p.getUniqueId()) == 1) {
                    p.getWorld().strikeLightningEffect(p.getLocation());
                }
                (new BukkitRunnable() {
                    public void run() {
                        LifeManager.setLife(p, FrostLife.lives.get(p.getUniqueId()) - 1);
                        Bukkit.broadcastMessage("test death 2");
                    }
                }).runTaskLater(FrostLife.getInstance(), 20L);

            }
        }
        if (p.getUniqueId() == FrostLife.frostbitten) {
            if (FrostbiteManager.curseStage == 6) {
                //death message
                if (p.getKiller()!=null) {
                    String cursedName = p.getName();
                    String killerName = p.getKiller().getName();
                    event.setDeathMessage(event.getDeathMessage()
                            .replace(cursedName, LifeManager.getLifeColor(FrostLife.lives.get(p.getUniqueId())) + cursedName + ChatColor.WHITE)
                            .replace(killerName, LifeManager.getLifeColor(FrostLife.lives.get(p.getKiller().getUniqueId())) + killerName + ChatColor.WHITE)
                                    + Messages.FROSTBITE_PVP_DEATH.message());
                }
                else {
                    event.setDeathMessage(Messages.FROSTBITE_DEATH.message().replace("%player%", LifeManager.getLifeColor(FrostLife.lives.get(p.getUniqueId())) + p.getName()));
                }
                FrostbiteManager.cancelEffects(Bukkit.getPlayer(FrostLife.frostbitten));
                FrostLife.frostbitten = null;
            }
            else if (FrostLife.lives.get(p.getUniqueId()) == 2) {
                p.sendMessage(Messages.FROSTBITE_ONE_LIFE.message());
                FrostbiteManager.cancelEffects(Bukkit.getPlayer(FrostLife.frostbitten));
                FrostLife.frostbitten = null;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if (FrostLife.enabled && !p.hasPermission("frostlife.bypass")) {
            if (p.getUniqueId() == FrostLife.frostbitten && FrostbiteManager.curseStage < 6) {
                (new BukkitRunnable() {
                    public void run() {
                        FrostbiteManager.putBackEffects(p);
                    }
                }).runTaskLater(FrostLife.getInstance(), 20L);
            }
        }
    }



}