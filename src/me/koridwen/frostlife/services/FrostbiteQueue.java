package me.koridwen.frostlife.services;

import java.util.ArrayList;
import java.util.UUID;

import me.koridwen.frostlife.FrostLife;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static java.lang.Math.max;

public class FrostbiteQueue {
    private ArrayList<UUID> pastFrostbittens;
    int maxPlayers = 5;

    public FrostbiteQueue() {
        this.pastFrostbittens = new ArrayList<>();
    }

    public void addPlayer(UUID player) {
        if (pastFrostbittens.size()>=maxPlayers) {
            pastFrostbittens.remove(0);
        }
        pastFrostbittens.add(player);
    }

    public boolean containsPlayer(UUID uuid) {
        return pastFrostbittens.contains(uuid);
    }

    public void updateQueue() {
        for (UUID uuid : pastFrostbittens)
            if (FrostLife.lives.get(uuid)<=1)
                pastFrostbittens.remove(uuid);
        int maxPlayers=0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Integer lives = FrostLife.lives.get(p.getUniqueId());
            if (lives!=null && lives>=2)
                maxPlayers++;
        }
        if (maxPlayers%2==0)
            this.maxPlayers=max(maxPlayers/2,1);
        else
            this.maxPlayers=max(maxPlayers/2+1,1);
        //this.maxPlayers=2;
    }

    public void clearQueue() {
        pastFrostbittens.clear();
    }

    public String toString() {
        String content = "";
        for (UUID uuid : pastFrostbittens)
            content+= Bukkit.getPlayer(uuid).getName() +", ";
        return content;
    }

}
