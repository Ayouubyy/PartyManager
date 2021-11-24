package me.serenia.partymanager.party;

import me.serenia.partymanager.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.serenia.partymanager.PartyManager.parties;

public class PartyListener implements Listener {
    private PartyManager plugin;
    public PartyListener(PartyManager plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Party party = getParty(p);
        if (party == null) return;
        party.kick(p);
    }
    public static Party getParty(Player p){
        for (Party party : parties){
            if (party.hasPlayer(p)) return party;
        }
        return null;
    }
}
