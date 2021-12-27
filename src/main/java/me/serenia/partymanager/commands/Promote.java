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
import static me.serenia.partymanager.commands.Kick.checkArguments;


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
        Player ps = Bukkit.getPlayer(args[0]);
        if (!checkArguments(p, ps, party)) return;
        if (!party.hasPlayer(ps)){
            p.sendMessage(getString("player-not-in-party"));
            return;
        }
        party.setPartyLeader(ps.getUniqueId());
        party.broadCastMessage(getString("promote-message", ps.getName()));
    }
}
