package me.serenia.partymanager;


import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String getString(String s) {
        return chat(PartyManager.instance().getConfig().getString(s));
    }

    public static String getString(String s, String value, String value1) {
        return chat(PartyManager.instance().getConfig().getString(s).replace("<value>", value).replace("<value1>", value1));
    }

    public static String getString(String s, String value) {
        return chat((PartyManager.instance().getConfig().getString(s).replace("<value>", value)));
    }

    public static String getToggleString(String value, String value1) {
        return chat((PartyManager.instance().getConfig().getString("toggle-message").replace("<value>", value).replace("<path>", value1)));
    }

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void addLore(ItemStack item, String s) {
        List<Component> list = new ArrayList<>();
        if (item.getItemMeta().hasLore()) list.addAll(item.getItemMeta().lore());
        list.add(net.kyori.adventure.text.Component.text(Utils.chat(s)));
        ItemMeta meta = item.getItemMeta();
        meta.lore(list);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

}
