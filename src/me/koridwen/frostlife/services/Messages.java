package me.koridwen.frostlife.services;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Messages {
    LASTLIFE_NOT_ENABLED("lastlife_not_enabled", "&cLast Life is not enabled"),
    NO_COMMAND_PERMISSION("no_command_permission", "&cYou do not have permission to use this command"),
    LIFE_RANDOMIZATION_START("life_randomization_title", "&7You will have..."),
    LIFE_RANDOMIZATION_FINAL("life_randomization_final", "%lives% &7lives."),
    LASTLIFE_WARNING_MAIN("last_life_warning_main", LifeManager.thirdLifeColor + "You are a Blue Life"),
    LASTLIFE_WARNING_SUB("last_life_warning_sub", "&7kill &c&leveryone &7who is not also a" + LifeManager.thirdLifeColor +  " Blue &7name"),
    GIVE_LIFE_COOLDOWN("give_life_cooldown", "&cPlease wait %seconds%s before giving a life again"),
    PLAYER_NOT_FOUND("give_life_player_not_found", "&cPlayer %player% was not found!"),
    GIVE_LIFE_TO_SELF("give_life_cant_give_to_self", "&cYou can't give yourself a life!"),
    GIVE_LIFE_NO_REVIVAL("give_life_cant_revive_players", "&cYou can not revive dead players!"),
    GIVE_LIFE_NO_LIVES("give_life_no_lives", "&cYou have no lives to give!"),
    GIVE_LIFE_MAX("give_life_target_max", "%player% &chas maxed out their lives already!"),
    GIVE_LIFE_SUCCESS("give_life_success", "&aYou gave one of your lives to %player%"),
    GIVE_LIFE_RECEIVED_TITLE("give_life_received_title", "&aYou received a life"),
    GIVE_LIFE_RECEIVED("give_life_received", "&aYou received a life from %player%"),
    BLACKLISTED_COMMAND("black_listed_command", "&cYou need at least one life to use that command."),
    FROSTBITE_HIT_TITLE("frostbite_hit_title",ChatColor.BLUE + "YOU GOT HIT BY THE FROSTBITE CURSE"),
    FROSTBITE_NOT_HIT_TITLE("frostbite_not_hit_title","Seems like you got spared"),
    FROSTBITE_DESCRIPTION_1("frostbite_description_1","&7You now have the " + "&3Frostbite Curse. " +"&7&oIt progresses on you, weakening you, damaging you..."),
    FROSTBITE_DESCRIPTION_2("frostbite_description_2","&7To get rid of it, pass it on to another player by throwing a snowball at them."),
    FROSTBITE_DESCRIPTION_3("frostbite_description_3",LifeManager.thirdLifeColor + "Blue lives " + "&7are already completely frozen, so they are immune to the curse. Don't waste your time on them."),
    FROSTBITE_SPREAD("frostbite_spread","&7&oThe frost spreads..."),
    FROSTBITE_DEATH("frostbite_death","%player% &fwas killed from the Frostbite Curse"),
    FROSTBITE_PVP_DEATH("frostbite_pvp_death"," &fwhile trying to ignore the Frostbite Curse"),
    FROSTBITE_LOSE_LIFE("frostbite_lose_life","&3The curse took one life from you."),
    FROSTBITE_INFECTION("frostbite_infection","&ainfection successful"),
    INFECTION_ONE_LIFE("infection_cant_receive","&c%player% is immune to the curse"),
    INFECTION_ALREADY_INFECTED("infection_already_infected","&c%player% is immune to the curse! RUN FOR YOUR LIFE!"),
    FROSTBITE_ONE_LIFE("frostbite_one_life", "&aYou became immune to the Frostbite Curse. You no longer have it.");


    private final String path;
    private final String defaultMessage;
    private static YamlConfiguration loadedMessageFile;

    private Messages(String path, String start) {
        this.path = path;
        this.defaultMessage = start;
    }

    public static void setFile(YamlConfiguration config) {
        loadedMessageFile = config;
    }

    public String message() {
        return ChatColor.translateAlternateColorCodes('&', loadedMessageFile.getString(this.getPath(), this.getDefault()));
    }

    public String getDefault() {
        return this.defaultMessage;
    }

    public String getPath() {
        return this.path;
    }
}
