package me.serenia.partymanager.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;


import static me.serenia.partymanager.Utils.getString;
import static me.serenia.partymanager.gui.GuiManager.*;
@CommandAlias("atlas")
public class Atlas extends BaseCommand {
    @Default
    @CommandCompletion("party")
    @Description("opens party gui")
    public static void onAtlas(Player p, String[] args){
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("party")) {
                createPartyGui(p);
                return;
            }
        }
        p.sendMessage(getString("atlas-wrong-usage"));
    }

}

