package me.serenia.partymanager.gui;

import me.serenia.partymanager.PartyManager;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import me.serenia.partymanager.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.serenia.partymanager.Utils.getString;
import static me.serenia.partymanager.gui.GuiManager.*;

public class GuiListener implements Listener {
    private final PartyManager plugin;

    public GuiListener(PartyManager plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        clearData(e.getPlayer());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        clearData((Player) e.getPlayer());
    }

    void clearData(Player p) {
        p.removeMetadata(partyMD, plugin);
        p.removeMetadata(lootSharingMD, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int i = e.getSlot();
        Party party = PartyListener.getParty(p);
        Inventory inv = e.getClickedInventory();
        if (inv == p.getInventory()) return;
        if (p.hasMetadata(partyMD)) {
            e.setCancelled(true);
            if (party != null) {
                if (s(i, 11, 12, 20, 21)) {
                    createLootSharingGui(p);
                } else if (s(i, 13, 14, 22, 23)) {
                    toggle(p, "partyGlow", "party glowing");
                    party.glowMembers();
                    createPartyGui(p);
                } else if (s(i, 15, 16, 24, 25)) {
                    p.closeInventory();
                    p.performCommand("leave");
                } else if (s(i, 40, 41, 42, 43)) {
                    int j = i - 39;
                    if (party.size() >= j + 1) {
                        Player ps = Bukkit.getPlayer(party.getMembers().get(j));
                        if (p.getUniqueId() == party.getPartyLeader()) showOptions(ps, p, inv);
                    }
                } else if (s(i, 30, 48)) {
                    ItemStack item = e.getCurrentItem();
                    if (item == null) return;
                    p.performCommand(getCommand(item));
                }
            } else {
                if (s(i, 21, 22, 30, 31)) {
                    createLootSharingGui(p);
                } else if (s(i, 23, 24, 32, 33)) {
                    toggle(p, "partyGlow", "party glowing");
                    createPartyGui(p);
                }
            }
        } else if (p.hasMetadata(lootSharingMD)) {
            e.setCancelled(true);
            if (party != null) {
                if (party.getPartyLeader() != p.getUniqueId()) {
                    p.sendMessage(getString("not-party-leader"));
                    return;
                }
                if (s(i, 20, 21, 29, 30)) {
                    party.toggleLootSharingOption("roundRobin", "round robin");
                    createLootSharingGui(p);
                } else if (s(i, 22, 23, 31, 32)) {
                    party.toggleLootSharingOption("ffa", "free for all");
                    createLootSharingGui(p);
                } else if (s(i, 24, 25, 33, 34)) {
                    party.toggleLootSharingOption("lastHit", "last hit");
                    createLootSharingGui(p);
                }
            } else {
                if (s(i, 20, 21, 29, 30)) {
                    toggle(p, "roundRobin", "round robin");
                    createLootSharingGui(p);
                } else if (s(i, 22, 23, 31, 32)) {
                    toggle(p, "ffa", "free for all");
                    createLootSharingGui(p);
                } else if (s(i, 24, 25, 33, 34)) {
                    toggle(p, "lastHit", "last hit");
                    createLootSharingGui(p);
                }
            }
        }
    }


    boolean s(int i, int... f) {
        for (int s : f) {
            if (i == s) return true;
        }
        return false;
    }

    void toggle(Player p, String path, String displayname) {
        boolean b = !Manager.getValue(p, path);
        Manager.setValue(p, path, b);
        String v = "&coff";
        if (b) v = "&aon";
        p.sendMessage(Utils.getToggleString(v, displayname));
    }

    void showOptions(Player p, Player invholder, Inventory inv) {
        ItemStack promote = createIStack(Material.GREEN_TERRACOTTA, "&aPromote " + p.getName(), "&e&lCLICK &7to promote the player");
        ItemStack kick = createIStack(Material.RED_TERRACOTTA, "&cKick " + p.getName(), "&e&lCLICK &7to kick the player");
        String cmd = "partymanager:promote " + p.getName();
        putCommand(promote, cmd);
        putCommand(kick, "partymanager:kick " + p.getName());
        inv.setItem(30, promote);
        inv.setItem(48, kick);
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack item1 = inv.getItem(30);
                if (item1 == null) return;
                if (getCommand(item1).equals(cmd)) {
                    ItemStack air = new ItemStack(Material.AIR);
                    inv.setItem(30, air);
                    inv.setItem(48, air.clone());
                }
            }
        }.runTaskLater(plugin, 100);
    }

    void putCommand(ItemStack item, String cmd) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "command"), PersistentDataType.STRING, cmd);
        item.setItemMeta(meta);
    }

    String getCommand(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return "null";
        if (!meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "command"), PersistentDataType.STRING))
            return "null";
        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "command"), PersistentDataType.STRING);
    }
}
