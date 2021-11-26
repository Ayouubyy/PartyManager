package me.serenia.partymanager.gui;

import me.serenia.partymanager.PartyManager;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import me.serenia.partymanager.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.serenia.partymanager.gui.GuiManager.*;
import static me.serenia.partymanager.Utils.*;
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
    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        int i = e.getSlot();
        Party party = PartyListener.getParty(p);
        if (p.hasMetadata(partyMD)){
            e.setCancelled(true);
            if (party != null){
                if (s(i, 11,12,20,21)){
                    createLootSharingGui(p);
                } else if (s(i, 13,14,22,23)){
                    toggle(p, "partyGlow", "party glowing");
                    createPartyGui(p);
                }  else if (s(i, 15,16,24,25)){
                    p.closeInventory();
                    p.performCommand("leave");
                }
            } else {
                if (s(i, 21,22,30,31)){
                    createLootSharingGui(p);
                } else if (s(i, 23,24,32,33)){
                    toggle(p, "partyGlow", "party glowing");
                    createPartyGui(p);
                }
            }
        } else if (p.hasMetadata(lootSharingMD)){
            e.setCancelled(true);
            if (party != null){
                if (party.getPartyLeader() != p.getUniqueId()){
                    p.sendMessage(getString("not-party-leader"));
                    return;
                }
                if (s(i, 20,21,29,30)){
                    party.toggleLootSharingOption("roundRobin", "round robin");
                    createLootSharingGui(p);
                } else if (s(i, 22,23,31,32)){
                    party.toggleLootSharingOption("ffa", "free for all");
                    createLootSharingGui(p);
                }  else if (s(i, 24,25,33,34)){
                    party.toggleLootSharingOption( "lastHit", "last hit");
                    createLootSharingGui(p);
                }
            } else {
                if (s(i, 20,21,29,30)){
                    toggle(p, "roundRobin", "round robin");
                    createLootSharingGui(p);
                } else if (s(i, 22,23,31,32)){
                    toggle(p, "ffa", "free for all");
                    createLootSharingGui(p);
                }  else if (s(i, 24,25,33,34)){
                    toggle(p, "lastHit", "last hit");
                    createLootSharingGui(p);
                }
            }
        }
    }


    boolean s(int i, int... f){
        for (int s: f){
            if (i == s) return true;
        }
        return false;
    }
    void toggle(Player p, String path, String displayname){
        boolean b = !Manager.getValue(p, path);
        Manager.setValue(p, path, b);
        String v = "&coff";
        if (b) v = "&aon";
        p.sendMessage(Utils.getToggleString(v,displayname));
    }
}
