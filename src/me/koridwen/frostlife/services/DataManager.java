package me.koridwen.frostlife.services;

import me.koridwen.frostlife.FrostLife;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataManager {
    private static DataManager instance;
    private final FrostLife plugin;
    private FileConfiguration dataConfig = null;
    private File dataFile = null;

    public DataManager() {
        this.plugin = FrostLife.getInstance();
    }

    public static DataManager getInstance() {
        return instance != null ? instance : (instance = new DataManager());
    }

    public DataManager(FrostLife plugin) {
        this.plugin = plugin;
        this.saveDefaultData();
    }

    public void reloadData() {
        if (this.dataFile == null) {
            this.dataFile = new File(this.plugin.getDataFolder(), "data.yml");
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
        InputStream defaultStream = this.plugin.getResource("data.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }

    }

    public FileConfiguration getData() {
        if (this.dataConfig == null) {
            this.reloadData();
        }

        return this.dataConfig;
    }

    public void saveData() {
        if (this.dataConfig != null && this.dataFile != null) {
            try {
                this.getData().save(this.dataFile);
            } catch (IOException var2) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not save config " + this.dataFile, var2);
            }

        }
    }

    public void saveDefaultData() {
        if (this.dataFile == null) {
            this.dataFile = new File(this.plugin.getDataFolder(), "data.yml");
        }

        if (!this.dataFile.exists()) {
            this.plugin.saveResource("data.yml", false);
        }

    }

    public void loadDataToFile() {
        this.clear();
        this.getData().set("enabled", FrostLife.enabled);
        this.getData().set("sessionsrun", FrostLife.sessionsRun);
        if (FrostLife.worldBorder > 0) {
            this.getData().set("worldborder", FrostLife.worldBorder);
        }
        if (FrostLife.spectatorSpawn != null) {
            this.getData().set("spectatorspawn", FrostLife.spectatorSpawn.getBlockX() + "/" + FrostLife.spectatorSpawn.getBlockY() + "/" + FrostLife.spectatorSpawn.getBlockZ() + "/" + FrostLife.spectatorSpawn.getWorld().getName());
        }

        if (FrostLife.lives.size() > 0) {
            for (Map.Entry<UUID,Integer> entry : FrostLife.lives.entrySet()) {
                if (entry.getValue() != null) {
                    this.getData().set("lives." + (entry.getKey()).toString(), ((Integer)entry.getValue()).toString());
                }
            }
            for (Map.Entry<UUID,Boolean> entry : FrostLife.explanations.entrySet()) {
                if (entry.getValue() != null) {
                    this.getData().set("explanations." + (entry.getKey()).toString(), entry.getValue());
                }
            }
        }

        this.saveData();
    }

    public void unloadDataFromFile() {
        FrostLife.enabled = this.getData().getBoolean("enabled");
        FrostLife.sessionsRun = this.getData().getInt("sessionsrun");

        if (this.getData().getList("spectator") != null) {
            for (String s : this.getData().getStringList("spectator")) {
                if (!FrostLife.lives.containsKey(UUID.fromString(s))) {
                    this.getData().set("lives." + s, "0");
                }
            }
            this.getData().set("spectator", (Object)null);
        }

        if (this.getData().get("lives.") != null) {
            this.getData().getConfigurationSection("lives").getKeys(false).forEach((key) -> {
                int value = Integer.parseInt(this.getData().getString("lives." + key));
                UUID keyuuid = UUID.fromString(key);
                if (!FrostLife.lives.containsKey(keyuuid)) {
                    if (value <= LifeManager.maxLives + 3) {
                        FrostLife.lives.put(keyuuid, value);
                    } else if (value > LifeManager.maxLives + 3) {
                        FrostLife.lives.put(keyuuid, LifeManager.maxLives + 3);
                    }
                }

            });
        }
        if (this.getData().get("explanations.") != null) {
            this.getData().getConfigurationSection("explanations").getKeys(false).forEach((key) -> {
                FrostLife.explanations.put(UUID.fromString(key), this.getData().getBoolean("lives." + key));
            });
        }

        FrostLife.worldBorder = this.getData().getInt("worldborder");
        if (this.getData().getString("spectatorspawn") != null) {
            String[] parts = this.getData().getString("spectatorspawn").split("/");
            World w = Bukkit.getServer().getWorld(parts[3]) == null ? Bukkit.getServer().getWorlds().get(0) : Bukkit.getServer().getWorld(parts[3]);
            FrostLife.spectatorSpawn = new Location(w, (double)Integer.parseInt(parts[0]), (double)Integer.parseInt(parts[1]), (double)Integer.parseInt(parts[2]));
        }

        this.saveData();
    }

    public void clear() {
        this.getData().set("enabled", false);
        this.getData().set("lives", (Object)null);
        this.getData().set("explanations", (Object)null);
        this.getData().set("worldborder", 1000);
        this.getData().set("finaldeathspawn", (Object)null);
        this.saveData();
    }

    public void loadMessages() {
        File lang = new File(FrostLife.getInstance().getDataFolder(), "messages.yml");
        if (!lang.exists()) {
            try {
                FrostLife.getInstance().getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = FrostLife.getInstance().getResource("messages.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
                    defConfig.save(lang);
                    Messages.setFile(defConfig);
                }
            } catch (IOException var8) {
                var8.printStackTrace();
                Bukkit.getLogger().severe("Failed to create messages.yml file! This is a fatal error! Now disabling!");
                Bukkit.getPluginManager().disablePlugin(FrostLife.getInstance());
            }
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        Messages[] var10 = Messages.values();
        int var4 = var10.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Messages item = var10[var5];
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }

        Messages.setFile(conf);
        conf.options().setHeader(Arrays.asList("Use & for color codes.", "%player% is where the player name will get inserted."));

        try {
            conf.save(lang);
        } catch (IOException var7) {
            Bukkit.getLogger().severe("Failed to save messages file!");
            var7.printStackTrace();
        }

    }
}