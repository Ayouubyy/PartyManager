package me.serenia.partymanager.commands.invitesystem;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.serenia.partymanager.Utils.*;
import static me.serenia.partymanager.commands.invitesystem.PartyCommand.*;

@CommandAlias("invite")
public class Invite extends BaseCommand {
    static List<PartyInvite> invites = new ArrayList<>();
    @Description("invites a player")
    @CommandCompletion("@players")
    @Default
    public static void onInvite(Player p, String[] args){
        if (args.length == 1 ){
            Player ps = Bukkit.getPlayer(args[0]);
            if (ps != null){
                Party party = PartyListener.getParty(p);
                if (getPartyInvite(ps) != null){
                    p.sendMessage(getString("invite-error", ps.getName()));
                    return;
                }
                if (PartyListener.getParty(ps) != null){
                    p.sendMessage(getString("invite-already-in-party", ps.getName()));
                    return;
                }
                if (ps == p){
                    p.sendMessage(getString("player-is-you"));
                    return;
                }
                if (party != null){
                    if (party.size() >= 5){
                        p.sendMessage(getString("invite-party-full"));
                        return;
                    }
                    if (party.getPartyLeader() != p.getUniqueId()){
                        p.sendMessage(getString("not-party-leader"));
                        return ;
                    }
                }

                TextComponent accept = create(getString("accept"), "/party accept", "&aClick to accept!");
                TextComponent deny = create(getString("deny"), "/party deny", "&cClick to deny!");
                p.sendMessage(getString("invite-send", ps.getName()));
                ps.sendMessage(getString("invite-receive", p.getName()));
                ps.sendMessage(accept.append(deny));
                new PartyInvite(ps.getUniqueId(), p.getUniqueId());
                return;
            }
        }
        p.sendMessage(getString("invite-wrong-usage"));
    }
    static net.kyori.adventure.text.TextComponent create(String s, String cmd, String text){
        return Component.text(Utils.chat(s))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
                .hoverEvent(HoverEvent.showText(Component.text(chat(text))));
    }

}
