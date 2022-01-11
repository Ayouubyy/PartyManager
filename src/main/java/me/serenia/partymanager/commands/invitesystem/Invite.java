package me.serenia.partymanager.commands.invitesystem;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("invite")
public class Invite extends BaseCommand {
    static List<PartyInvite> invites = new ArrayList<>();

    @Description("invites a player")
    @CommandCompletion("@players")
    @Default
    public static void onInvite(Player p, String[] args) {
        if (args.length == 1) {
            Player ps = Bukkit.getPlayer(args[0]);
            if (ps != null) {
                Party party = PartyListener.getParty(p);
                if (getPartyInvite(ps) != null) {
                    p.sendMessage(Utils.getString("invite-error", ps.getName()));
                    return;
                }
                if (PartyListener.getParty(ps) != null) {
                    p.sendMessage(Utils.getString("invite-already-in-party", ps.getName()));
                    return;
                }
                if (ps == p) {
                    p.sendMessage(Utils.getString("player-is-you"));
                    return;
                }
                if (party != null) {
                    if (party.size() >= 5) {
                        p.sendMessage(Utils.getString("invite-party-full"));
                        return;
                    }
                    if (party.getPartyLeader() != p.getUniqueId()) {
                        p.sendMessage(Utils.getString("not-party-leader"));
                        return;
                    }
                }

                TextComponent accept = create(Utils.getString("accept"), "/party accept", "&aClick to accept!");
                TextComponent deny = create(Utils.getString("deny"), "/party deny", "&cClick to deny!");
                p.sendMessage(Utils.getString("invite-send", ps.getName()));
                ps.sendMessage(Utils.getString("invite-receive", p.getName()));
                ps.sendMessage(accept.append(deny));
                new PartyInvite(ps.getUniqueId(), p.getUniqueId());
                return;
            }
        }
        p.sendMessage(Utils.getString("invite-wrong-usage"));
    }

    @Subcommand("party")
    @Description("invites a player")
    @CommandCompletion("accept|deny")
    @Default
    public static void onParty(Player p, String[] args) {
        if (args.length == 1) {
            PartyInvite invite = getPartyInvite(p);
            if (invite == null) {
                p.sendMessage(Utils.getString("invalid-request"));
                return;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                invite.accept();
            } else if (args[0].equalsIgnoreCase("deny")) {
                invite.deny();
            }
            return;
        }
        p.sendMessage(Utils.getString("party-wrong-usage"));
    }

    public static PartyInvite getPartyInvite(Player p) {
        for (PartyInvite partyI : invites) {
            if (partyI.getInvited() == p.getUniqueId()) return partyI;
        }
        return null;
    }

    static net.kyori.adventure.text.TextComponent create(String s, String cmd, String text) {
        return Component.text(Utils.chat(s))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
                .hoverEvent(HoverEvent.showText(Component.text(Utils.chat(text))));
    }

}
