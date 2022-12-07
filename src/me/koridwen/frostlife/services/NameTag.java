package me.koridwen.frostlife.services;

import me.koridwen.frostlife.FrostLife;
import org.bukkit.scoreboard.Team;

public class NameTag {
    public NameTag() {
    }

    public static void registerNameTags() {
        unregisterNameTags();
        Team extralife = FrostLife.s.registerNewTeam("extralife");
        extralife.setCanSeeFriendlyInvisibles(false);
        extralife.setColor(LifeManager.extraLivesColor);
        Team first = FrostLife.s.registerNewTeam("firstlife");
        first.setCanSeeFriendlyInvisibles(false);
        first.setColor(LifeManager.firstLifeColor);
        Team second = FrostLife.s.registerNewTeam("secondlife");
        second.setCanSeeFriendlyInvisibles(false);
        second.setColor(LifeManager.secondLifeColor);
        Team third = FrostLife.s.registerNewTeam("thirdlife");
        third.setCanSeeFriendlyInvisibles(false);
        third.setColor(LifeManager.thirdLifeColor);
        Team spectator = FrostLife.s.registerNewTeam("spectator");
        spectator.setCanSeeFriendlyInvisibles(false);
        spectator.setColor(LifeManager.spectatorColor);
    }

    public static void unregisterNameTags() {
        if (FrostLife.s.getTeam("extralife") != null) {
            FrostLife.s.getTeam("extralife").unregister();
        }

        if (FrostLife.s.getTeam("firstlife") != null) {
            FrostLife.s.getTeam("firstlife").unregister();
        }

        if (FrostLife.s.getTeam("secondlife") != null) {
            FrostLife.s.getTeam("secondlife").unregister();
        }

        if (FrostLife.s.getTeam("thirdlife") != null) {
            FrostLife.s.getTeam("thirdlife").unregister();
        }

        if (FrostLife.s.getTeam("spectator") != null) {
            FrostLife.s.getTeam("spectator").unregister();
        }

    }
}