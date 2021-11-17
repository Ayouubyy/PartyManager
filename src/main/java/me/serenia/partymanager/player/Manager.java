package me.serenia.partymanager.player;

import me.serenia.partymanager.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Manager implements Listener {
    private PartyManager plugin;
    public Manager(PartyManager plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        setupPlayer(p);
    }

    static void setupPlayer(Player p){
        FileConfiguration config = PlayerData.get();
        if (config.contains("players." + p.getUniqueId())) return;
        config.set("players." + p.getUniqueId() + ".roundRobin" , "false");
        config.set("players." + p.getUniqueId() + ".lastHit" , "false");
        config.set("players." + p.getUniqueId() + ".ffa" , "false");
        PlayerData.save();
    }
    void setValue(Player p, String path, boolean value){
        FileConfiguration config = PlayerData.get();
        config.set("players." + p.getUniqueId() + "." + path , value);
        PlayerData.save();
    }
}