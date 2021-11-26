package me.serenia.partymanager.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.serenia.partymanager.Utils.getString;

@CommandAlias("leave")
public class Leave extends BaseCommand {
    @Default
    @Description("kick a player from party")
    public static void onLeave(Player p, String[] args){
        Party party = PartyListener.getParty(p);
        if (party == null){
            p.sendMessage(getString("not-in-party"));
            return;
        }
        party.kick(p);
        party.broadCastMessage(getString("leave-message", p.getName()));
        p.sendMessage(getString("leave-player-message"));
    }
}
