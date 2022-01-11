package me.serenia.partymanager;

import co.aikar.commands.PaperCommandManager;
import me.serenia.partymanager.commands.*;
import me.serenia.partymanager.commands.invitesystem.Invite;
import me.serenia.partymanager.gui.GuiListener;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import me.serenia.partymanager.player.Manager;
import me.serenia.partymanager.player.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public final class PartyManager extends JavaPlugin {
    public static List<Party> parties = new ArrayList<>();
    private static PartyManager instance;

    public static PartyManager instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupFiles();
        new Manager(this);
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
    }

    void setupFiles() {
        PlayerData.setup();
        PlayerData.get().options().copyDefaults(true);
        PlayerData.save();
        this.saveDefaultConfig();
    }

    void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new Invite());
        manager.registerCommand(new Atlas());
    }

    void registerListeners() {
        new Manager(this);
        new GuiListener(this);
        new PartyListener(this);
    }
}
