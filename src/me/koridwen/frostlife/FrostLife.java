package me.koridwen.frostlife;

import me.koridwen.frostlife.commands.*;
import me.koridwen.frostlife.listeners.*;
import me.koridwen.frostlife.services.*;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class FrostLife extends JavaPlugin {
    private static FrostLife plugin;
    public static boolean enabled;
    public static boolean hasSessionRunning;
    public static int sessionsRun;
    public static Map<UUID, Integer> lives = new ConcurrentHashMap<>();
    public static Map<UUID, Boolean> explanations = new ConcurrentHashMap<>();
    public static UUID frostbitten;
    public static Scoreboard s;
    public DataManager data;
    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&f[&bFrostLife&f]&a");
    public static Integer worldBorder = 1000;
    public static Location spectatorSpawn;
    public static BukkitRunnable curseTimer;

    @Override
    public void onEnable() {
        plugin = this;
        new FrostLifeCommand();
        new GiveLifeCommand();
        new ToggleExplanationsCommand();
    }

    public void enable() {

        plugin = this;
        spectatorSpawn = (plugin.getServer().getWorlds().get(0)).getSpawnLocation();

        this.data = new DataManager(this);
        this.data.unloadDataFromFile();
        enabled = true;
        this.data.loadMessages();

        this.registerListeners();

        s = Bukkit.getScoreboardManager().getMainScoreboard();
        NameTag.registerNameTags();

        if (!this.data.getData().contains("enabled")) {
            enabled = true;
        }
        /*else if (this.data.getData().getInt("worldborder") > 0) {
            worldBorder = this.data.getData().getInt("worldborder");
        }
        else {
            this.data.getData().set("worldborder", 800);
            worldBorder = 8000;
        }*/
        this.getLogger().info("\u001b[36mLastLife \u001b[33mv" + plugin.getDescription().getVersion() + "\u001b[37m" + "\u001b[1m" + " [" + "\u001b[32m" + "Online" + "\u001b[37m" + "\u001b[1m" + "]");
        CustomCrafting.reloadCustomRecipes();
    }

    @Override
    public void onDisable() {
        enabled=false;
    }
    public void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new FrostbiteListener(), this);
        pm.registerEvents(new OnPlayerDeath(), this);
        pm.registerEvents(new OnPlayerJoin(), this);
        pm.registerEvents(new OnPlayerLeave(), this);
    }

    public void reload() {
        this.reloadConfig();
        DataManager.getInstance().loadMessages();
        NameTag.registerNameTags();
        CustomCrafting.reloadCustomRecipes();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!lives.containsKey(p.getUniqueId())) {
                lives.put(p.getUniqueId(), LifeManager.maxLives + 3);
            } else if (lives.containsKey(p.getUniqueId()) && (Integer)lives.get(p.getUniqueId()) > LifeManager.maxLives + 3) {
                LifeManager.setLife(p, LifeManager.maxLives + 3);
            }
        }

        if (enabled) {
            lives.entrySet().forEach((entry) -> {
                LifeManager.setLife(Bukkit.getPlayer((UUID)entry.getKey()), (Integer)entry.getValue());
            });
        }
        this.data.loadDataToFile();
        worldBorder = this.data.getData().getInt("worldborder");
    }

    public void reset() {
        for (String playerName : s.getTeam("firstlife").getEntries()) {
            s.getTeam("firstlife").removeEntry(playerName);
        }
        for (String playerName : s.getTeam("secondlife").getEntries()) {
            s.getTeam("secondlife").removeEntry(playerName);
        }
        for (String playerName : s.getTeam("thirdlife").getEntries()) {
            s.getTeam("thirdlife").removeEntry(playerName);
        }

        for (String playerName : s.getTeam("spectator").getEntries()) {
            s.getTeam("spectator").removeEntry(playerName);
            Player p = Bukkit.getPlayer(playerName);
            if (p != null) {
                p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().getY() + 1.0, p.getLocation().getZ()));
                p.setGameMode(GameMode.SURVIVAL);
                p.setHealth(20.0);
            }
        }
        if (Bukkit.getPlayer(frostbitten)!=null)
            FrostbiteManager.cancelEffects(Bukkit.getPlayer(frostbitten));
        frostbitten=null;
        FrostbiteManager.frostbiteQueue.clearQueue();
        Bukkit.getScheduler().cancelTasks(plugin);

        /*lives.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (enabled) {
                if (!p.hasPermission("frostlife.bypass")) {
                    LifeManager.setLife(p, 3);
                    LifeManager.setPlayerToRandomLife(p);
                }
            }
        }*/
        this.data.clear();
        //this.reload();
        worldBorder = 8000;
        World w = spectatorSpawn.getWorld();
        w.getWorldBorder().setCenter(w.getSpawnLocation());
        if (!enabled) {
            w.getWorldBorder().setSize(6.0E7);
        }
    }

    public void startSession() {
        Bukkit.broadcastMessage(prefix + " Session started!");
        hasSessionRunning = true;
        data.unloadDataFromFile();
        sessionsRun++;
        //attributing lives & explanations
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (explanations.containsKey(p.getUniqueId()))
                explanations.replace(p.getUniqueId(), true);
            else
                explanations.put(p.getUniqueId(), true);
            if (!lives.containsKey(p.getUniqueId())) {
                if (!p.hasPermission("frostlife.bypass")) {
                    LifeManager.setPlayerToRandomLife(p);
                }
            }
        }

        //curse initial start
        curseTimer = (new BukkitRunnable() {
            public void run() {
                FrostbiteManager.startFrostbiteCycle();
            }
        });
        curseTimer.runTaskLater(FrostLife.getInstance(), 18000L);

        //planning end of session
        (new BukkitRunnable() {
            public void run() {
                endSession();
            }
        }).runTaskLater(FrostLife.getInstance(), 216000L);

        //OddPolar winning message
        (new BukkitRunnable() {
            public void run() {
                Player polar = Bukkit.getPlayer("Oddpolar21");
                if (polar!=null && polar.isOnline())
                    polar.sendMessage("&6&lPolar will win!!!!!");
            }
        }).runTaskLater(FrostLife.getInstance(), 1200L);

    }

    public void endSession() {
        Bukkit.broadcastMessage(prefix + " Session ended!");
        data.loadDataToFile();
        hasSessionRunning = false;
    }

    public static FrostLife getInstance() {
        return plugin;
    }
}
