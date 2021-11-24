package me.serenia.partymanager.gui;

import me.serenia.partymanager.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.serenia.partymanager.gui.GuiManager.*;
public class GuiListener implements Listener {
    private PartyManager plugin;
    public GuiListener(PartyManager plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        clearData(e.getPlayer());
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        clearData((Player) e.getPlayer());
    }
    void clearData(Player p){
        p.removeMetadata(partyMD, plugin);
        p.removeMetadata(lootSharingMD, plugin);
    }
}
