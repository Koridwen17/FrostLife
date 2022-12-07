package me.koridwen.frostlife.services;

import me.koridwen.frostlife.FrostLife;

import java.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class FrostbiteManager {
    private static BukkitRunnable addWeakness;
    private static BukkitRunnable addHunger;
    private static BukkitRunnable addSlowness;
    private static BukkitRunnable addAmbientParticles;
    private static BukkitRunnable addParticles;
    private static BukkitRunnable addFreezingDamage;
    public static BukkitRunnable freezeIncrease;
    private static BukkitRunnable occasionalNausea;
    private static int freezeTicks;
    public static FrostbiteQueue frostbiteQueue = new FrostbiteQueue();
    public static int curseStage = 0;
    public static Random random = new Random();


    public static void startFrostbiteCycle() {
        //complete reset of curse
        curseStage = 0;
        if (FrostLife.frostbitten != null) {
            cancelEffects(Bukkit.getPlayer(FrostLife.frostbitten));
            FrostLife.frostbitten=null;
        }

        //putting the curse on someone random
        (new BukkitRunnable() {
            public void run() {
                if (infectRandom())
                    startCurse();
            }
        }).runTaskLater(FrostLife.getInstance(),200L);

        //planning curse strike
        (new BukkitRunnable() {
            public void run() {
                strikeCurse();
            }
        }).runTaskLater(FrostLife.getInstance(),generateSessionLength());

    }

    public static void startCurse() {
        //resetting curse progression
        curseStage = 0;
        freezeTicks = 0;
        //registering cursed player
        frostbiteQueue.updateQueue();
        frostbiteQueue.addPlayer(FrostLife.frostbitten);
        Player cursed = Bukkit.getPlayer(FrostLife.frostbitten);
        //announcement
        cursed.sendTitle(Messages.FROSTBITE_HIT_TITLE.message(), "",10,40,20);
        cursed.playSound(cursed.getLocation(),Sound.BLOCK_POWDER_SNOW_STEP, 2.5f, 0.5f);
        cursed.playSound(cursed.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.7f, 0.8f);
        //explanations
        (new BukkitRunnable() {
            public void run() {
                if (FrostLife.explanations.get(cursed.getUniqueId()) || !FrostLife.explanations.containsKey(cursed.getUniqueId())) {
                    cursed.sendMessage(Messages.FROSTBITE_DESCRIPTION_1.message());
                    cursed.playSound(cursed.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 0.2f, 1.0f);
                    (new BukkitRunnable() {
                        public void run() {
                            cursed.sendMessage(Messages.FROSTBITE_DESCRIPTION_2.message());
                            cursed.playSound(cursed.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 0.2f, 1.0f);
                            (new BukkitRunnable() {
                                public void run() {
                                    cursed.sendMessage(Messages.FROSTBITE_DESCRIPTION_3.message());
                                    cursed.playSound(cursed.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 0.2f, 1.0f);
                                }
                            }).runTaskLater(FrostLife.getInstance(), 80L);
                        }
                    }).runTaskLater(FrostLife.getInstance(), 60L);
                }
            }
        }).runTaskLater(FrostLife.getInstance(), 80L);

        //planning curse progression
        defineTasks();
        cursed.setFreezeTicks(cursed.getMaxFreezeTicks());
        addWeakness.runTaskLater(FrostLife.getInstance(), 3000L);
        addHunger.runTaskLater(FrostLife.getInstance(), 6000L);
        addSlowness.runTaskLater(FrostLife.getInstance(), 9000L);
        addAmbientParticles.runTaskLater(FrostLife.getInstance(), 12000L);
        addParticles.runTaskLater(FrostLife.getInstance(), 18000L);
        addFreezingDamage.runTaskLater(FrostLife.getInstance(), 24000L);
        freezeIncrease.runTaskTimer(FrostLife.getInstance(),24000L,1L);
        occasionalNausea.runTaskTimer(FrostLife.getInstance(), 300L, 340L);

    }

    private static void defineTasks() {
        Player cursed = Bukkit.getPlayer(FrostLife.frostbitten);
        addWeakness = new BukkitRunnable() {
            public void run() {
                cursed.sendMessage(Messages.FROSTBITE_SPREAD.message());
                cursed.playSound(cursed.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.5f,0.5f);
                (new PotionEffect(PotionEffectType.WEAKNESS, 72000, 0, false, false, false)).apply(cursed);
                curseStage=1;
            }
        };

        addHunger = new BukkitRunnable() {
            public void run() {
                cursed.sendMessage(Messages.FROSTBITE_SPREAD.message());
                cursed.playSound(cursed.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.5f,0.5f);
                (new PotionEffect(PotionEffectType.HUNGER, 72000, 0, false, false, false)).apply(cursed);
                curseStage=2;
            }
        };

        addSlowness = new BukkitRunnable() {
            public void run() {
                cursed.sendMessage(Messages.FROSTBITE_SPREAD.message());
                cursed.playSound(cursed.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.5f,0.5f);
                (new PotionEffect(PotionEffectType.SLOW, 72000, 0, false, false, false)).apply(cursed);
                curseStage=3;
            }
        };

        addAmbientParticles = new BukkitRunnable() {
            public void run() {
                cursed.sendMessage(Messages.FROSTBITE_SPREAD.message());
                cursed.playSound(cursed.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.5f,0.5f);
                cursed.removePotionEffect(PotionEffectType.SLOW);
                (new PotionEffect(PotionEffectType.SLOW, 72000, 0, true, true, false)).apply(cursed);
                curseStage=4;
            }
        };

        addParticles = new BukkitRunnable() {
            public void run() {
                cursed.sendMessage(Messages.FROSTBITE_SPREAD.message());
                cursed.playSound(cursed.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.5f,0.5f);
                cursed.removePotionEffect(PotionEffectType.SLOW);
                (new PotionEffect(PotionEffectType.SLOW, 72000, 0, false, true, false)).apply(cursed);
                curseStage=5;
            }
        };

        addFreezingDamage = new BukkitRunnable() {
            @Override
            public void run() {
                cursed.sendMessage(Messages.FROSTBITE_SPREAD.message());
                cursed.playSound(cursed.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.5f,0.5f);
                curseStage=6;
            }
        };
        freezeIncrease = new BukkitRunnable() {
            @Override
            public void run() {
                Player cursed = Bukkit.getPlayer(FrostLife.frostbitten);
                if (freezeTicks >= cursed.getMaxFreezeTicks() || cursed.getFreezeTicks()>=cursed.getMaxFreezeTicks()) {
                    cursed.setFreezeTicks(72000);
                    freezeTicks=0;
                    this.cancel();
                }
                else {
                    cursed.setFreezeTicks(freezeTicks);
                    freezeTicks++;
                }
            }
        };

        occasionalNausea = new BukkitRunnable() {
            @Override
            public void run() {
                if (random.nextInt(10)==0)
                    (new PotionEffect(PotionEffectType.CONFUSION, 200, 1, false, false, false)).apply(cursed);
            }
        };

    }

    public static void infection (Player infecter, Player infected) {
        FrostLife.frostbitten = infected.getUniqueId();
        cancelEffects(infecter);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(infected.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.0f);
        }
        bigSnowball(infected.getLocation());
        (new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0, false, true, false)).apply(infected);
        startCurse();
    }

    public static void cancelEffects(Player cursed) {
        cursed.removePotionEffect(PotionEffectType.WEAKNESS);
        cursed.removePotionEffect(PotionEffectType.HUNGER);
        cursed.removePotionEffect(PotionEffectType.CONFUSION);
        cursed.removePotionEffect(PotionEffectType.SLOW);

        addWeakness.cancel();
        addHunger.cancel();
        addSlowness.cancel();
        addAmbientParticles.cancel();
        addParticles.cancel();
        addFreezingDamage.cancel();
        freezeIncrease.cancel();
        occasionalNausea.cancel();

        curseStage=0;
        freezeTicks=0;
        cursed.setFreezeTicks(0);
    }

    public static void putBackEffects(Player cursed) {
        if (curseStage >= 1) {
            (new PotionEffect(PotionEffectType.WEAKNESS, 72000, 0, false, false, false)).apply(cursed);
        }
        if (curseStage >= 2) {
            (new PotionEffect(PotionEffectType.HUNGER, 72000, 0, false, false, false)).apply(cursed);
        }
        if (curseStage >= 3) {
            (new PotionEffect(PotionEffectType.SLOW, 72000, 0, (curseStage==4), (curseStage>=5), false)).apply(cursed);
        }
    }

    public static boolean infectRandom() {
        ArrayList<UUID> potential = new ArrayList<>();
        potential.clear();
        for (UUID uuid : FrostLife.lives.keySet()) {
            if (FrostLife.lives.get(uuid) >= 2 && !frostbiteQueue.containsPlayer(uuid)) {
                potential.add(uuid);
            }
        }
        Collections.shuffle(potential);
        if (!potential.isEmpty()) {
            FrostLife.frostbitten = potential.get(0);
            return true;
        }
        else {
            FrostLife.frostbitten = null;
            return false;
        }
    }

    public static void bigSnowball(Location baseLoc) {
        Integer[][] locations = {
                {1,-2,0} , {0,-2,1} , {0,-2,0} , {0,-2,-1} , {-1,-2,0} ,
                {2,-1,0} , {1,-1,1} , {0,-1,2} , {1,-1,0} , {0,-1,1} , {1,-1,-1} , {0,-1,0} , {-1,-1,1} ,{0,-1,-1} , {-1,-1,0} ,{0,-1,-2} , {-1,-1,-1} , {-2,-1,0} ,
                {2,0,0} , {1,0,1} , {0,0,2} , {1,0,0} , {0,0,1} , {1,0,-1} , {0,0,0} , {-1,0,1} ,{0,0,-1} , {-1,0,0} ,{0,0,-2} , {-1,0,1} , {-2,0,0} , {2,0,1} , {1,0,2} , {2,0,-1} , {-1,0,2} , {-2,0,1} , {1,0,-2} , {-1,0,-2} , {-2,0,-1} ,
                {2,1,0} , {1,1,1} , {0,1,2} , {1,1,0} , {0,1,1} , {1,1,-1} , {0,1,0} , {-1,1,1} ,{0,1,-1} , {-1,1,0} ,{0,1,-2} , {-1,1,-1} , {-2,1,0} ,
                {1,2,0} , {0,2,1} , {0,2,0} , {0,2,-1} , {-1,2,0}
        };
        Location loci;
        for (int i = 0; i< locations.length; i++) {

            loci=baseLoc.clone();
            Block block = loci.add((double)locations[i][0], (double)locations[i][1], (double)locations[i][2]).getBlock();
            if (!block.getType().isSolid()) {
                block.setType(Material.POWDER_SNOW);
            }
        }
    }

    public static void strikeCurse() {
        if (FrostLife.frostbitten != null) {
            Player p = Bukkit.getPlayer(FrostLife.frostbitten);
            p.sendMessage(Messages.FROSTBITE_LOSE_LIFE.message());
            p.getWorld().strikeLightningEffect(p.getLocation());
            LifeManager.setLife(p, (Integer) FrostLife.lives.get(FrostLife.frostbitten) - 1);
            FrostbiteManager.cancelEffects(p);
            FrostLife.frostbitten=null;
        }
        FrostbiteManager.frostbiteQueue.clearQueue();
    }

    private static long generateSessionLength() {
        int randomInt = random.nextInt(20);
        long sessionLength = 0;
        if (randomInt < 2)
            sessionLength = 5;
        else if (randomInt < 5)
            sessionLength = 35;
        else if (randomInt < 9)
            sessionLength = 65;
        else if (randomInt < 14)
            sessionLength = 95;
        else
            sessionLength = 125;
        sessionLength += random.nextInt(30);
        return sessionLength*20*60;
    }
}
