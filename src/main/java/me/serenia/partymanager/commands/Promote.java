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


@CommandAlias("promote")
public class Promote extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    @Description("promote a player in a party")
    public static void onPromote(Player p, String[] args){
        Party party = PartyListener.getParty(p);
        if (args.length != 1){
            p.sendMessage(getString("promote-wrong-usage"));
            return;
        }
        if (party == null){
            p.sendMessage(getString("not-in-party"));
            return;
        }
        if (party.getPartyLeader() != p.getUniqueId()){
            p.sendMessage(getString("not-party-leader"));
            return;
        }
        Player ps = Bukkit.getPlayer(args[0]);
        if (ps == null || (!party.hasPlayer(ps))){
            p.sendMessage(getString("kick-player-not-in-party"));
            return;
        }
        party.setPartyLeader(ps.getUniqueId());
        party.broadCastMessage(getString("promote-message", ps.getName()));
    }
}
