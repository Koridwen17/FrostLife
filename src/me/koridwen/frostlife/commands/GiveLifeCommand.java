package me.koridwen.frostlife.commands;

import me.koridwen.frostlife.FrostLife;
import me.koridwen.frostlife.services.LifeManager;
import me.koridwen.frostlife.services.Messages;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GiveLifeCommand implements CommandExecutor {
    public Map<UUID, Long> cooldowns = new HashMap<>();

    public GiveLifeCommand() {
        FrostLife.getInstance().getCommand("givelife").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!label.equalsIgnoreCase("givelife")) {
            return true;
        }
        else if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "This is a player only command");
            return true;
        }
        else {
            if ((!p.hasPermission("frostlife.givelife") && !p.hasPermission("frostlife.manager")) || p.hasPermission("frostlife.bypass")) {
                p.sendMessage(Messages.NO_COMMAND_PERMISSION.message());
                return true;
            }
            else if (!FrostLife.enabled) {
                p.sendMessage(Messages.LASTLIFE_NOT_ENABLED.message());
                return true;
            }
            else if (args.length == 0) {
                return true;
            }
            else {
                if (this.cooldowns.containsKey(p.getUniqueId())) {
                    if (this.cooldowns.get(p.getUniqueId()) > System.currentTimeMillis()) {
                        p.sendMessage(Messages.GIVE_LIFE_COOLDOWN.message().replace("%seconds%", ((Long)this.cooldowns.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L + ""));
                        return true;
                    }

                    this.cooldowns.remove(p.getUniqueId());
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(Messages.PLAYER_NOT_FOUND.message().replace("%player%", args[0]));
                    return true;
                }
                else if (p.equals(target)) {
                    p.sendMessage(Messages.GIVE_LIFE_TO_SELF.message());
                    return true;
                }
                else if (FrostLife.lives.get(target.getUniqueId()) == 0) {
                    p.sendMessage(Messages.GIVE_LIFE_NO_REVIVAL.message());
                    return true;
                }
                else if (FrostLife.lives.get(p.getUniqueId()) <= 2) {
                    p.sendMessage(Messages.GIVE_LIFE_NO_LIVES.message());
                    return true;
                }
                /*else if (FrostLife.lives.get(target.getUniqueId()) == LifeManager.maxLives) {
                    p.sendMessage(Messages.GIVE_LIFE_MAX.message().replace("%player%", ChatColor.GOLD + target.getName()));
                    return true;
                }*/
                else {
                    this.cooldowns.put(p.getUniqueId(), System.currentTimeMillis() + 2000L);
                    this.giveLife(p, target);
                    return false;
                }
            }
        }
    }

    private void giveLife(final Player p, final Player target) {
        this.totemEffect(p);
        (new BukkitRunnable() {
            public void run() {
                GiveLifeCommand.this.greenParticleEffect(target);
                target.sendMessage(Messages.GIVE_LIFE_RECEIVED.message().replace("%player%", ChatColor.GOLD + p.getName()));
                target.sendTitle(Messages.GIVE_LIFE_RECEIVED_TITLE.message(), "", 15, 50, 18);
                LifeManager.setLife(p, FrostLife.lives.get(p.getUniqueId()) - 1);
                LifeManager.setLife(target, FrostLife.lives.get(target.getUniqueId()) + 1);
            }
        }).runTaskLater(FrostLife.getInstance(), 40L);
        p.sendMessage(Messages.GIVE_LIFE_SUCCESS.message().replace("%player%", ChatColor.GOLD + target.getName()));
    }

    private void totemEffect(Player p) {
        p.playEffect(EntityEffect.TOTEM_RESURRECT);
    }

    private void greenParticleEffect(final Player p) {
        (new BukkitRunnable() {
            int runs = 0;

            public void run() {
                if (this.runs == 2) {
                    this.cancel();
                } else {
                    Location loc = p.getLocation();
                    double radius = 0.6;

                    for(double y = 0.0; y <= 1.8; y += 0.01) {
                        double x = radius * Math.cos(y * 50.0);
                        double z = radius * Math.sin(y * 50.0);
                        p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, (double)((float)(loc.getX() + x)), (double)((float)(loc.getY() + y)), (double)((float)(loc.getZ() + z)), 0, 0.0, 0.0, 0.0, 1.0);
                    }

                    ++this.runs;
                }
            }
        }).runTaskTimerAsynchronously(FrostLife.getInstance(), 0L, 1L);
    }
}
