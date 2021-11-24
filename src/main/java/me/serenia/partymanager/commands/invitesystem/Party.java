package me.serenia.partymanager.commands.invitesystem;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import me.serenia.partymanager.Utils;
import org.bukkit.entity.Player;

import static me.serenia.partymanager.commands.invitesystem.Invite.invites;

@CommandAlias("party|p")
public class Party extends BaseCommand {
    @Description("invites a player")
    @CommandCompletion("accept|deny")
    @Default
    public static void onParty(Player p, String[] args){
        if (args.length == 1){
            PartyInvite invite = getPartyInvite(p);
            if (invite == null){
                p.sendMessage(Utils.getString("invalid-request"));
                return;
            }
            if (args[0].equalsIgnoreCase("accept")){
                invite.accept();
            } else if (args[0].equalsIgnoreCase("deny")){
                invite.deny();
            }
            return;
        }
        p.sendMessage(Utils.getString("party-wrong-usage"));
    }

    public static PartyInvite getPartyInvite(Player p){
        for (PartyInvite partyI: invites){
            if (partyI.getInvited() == p.getUniqueId()) return partyI;
        }
        return null;
    }
}
