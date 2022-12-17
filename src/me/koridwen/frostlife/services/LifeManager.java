package me.koridwen.frostlife.services;



import me.koridwen.frostlife.FrostLife;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LifeManager {
    public static int maxLives = 4;
    public static ChatColor extraLivesColor = ChatColor.DARK_PURPLE;
    public static ChatColor firstLifeColor = ChatColor.LIGHT_PURPLE;
    public static ChatColor secondLifeColor = ChatColor.AQUA;
    public static ChatColor thirdLifeColor = ChatColor.DARK_BLUE;
    public static ChatColor spectatorColor = ChatColor.GRAY;
    public static int randomLifeCountMinimum = 3;
    public static int randomLifeCountMaximum = 7;

    public LifeManager() {
    }

    public static void setLife(Player p, Integer life) {
        if (p != null) {
            if (!FrostLife.explanations.containsKey(p.getUniqueId()))
                FrostLife.explanations.put(p.getUniqueId(), true);
            if (life >= 0) {
                if (FrostLife.s.getTeam("firstlife").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("firstlife").removeEntry(p.getName());
                }
                else if (FrostLife.s.getTeam("secondlife").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("secondlife").removeEntry(p.getName());
                }
                else if (FrostLife.s.getTeam("thirdlife").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("thirdlife").removeEntry(p.getName());
                }
                else if (FrostLife.s.getTeam("spectator").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("spectator").removeEntry(p.getName());
                }

                if (p.hasPermission("frostlife.bypass")) {
                    p.setPlayerListName(ChatColor.WHITE + p.getName());
                }
                else if (life > 3) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    p.setPlayerListName(ChatColor.WHITE + p.getName());
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("extralife").addEntry(p.getName());
                        p.setPlayerListName(extraLivesColor + p.getName());
                    }

                }
                else if (life == 3) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    p.setPlayerListName(ChatColor.WHITE + p.getName());
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("firstlife").addEntry(p.getName());
                        p.setPlayerListName(firstLifeColor + p.getName());
                    }

                }
                else if (life == 2) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    p.setPlayerListName(ChatColor.WHITE + p.getName());
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("secondlife").addEntry(p.getName());
                        p.setPlayerListName(secondLifeColor + p.getName());
                    }

                }
                else if (life == 1) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    p.setPlayerListName(ChatColor.WHITE + p.getName());
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("thirdlife").addEntry(p.getName());
                        p.setPlayerListName(thirdLifeColor + p.getName());
                    }

                }
                else  {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    p.setPlayerListName(ChatColor.WHITE + p.getName());
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("spectator").addEntry(p.getName());
                        p.setPlayerListName(spectatorColor + p.getName());
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                }
                Bukkit.broadcastMessage("set " + p.getDisplayName() + " lives to " + life);
            }
        }
    }

    public static void setLifeOfOfflinePlayer(OfflinePlayer p, Integer life) {
        if (!FrostLife.explanations.containsKey(p.getUniqueId()))
            FrostLife.explanations.put(p.getUniqueId(), true);
        if (p != null) {
            if (life >= 0) {
                if (FrostLife.s.getTeam("firstlife").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("firstlife").removeEntry(p.getName());
                } else if (FrostLife.s.getTeam("secondlife").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("secondlife").removeEntry(p.getName());
                } else if (FrostLife.s.getTeam("thirdlife").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("thirdlife").removeEntry(p.getName());
                } else if (FrostLife.s.getTeam("spectator").getEntries().contains(p.getName())) {
                    FrostLife.s.getTeam("spectator").removeEntry(p.getName());
                }

                if (life > 3) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("extralife").addEntry(p.getName());
                    }

                } else if (life == 3) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("firstlife").addEntry(p.getName());
                    }

                } else if (life == 2) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("secondlife").addEntry(p.getName());
                    }

                } else if (life == 1) {
                    FrostLife.lives.remove(p.getUniqueId());
                    FrostLife.lives.put(p.getUniqueId(), life);
                    if (FrostLife.enabled) {
                        FrostLife.s.getTeam("thirdlife").addEntry(p.getName());
                    }

                } else {
                    if (life == 0) {
                        FrostLife.lives.remove(p.getUniqueId());
                        FrostLife.lives.put(p.getUniqueId(), life);
                        if (FrostLife.enabled) {
                            FrostLife.s.getTeam("spectator").addEntry(p.getName());
                        }
                    }

                }
            }
        }
    }

    public static void setPlayerToRandomLife(final Player p) {
        final ArrayList<String> stringNumbers = new ArrayList();
        int min = randomLifeCountMinimum;

        for(int i = min; i <= randomLifeCountMaximum; ++i) {
            stringNumbers.add(getLifeColor(i) + "" + i);
        }

        Collections.shuffle(stringNumbers);
        (new BukkitRunnable() {
            public void run() {
                p.sendTitle(Messages.LIFE_RANDOMIZATION_START.message(), "", 20, 60, 20);
                (new BukkitRunnable() {
                    String life;
                    int lastNum;
                    int ticks;
                    double delay;
                    boolean finished;

                    {
                        this.life = ChatColor.GREEN + "3";
                        this.lastNum = 0;
                        this.ticks = 0;
                        this.delay = 0.0048;
                    }

                    public void run() {
                        if (this.finished) {
                            (new BukkitRunnable() {
                                public void run() {
                                    p.sendTitle(Messages.LIFE_RANDOMIZATION_FINAL.message().replace("%lives%", life), "", 20, 60, 20);
                                    p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5F, 1.0F);
                                    LifeManager.setLife(p, Integer.parseInt(ChatColor.stripColor(life)));
                                }
                            }).runTaskLater(FrostLife.getInstance(), 25L);
                            this.cancel();
                        } else {
                            ++this.ticks;
                            this.delay += 0.0048;
                            if ((double)this.ticks > this.delay * 10.0) {
                                this.ticks = 0;
                                int num = LifeManager.noRepeatRandomNumber(0, stringNumbers.size(), this.lastNum);
                                this.life = (String)stringNumbers.get(num);
                                this.lastNum = num;
                                p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0F, 1.0F);
                                p.sendTitle(this.life, "", 0, 30, 0);
                            }

                            if (this.delay >= 1.2) {
                                this.finished = true;
                            }

                        }
                    }
                }).runTaskTimer(FrostLife.getInstance(), 75L, 1L);
            }
        }).runTaskLater(FrostLife.getInstance(), 25L);
    }

    private static int noRepeatRandomNumber(int min, int max, int badNum) {
        int num;
        do {
            num = (int)(Math.random() * (double)(max - min) + (double)min);
        } while(num == badNum);

        return num;
    }

    public static ChatColor getLifeColor(int i) {
        if (i == 1)
            return thirdLifeColor;
        if (i == 2) {
            return secondLifeColor;
        } else {
            return i == 3 ? firstLifeColor : extraLivesColor;
        }
    }
}
