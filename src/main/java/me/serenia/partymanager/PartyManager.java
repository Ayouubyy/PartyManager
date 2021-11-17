package me.serenia.partymanager;

import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.player.Manager;
import me.serenia.partymanager.player.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public final class PartyManager extends JavaPlugin {

    @Override
    public void onEnable() {
        setupFiles();
        new Manager(this);
    }

    @Override
    public void onDisable() {
    }

    public static List<Party> parties = new ArrayList<>();
    void setupFiles(){
        PlayerData.setup();
        PlayerData.get().options().copyDefaults(true);
        PlayerData.save();
        this.saveDefaultConfig();
    }
}
