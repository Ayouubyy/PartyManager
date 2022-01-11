package me.serenia.partymanager.party;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.serenia.partymanager.PartyManager;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.player.Manager;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.serenia.partymanager.PartyManager.parties;
import static me.serenia.partymanager.Utils.getString;

public class PartyListener implements Listener {
    private PartyManager plugin;

    public PartyListener(PartyManager plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static Party getParty(Player p) {
        for (Party party : parties) {
            if (party.hasPlayer(p)) return party;
        }
        return null;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Party party = getParty(p);
        if (party == null) return;
        party.kick(p);
        party.broadCastMessage(getString("leave-message", p.getName()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        Party party = getParty(p);
        if (party == null) return;
        if (!Manager.getValue(p, "partyChat")) return;
        e.setCancelled(true);
        party.broadCastMessage(Utils.getString("chat-format", p.getName()) + PlainTextComponentSerializer.plainText().serialize(e.message()));
    }
}
