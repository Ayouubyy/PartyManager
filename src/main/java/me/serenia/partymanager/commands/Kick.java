package me.serenia.partymanager.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import static me.serenia.partymanager.Utils.*;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@CommandAlias("kick")
public class Kick extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    @Description("kick a player from party")
    public static void onKick(Player p, String[] args){
        Party party = PartyListener.getParty(p);
        if (args.length != 1) {
            p.sendMessage(getString("kick-wrong-usage"));
            return;
        }
        Player ps = Bukkit.getPlayer(args[0]);
        if (!checkArguments(p, ps, party)) return;
        if (!party.hasPlayer(ps)){
            p.sendMessage(getString("player-not-in-party"));
            return;
        }
        ps.sendMessage(getString("kick-player-kick", ps.getName()));
        party.kick(ps);
        party.broadCastMessage(getString("kick-player-kicked", ps.getName(), p.getName()));
    }
    public static boolean checkArguments(Player p, Player ps, Party party){
        if (party == null){
            p.sendMessage(getString("not-in-party"));
            return false;
        }
        if (party.getPartyLeader() != p.getUniqueId()){
            p.sendMessage(getString("not-party-leader"));
            return false;
        }
        if (ps == null){
            p.sendMessage(getString("player-invalid"));
            return false;
        }
        if (ps == p){
            p.sendMessage(getString("player-is-you"));
            return false;
        }
        return true;
    }
}
