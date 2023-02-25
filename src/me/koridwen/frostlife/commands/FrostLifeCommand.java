package me.koridwen.frostlife.commands;

import me.koridwen.frostlife.FrostLife;
import me.koridwen.frostlife.services.FrostbiteManager;
import me.koridwen.frostlife.services.LifeManager;
import me.koridwen.frostlife.services.Messages;
import me.koridwen.frostlife.services.NameTag;
import me.koridwen.frostlife.util.StringManipulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FrostLifeCommand implements CommandExecutor, TabCompleter {
    private final List<String> arguments = new ArrayList<>();
    private final List<String> secondArguments = new ArrayList<>();
    private final List<String> thirdArguments = new ArrayList<>();

    public FrostLifeCommand() {
        FrostLife.getInstance().getCommand("frostlife").setExecutor(this);
        FrostLife.getInstance().getCommand("frostlife").setTabCompleter(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("FrostLife") || label.equalsIgnoreCase("lf")) {
            Player target;

            if (args.length == 0) {
                return false;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("enable")) {
                    if (!FrostLife.enabled) {
                        FrostLife.getInstance().enable();
                        //w.getWorldBorder().setCenter(w.getSpawnLocation());
                        //w.getWorldBorder().setSize((double)FrostLife.worldBorder);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " enabled successfully"));
                    } else
                        sender.sendMessage("already enabled");
                       /* else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " disabled successfully"));
                            //w.getWorldBorder().setSize(6.0E7);

                            Player all;
                            for(Iterator var13 = Bukkit.getOnlinePlayers().iterator(); var13.hasNext(); all.setPlayerListName(all.getName())) {
                                all = (Player)var13.next();
                                if (FrostLife.s.getTeam("extralife").getEntries().contains(all.getName())) {
                                    FrostLife.s.getTeam("extralife").removeEntry(all.getName());
                                }

                                if (FrostLife.s.getTeam("firstlife").getEntries().contains(all.getName())) {
                                    FrostLife.s.getTeam("firstlife").removeEntry(all.getName());
                                }

                                if (FrostLife.s.getTeam("secondlife").getEntries().contains(all.getName())) {
                                    FrostLife.s.getTeam("secondlife").removeEntry(all.getName());
                                }

                                if (FrostLife.s.getTeam("thirdlife").getEntries().contains(all.getName())) {
                                    FrostLife.s.getTeam("thirdlife").removeEntry(all.getName());
                                }

                                if (FrostLife.s.getTeam("spectator").getEntries().contains(all.getName())) {
                                    FrostLife.s.getTeam("spectator").removeEntry(all.getName());
                                }
                            }

                            FrostLife.enabled = false;
                        }*/

                    return true;
                }

                if (args[0].equalsIgnoreCase("startsession")) {
                    if (!FrostLife.enabled) {
                        FrostLife.getInstance().enable();
                        FrostLife.getInstance().data.loadDataToFile();
                    }
                    if (FrostLife.hasSessionRunning)
                        sender.sendMessage(ChatColor.RED + "A session is already running");
                    else {
                        FrostLife.getInstance().startSession();
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("endsession")) {
                    if (!FrostLife.hasSessionRunning)
                        sender.sendMessage(ChatColor.RED + "No session is currently running");
                    else FrostLife.getInstance().endSession();
                    return true;
                }

                if (args[0].equalsIgnoreCase("reset")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " Reset Complete"));
                    FrostLife.getInstance().reset();
                    return true;
                }

                if (args[0].equalsIgnoreCase("liferitual")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " Life ritual Complete"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playEffect(EntityEffect.TOTEM_RESURRECT);
                        (new BukkitRunnable() {
                            public void run() {
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
                                p.sendTitle(Messages.GIVE_LIFE_RECEIVED_TITLE.message(), "", 15, 50, 18);
                                LifeManager.setLife(p, FrostLife.lives.get(p.getUniqueId()) + 1);
                            }
                        }).runTaskLater(FrostLife.getInstance(), 40L);
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("help")) {
                    return true;
                }
                if (args[0].equalsIgnoreCase("w")) {
                    sender.sendMessage(FrostbiteManager.waitingToStrike + "");
                }
            }
            if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("curse")) {
                    if (args[1].equalsIgnoreCase("roll")) {
                        if (FrostLife.hasSessionRunning) {
                            FrostLife.curseTimer.cancel();
                            FrostbiteManager.frostbiteQueue.clearQueue();
                            FrostbiteManager.startFrostbiteCycle();
                        }
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("strike")) {
                        if (FrostLife.hasSessionRunning) {
                            FrostbiteManager.strikeCurse();
                        }
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("getfrostbitten")) {
                        if (FrostLife.frostbitten != null)
                            sender.sendMessage(Bukkit.getPlayer(FrostLife.frostbitten).getName());
                        else
                            sender.sendMessage("no one is cursed yet");
                        return true;
                    }
                    return false;
                }
                if (args[0].equalsIgnoreCase("debug")) {
                    if (args[1].equalsIgnoreCase("savedata")) {
                        FrostLife.getInstance().data.loadDataToFile();
                        sender.sendMessage(ChatColor.GREEN + "data saved");
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("registerteams")) {
                        NameTag.registerNameTags();
                        for (UUID uuid : FrostLife.lives.keySet()) {
                            LifeManager.setLife(Bukkit.getPlayer(uuid), FrostLife.lives.get(uuid));
                        }
                        sender.sendMessage(ChatColor.GREEN + "teams registered");
                        return true;
                    }

                    if (args.length==3) {
                        if (args[1].equalsIgnoreCase("getlives")) {
                            Player p = Bukkit.getPlayer(args[2]);
                            if (p!=null)
                                sender.sendMessage(FrostLife.lives.get(p.getUniqueId()).toString());
                            else
                                sender.sendMessage(ChatColor.RED + "Invalid player name");
                            return true;
                        }

                        if (args[1].equalsIgnoreCase("addlife")) {
                            target = Bukkit.getPlayer(args[2]);
                        /*if (target == null) {
                            op = Bukkit.getOfflinePlayer(args[1]);
                            if (op.hasPlayedBefore() && FrostLife.lives.containsKey(op.getUniqueId())) {
                                this.addLifeToOfflinePlayer(sender, op);
                            }
                            else {
                                sender.sendMessage(ChatColor.RED + "Invalid player name");
                            }
                            return true;
                        }*/
                            if (target != null)
                                this.addLifeToOnlinePlayer(sender, target);
                            else
                                sender.sendMessage(ChatColor.RED + "Invalid player name");
                            return true;
                        }

                        if (args[1].equalsIgnoreCase("removelife")) {
                            target = Bukkit.getPlayer(args[2]);
                            /*if (target == null) {
                                op = Bukkit.getOfflinePlayer(args[1]);
                                if (op.hasPlayedBefore() && FrostLife.lives.containsKey(op.getUniqueId())) {
                                    this.removeLifeFromOfflinePlayer(sender, op);
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Invalid player name");
                                }

                                return true;
                            }*/
                            if (target != null)
                                this.removeLifeFromOnlinePlayer(sender, target);
                            else
                                sender.sendMessage(ChatColor.RED + "Invalid player name");
                            return true;
                        }

                        if (args[1].equalsIgnoreCase("randomizelife")) {
                            if (args[2].equals("@a")) {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (!p.hasPermission("frostlife.bypass")) {
                                        LifeManager.setPlayerToRandomLife(p);
                                    }
                                }
                                sender.sendMessage(ChatColor.GREEN + "Randomizing all online players life count...");
                            }
                            else {
                                target = Bukkit.getPlayer(args[2]);
                                if (target != null) {
                                    if (!target.hasPermission("frostlife.bypass")) {
                                        LifeManager.setPlayerToRandomLife(target);
                                        sender.sendMessage(ChatColor.GREEN + "Randomizing " + ChatColor.GOLD + target.getName() + "'s" + ChatColor.GREEN + " life count...");
                                    }
                                    else
                                        sender.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.RED + " has bypassed the command");
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public void addLifeToOnlinePlayer(CommandSender sender, Player target) {
        if (!target.hasPermission("frostlife.bypass")) {
            if (!FrostLife.lives.containsKey(target.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "Player not yet registered");
                return;
            }
            if (FrostLife.lives.get(target.getUniqueId()) != 0) {
                LifeManager.setLife(target, FrostLife.lives.get(target.getUniqueId()) + 1);
            }
            else if (FrostLife.lives.get(target.getUniqueId()) == 0) {
                LifeManager.setLife(target, 1);
                if (target.getGameMode() == GameMode.SPECTATOR) {
                    Location loc = target.getWorld().getHighestBlockAt((int)target.getLocation().getX(), (int)target.getLocation().getZ()).getLocation();
                    loc.setY(loc.getY() + 1.0);
                    loc.setPitch(target.getLocation().getPitch());
                    loc.setYaw(target.getLocation().getYaw());
                    target.teleport(loc);
                }
                target.setGameMode(GameMode.SURVIVAL);
                target.setHealth(20.0);
                if (FrostLife.enabled) {
                    target.sendTitle(Messages.LASTLIFE_WARNING_MAIN.message(), Messages.LASTLIFE_WARNING_SUB.message(), 10, 70, 10);
                }
            }

            sender.sendMessage(ChatColor.GREEN + "Added life to " + ChatColor.GOLD + target.getName());
            if (sender != target) {
                target.sendMessage(ChatColor.GREEN + "You have been given a life");
            }
        }
        else
            sender.sendMessage(ChatColor.RED + "Targeted player has the bypass permission");
    }

    /*public void addLifeToOfflinePlayer(CommandSender sender, OfflinePlayer target) {
        if (!FrostLife.lives.containsKey(target.getUniqueId())) {
            if (LifeManager.maxLives > 0) {
                LifeManager.setLifeOfOfflinePlayer(target, LifeManager.maxLives + 3);
            } else {
                LifeManager.setLifeOfOfflinePlayer(target, 3);
            }
        }

        if ((LifeManager.maxLives != 0 || (Integer)FrostLife.lives.get(target.getUniqueId()) != 3) && (LifeManager.maxLives <= 0 || (Integer)FrostLife.lives.get(target.getUniqueId()) != LifeManager.maxLives + 3)) {
            if (LifeManager.maxLives > 0 && (Integer)FrostLife.lives.get(target.getUniqueId()) >= 3 && (Integer)FrostLife.lives.get(target.getUniqueId()) != LifeManager.maxLives + 3) {
                LifeManager.setLifeOfOfflinePlayer(target, (Integer)FrostLife.lives.get(target.getUniqueId()));
            }

            if ((Integer)FrostLife.lives.get(target.getUniqueId()) != 0) {
                LifeManager.setLifeOfOfflinePlayer(target, (Integer)FrostLife.lives.get(target.getUniqueId()) + 1);
            } else if ((Integer)FrostLife.lives.get(target.getUniqueId()) == 0) {
                LifeManager.setLifeOfOfflinePlayer(target, 1);
            }

            sender.sendMessage(ChatColor.GREEN + "Added life to " + ChatColor.GOLD + target.getName());
        } else {
            sender.sendMessage(ChatColor.RED + "This player already has max lives");
        }
    }*/

    public void removeLifeFromOnlinePlayer(CommandSender sender, Player target) {
        if (!target.hasPermission("frostlife.bypass")) {
            if (!FrostLife.lives.containsKey(target.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "Player not yet registered");
                return;
            }

            if (FrostLife.lives.get(target.getUniqueId()) == 0) {
                sender.sendMessage(ChatColor.RED + "This player doesn't have any lives to remove");
                return;
            }

            if (FrostLife.lives.get(target.getUniqueId()) - 1 != 0 && FrostLife.lives.get(target.getUniqueId()) - 1 != 1) {
                LifeManager.setLife(target, FrostLife.lives.get(target.getUniqueId()) - 1);
            }
            else if (FrostLife.lives.get(target.getUniqueId()) - 1 == 1) {
                LifeManager.setLife(target, 1);
                if (FrostLife.enabled) {
                    target.sendTitle(Messages.LASTLIFE_WARNING_MAIN.message(), Messages.LASTLIFE_WARNING_SUB.message(), 10, 70, 10);
                }
            }
            else if (FrostLife.lives.get(target.getUniqueId()) - 1 == 0) {
                LifeManager.setLife(target, 0);
                target.setGameMode(GameMode.SPECTATOR);
            }

            sender.sendMessage(ChatColor.GREEN + "Removed life from " + ChatColor.GOLD + target.getName());
            if (sender != target) {
                target.sendMessage(ChatColor.GREEN + "You have had one life removed");
            }
        }
        else
            sender.sendMessage(ChatColor.RED + "Targeted player has the bypass permission");


    }

    /*public void removeLifeFromOfflinePlayer(CommandSender sender, OfflinePlayer target) {
        if (!FrostLife.lives.containsKey(target.getUniqueId())) {
            if (LifeManager.maxLives > 0) {
                LifeManager.setLifeOfOfflinePlayer(target, LifeManager.maxLives - 1);
            } else {
                LifeManager.setLifeOfOfflinePlayer(target, 2);
            }
        }

        if ((Integer)FrostLife.lives.get(target.getUniqueId()) == 0) {
            sender.sendMessage(ChatColor.RED + "This player doesn't have any lives to remove");
        } else {
            if ((Integer)FrostLife.lives.get(target.getUniqueId()) - 1 != 0 && (Integer)FrostLife.lives.get(target.getUniqueId()) - 1 != 1) {
                LifeManager.setLifeOfOfflinePlayer(target, (Integer)FrostLife.lives.get(target.getUniqueId()) - 1);
            } else if ((Integer)FrostLife.lives.get(target.getUniqueId()) - 1 == 1) {
                LifeManager.setLifeOfOfflinePlayer(target, 1);
            } else if ((Integer)FrostLife.lives.get(target.getUniqueId()) - 1 == 0) {
                LifeManager.setLifeOfOfflinePlayer(target, 0);
            }

            sender.sendMessage(ChatColor.GREEN + "Removed life from " + ChatColor.GOLD + target.getName());
        }
    }*/

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.arguments.isEmpty() && (sender.hasPermission("frostlife.manager") || sender.isOp()) && args.length == 1) {
            this.arguments.add("enable");
            this.arguments.add("startsession");
            this.arguments.add("endsession");
            this.arguments.add("curse");
            this.arguments.add("debug");
            return this.arguments;
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("curse")) {
            this.secondArguments.clear();
            this.secondArguments.add("roll");
            this.secondArguments.add("strike");
            this.secondArguments.add("getfrostbitten");
            return this.secondArguments;
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
            this.secondArguments.clear();
            this.secondArguments.add("savedata");
            this.secondArguments.add("registerteams");
            this.secondArguments.add("getlives");
            this.secondArguments.add("addlife");
            this.secondArguments.add("removelife");
            this.secondArguments.add("randomizelife");
            return this.secondArguments;
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("debug")) {
            if (args[1].equalsIgnoreCase("getLives") || args[1].equalsIgnoreCase("addlife") ||  args[1].equalsIgnoreCase("removelife")){
                this.thirdArguments.clear();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    this.thirdArguments.add(p.getName());
                }
                return this.thirdArguments;
            }
            if (args[1].equalsIgnoreCase("randomizelife")) {
                this.thirdArguments.clear();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    this.thirdArguments.add(p.getName());
                }
                this.thirdArguments.add("@a");
                return this.thirdArguments;
            }
            return null;

        }

        else {
            List<String> result = new ArrayList<>();
            if (args.length == 1) {
                for (String arg : this.arguments) {
                    if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(arg);
                    }
                }
                return result;
            }
            else {
                return null;
            }
        }
    }
}