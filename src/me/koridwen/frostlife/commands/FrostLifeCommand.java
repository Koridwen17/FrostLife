package me.koridwen.frostlife.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.koridwen.frostlife.FrostLife;
//import me.koridwen.frostlife.GUI.MainGUI;
//import me.koridwen.frostlife.services.BoogeymenManager;
import me.koridwen.frostlife.services.FrostbiteManager;
import me.koridwen.frostlife.services.LifeManager;
import me.koridwen.frostlife.services.Messages;
//import me.koridwen.frostlife.services.SoulBindManager;
import me.koridwen.frostlife.util.StringManipulator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class FrostLifeCommand implements CommandExecutor, TabCompleter {
    private List<String> arguments = new ArrayList();
    private List<String> secondArguments = new ArrayList();

    public FrostLifeCommand() {
        FrostLife.getInstance().getCommand("frostlife").setExecutor(this);
        FrostLife.getInstance().getCommand("frostlife").setTabCompleter(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("FrostLife") || label.equalsIgnoreCase("lf")) {
            Player target;

            if (sender instanceof Player) {
                target = (Player)sender;
                World w = target.getWorld();
                if (!target.hasPermission("frostlife.manager") || !target.isPermissionSet("frostlife.manager")) {
                    target.sendMessage(Messages.NO_COMMAND_PERMISSION.message());
                    return true;
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("enable")) {
                        if (!FrostLife.enabled) {
                            FrostLife.getInstance().enable();
                            w.getWorldBorder().setCenter(w.getSpawnLocation());
                            w.getWorldBorder().setSize((double)FrostLife.worldBorder);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " enabled successfully"));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " disabled successfully"));
                            w.getWorldBorder().setSize(6.0E7);

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
                        }

                        return true;
                    }

                    if (args[0].equalsIgnoreCase("startSession")) {
                        if (FrostLife.hasSessionRunning)
                            sender.sendMessage(ChatColor.RED + "A session is already running");
                        else {
                            FrostLife.getInstance().startSession();
                        }
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("endSession")) {
                        if (!FrostLife.hasSessionRunning)
                            sender.sendMessage(ChatColor.RED + "No session is currently running");
                        else FrostLife.getInstance().endSession();
                    }

                    if (args[0].equalsIgnoreCase("setSpawn")) {
                        w.setSpawnLocation(target.getLocation());
                        w.getWorldBorder().setCenter(target.getLocation());
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " Set world spawn successfully"));
                    }

                    if (args[0].equalsIgnoreCase("help")) {
                        return true;
                    }
                }

                else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("curse")) {
                        if (args[1].equalsIgnoreCase("roll")) {
                            if (FrostLife.hasSessionRunning) {
                                FrostLife.curseTimer.cancel();
                                FrostbiteManager.frostbiteQueue.clearQueue();
                                FrostbiteManager.startFrostbiteCycle();
                            }
                        }
                        if (args[1].equalsIgnoreCase("strike")) {
                            if (FrostLife.hasSessionRunning) {
                                FrostbiteManager.strikeCurse();
                            }
                        }
                        if (args[1].equalsIgnoreCase("getFrostbitten")) {
                            if (FrostLife.frostbitten !=null)
                                target.sendMessage(Bukkit.getPlayer(FrostLife.frostbitten).getName());
                            else target.sendMessage("no one is cursed yet");
                        }
                    }

                    if (args[0].equalsIgnoreCase("getLives") && args.length == 2) {
                        Player p = Bukkit.getPlayer(args[1]);
                        target.sendMessage(FrostLife.lives.get(p.getUniqueId()).toString());
                        target.sendMessage(FrostbiteManager.frostbiteQueue.toString());
                    }

                    if (args[0].equalsIgnoreCase("setCurse") && args.length == 2) {
                        Player p = Bukkit.getPlayer(args[1]);
                        if (FrostLife.frostbitten != null) {
                            FrostbiteManager.cancelEffects(Bukkit.getPlayer(FrostLife.frostbitten));
                            FrostLife.frostbitten = null;
                        }
                        FrostbiteManager.curseStage = 0;
                        FrostLife.frostbitten = p.getUniqueId();
                        FrostbiteManager.startCurse();
                    }

                    if (args[0].equalsIgnoreCase("setBorder") && args.length == 2) {
                        int size;
                        if (StringManipulator.isNumeric(args[1]) && FrostLife.enabled) {
                            size = Integer.parseInt(args[1]);
                            w.getWorldBorder().setCenter(w.getSpawnLocation());
                            w.getWorldBorder().setSize((double) size);
                            FrostLife.worldBorder = size;
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " Set world boarder size to &f[&6" + size + "&f]"));
                            return true;
                        }

                        if (!StringManipulator.isNumeric(args[1])) {
                            target.sendMessage(ChatColor.RED + "Invalid border size");
                            return true;
                        }

                        if (!FrostLife.enabled) {
                            size = Integer.parseInt(args[1]);
                            FrostLife.worldBorder = size;
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " Updated border to &f[&6" + size + "&f]"));
                            target.sendMessage(ChatColor.YELLOW + "(Enable the plugin to see the border)");
                        }
                    }
                }
            }
            else {
                if (args.length <= 0) {
                    sender.sendMessage(ChatColor.RED + "This is a player only command");
                    return false;
                }
                if (!args[0].equalsIgnoreCase("addlife") && !args[0].equalsIgnoreCase("removelife") && !args[0].equalsIgnoreCase("reset")) {
                    sender.sendMessage(ChatColor.RED + "This is a player only command");
                    return true;
                }
                if (!sender.hasPermission("frostlife.manager")) {
                    sender.sendMessage(Messages.NO_COMMAND_PERMISSION.message());
                    return true;
                }
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FrostLife.prefix + " Reset Complete"));
                    FrostLife.getInstance().reset();
                }
            }
            else if (args.length == 2) {
                OfflinePlayer op;
                if (args[0].equalsIgnoreCase("addlife")) {
                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        op = Bukkit.getOfflinePlayer(args[1]);
                        if (op.hasPlayedBefore() && FrostLife.lives.containsKey(op.getUniqueId())) {
                            this.addLifeToOfflinePlayer(sender, op);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid player name");
                        }

                        return true;
                    }

                    this.addLifeToOnlinePlayer(sender, target);
                }

                if (args[0].equalsIgnoreCase("removelife")) {
                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        op = Bukkit.getOfflinePlayer(args[1]);
                        if (op.hasPlayedBefore() && FrostLife.lives.containsKey(op.getUniqueId())) {
                            this.removeLifeFromOfflinePlayer(sender, op);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid player name");
                        }

                        return true;
                    }

                    this.removeLifeFromOnlinePlayer(sender, target);
                }

                if (args[0].equalsIgnoreCase("randomizelife")) {
                    if (args[1].equals("@a")) {
                        Iterator var9 = Bukkit.getOnlinePlayers().iterator();

                        while(var9.hasNext()) {
                            Player p = (Player)var9.next();
                            if (!p.hasPermission("frostlife.bypass")) {
                                LifeManager.setPlayerToRandomLife(p);
                            }
                        }

                        sender.sendMessage(ChatColor.GREEN + "Randomizing all online players life count...");
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (!target.hasPermission("frostlife.bypass")) {
                            LifeManager.setPlayerToRandomLife(target);
                            sender.sendMessage(ChatColor.GREEN + "Randomizing " + ChatColor.GOLD + target.getName() + "'s" + ChatColor.GREEN + " life count...");
                        } else {
                            sender.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.RED + " has bypassed the command");
                        }
                    }
                }
            }
        }
        return false;
    }

    public void addLifeToOnlinePlayer(CommandSender sender, Player target) {
        if (!target.hasPermission("frostlife.bypass")) {
            if (!FrostLife.lives.containsKey(target.getUniqueId())) {
                if (LifeManager.maxLives > 0) {
                    LifeManager.setLife(target, LifeManager.maxLives + 3);
                } else {
                    LifeManager.setLife(target, 3);
                }
            }

            if (LifeManager.maxLives == 0 && (Integer)FrostLife.lives.get(target.getUniqueId()) == 3 || LifeManager.maxLives > 0 && (Integer)FrostLife.lives.get(target.getUniqueId()) == LifeManager.maxLives + 3) {
                sender.sendMessage(ChatColor.RED + "This player already has max lives");
                return;
            }

            if (LifeManager.maxLives > 0 && (Integer)FrostLife.lives.get(target.getUniqueId()) >= 3 && (Integer)FrostLife.lives.get(target.getUniqueId()) != LifeManager.maxLives + 3) {
                LifeManager.setLife(target, (Integer)FrostLife.lives.get(target.getUniqueId()));
            }

            if ((Integer)FrostLife.lives.get(target.getUniqueId()) != 0) {
                LifeManager.setLife(target, (Integer)FrostLife.lives.get(target.getUniqueId()) + 1);
            } else if ((Integer)FrostLife.lives.get(target.getUniqueId()) == 0) {
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
        } else {
            sender.sendMessage(ChatColor.RED + "Targeted player has the bypass permission");
        }

    }

    public void addLifeToOfflinePlayer(CommandSender sender, OfflinePlayer target) {
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
    }

    public void removeLifeFromOnlinePlayer(CommandSender sender, Player target) {
        if (!target.hasPermission("frostlife.bypass")) {
            if (!FrostLife.lives.containsKey(target.getUniqueId())) {
                if (LifeManager.maxLives > 0) {
                    LifeManager.setLife(target, LifeManager.maxLives - 1);
                } else {
                    LifeManager.setLife(target, 2);
                }
            }

            if ((Integer)FrostLife.lives.get(target.getUniqueId()) == 0) {
                sender.sendMessage(ChatColor.RED + "This player doesn't have any lives to remove");
                return;
            }

            if ((Integer)FrostLife.lives.get(target.getUniqueId()) - 1 != 0 && (Integer)FrostLife.lives.get(target.getUniqueId()) - 1 != 1) {
                LifeManager.setLife(target, (Integer)FrostLife.lives.get(target.getUniqueId()) - 1);
            } else if ((Integer)FrostLife.lives.get(target.getUniqueId()) - 1 == 1) {
                LifeManager.setLife(target, 1);
                if (FrostLife.enabled) {
                    target.sendTitle(Messages.LASTLIFE_WARNING_MAIN.message(), Messages.LASTLIFE_WARNING_SUB.message(), 10, 70, 10);
                }
            } else if ((Integer)FrostLife.lives.get(target.getUniqueId()) - 1 == 0) {
                LifeManager.setLife(target, 0);
                target.setGameMode(GameMode.SPECTATOR);
            }

            sender.sendMessage(ChatColor.GREEN + "Removed life from " + ChatColor.GOLD + target.getName());
            if (sender != target) {
                target.sendMessage(ChatColor.GREEN + "You have had one life removed");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Targeted player has the bypass permission");
        }

    }

    public void removeLifeFromOfflinePlayer(CommandSender sender, OfflinePlayer target) {
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
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.arguments.isEmpty() && args.length == 1) {
            this.arguments.add("toggleexplanations");
            if (sender.hasPermission("frostlife.manager") || sender.isOp()) {
                this.arguments.add("enable");
                this.arguments.add("startsession");
                this.arguments.add("endsession");
                this.arguments.add("curse");
                //this.arguments.add("setSpawn");
                //this.arguments.add("setBorder");
                this.arguments.add("addLife");
                this.arguments.add("removeLife");
            }
        }

        if (this.arguments.isEmpty() && (sender.hasPermission("frostlife.manager") || sender.isOp()) && args.length == 1) {
            //this.arguments.add("enable");
            //this.arguments.add("reload");
            //this.arguments.add("reset");
            //this.arguments.add("randomizeLife");
            //this.arguments.add("randomizeSoulBindings");
            //this.arguments.add("pickBoogeymen");
            //this.arguments.add("GUI");
            //if (ConfigManager.enableTpOnDeath()) {
            //    this.arguments.add("setspectatorspawnpoint");
            //}
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("curse")) {
            this.secondArguments.clear();
            this.secondArguments.add("roll");
            this.secondArguments.add("strike");
            this.secondArguments.add("getFrostbitten");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("randomizelife")) {
            this.secondArguments.clear();
            Iterator var8 = Bukkit.getOnlinePlayers().iterator();

            while(var8.hasNext()) {
                Player p = (Player)var8.next();
                this.secondArguments.add(p.getName());
            }

            this.secondArguments.add("@a");
            return this.secondArguments;
        } else {
            List<String> result = new ArrayList();
            if (args.length == 1) {
                Iterator var6 = this.arguments.iterator();

                while(var6.hasNext()) {
                    String arg = (String)var6.next();
                    if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(arg);
                    }
                }

                return result;
            } else {
                return null;
            }
        }
    }
}