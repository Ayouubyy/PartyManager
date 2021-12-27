package me.serenia.partymanager.gui;

import me.serenia.partymanager.PartyManager;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import me.serenia.partymanager.player.Manager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static me.serenia.partymanager.Utils.*;

public class GuiManager {
    static String partyMD = "partyMD";
    static String lootSharingMD = "lootSharingMD";
    public static void createPartyGui(Player p){
        Party party = PartyListener.getParty(p);
        Inventory inv = Bukkit.createInventory(null, 54);
        ItemStack lootSharing = createIStack(Material.BLAZE_ROD, "&6Loot Sharing", "&e&lCLICK &fto open loot sharing menu");
        ItemStack partyGlowing = createIStack(Material.BLAZE_ROD, "&bParty Glow", getStr(Manager.getValue(p, "partyGlow")), "&e&lCLICK &fto toggle");
        ItemStack partyLeave = createIStack(Material.BLAZE_ROD, "&6Leave party", "&e&lCLICK &fto leave the party");
        if (party == null){
            f(inv, lootSharing, 21, 22, 30, 31);
            f(inv, partyGlowing, 23, 24, 32, 33);
        } else {
            f(inv, lootSharing, 11, 12, 20, 21);
            f(inv, partyGlowing, 13, 14, 22, 23);
            f(inv, partyLeave, 15, 16, 24, 25);
            int i = 39;
            for(Player ps : party.getPlayers()){
                inv.setItem(i, getHead(ps));
                i++;
            }
        }
        new BukkitRunnable(){@Override public void run() { p.setMetadata(partyMD, new FixedMetadataValue(PartyManager.instance(), false));}}.runTaskLater(PartyManager.instance(), 3);
        p.openInventory(inv);
    }
    public static void createLootSharingGui(Player p){
        Party party = PartyListener.getParty(p);
        Inventory inv = Bukkit.createInventory(null, 54);
        ItemStack roundRobin = createIStack(Material.BLAZE_ROD, "&6Round Robin", "&e&lCLICK &fto toggle");
        ItemStack ffa = createIStack(Material.BLAZE_ROD, "&bFree for all", "&e&lCLICK &fto toggle");
        ItemStack lastHit = createIStack(Material.BLAZE_ROD, "&6Last hit", "&e&lCLICK &fto toggle");
        if (party == null){
            addLore(roundRobin,getStr(Manager.getValue(p, "roundRobin")));
            addLore(ffa,getStr(Manager.getValue(p, "ffa")));
            addLore(lastHit,getStr(Manager.getValue(p, "lastHit")));
        } else {
            addLore(roundRobin,getStr(party.isRoundRobin()));
            addLore(ffa,getStr(party.isFfa()));
            addLore(lastHit,getStr(party.isLastHit()));
        }
        f(inv, roundRobin, 20, 21, 29, 30);
        f(inv, ffa, 22, 23, 31, 32);
        f(inv, lastHit, 24, 25, 33, 34);
        new BukkitRunnable(){@Override public void run() { p.setMetadata(lootSharingMD, new FixedMetadataValue(PartyManager.instance(), false));}}.runTaskLater(PartyManager.instance(), 3);
        p.openInventory(inv);
    }
    static void f(Inventory v, ItemStack t, int... i){
        for (int s : i) v.setItem(s, t);
    }
   public static ItemStack createIStack(Material mat, String title, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.displayName(Component.text(Utils.chat(title)));

        List<Component> lor = new ArrayList<>();
        for (String s : lore) {
            lor.add(Component.text(Utils.chat(s)));
        }
        meta.lore(lor);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
    static String getStr(Boolean b){
        String s;
        if (b) s = Utils.chat("&a&lENABLED");else s =  Utils.chat("&4&lDISABLED");
        return s;
    }
    static ItemStack getHead(Player p){
        ItemStack item = createIStack(Material.PLAYER_HEAD, "&a&l" + p.getName(), "&e&lCLICK &fto show options");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setPlayerProfile(p.getPlayerProfile());
        item.setItemMeta(meta);
        return item;
    }
}
