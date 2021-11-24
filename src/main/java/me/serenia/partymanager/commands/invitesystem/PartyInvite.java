package me.serenia.partymanager.commands.invitesystem;


import lombok.Data;
import me.serenia.partymanager.PartyManager;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import me.serenia.partymanager.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.serenia.partymanager.Utils.getString;
import static me.serenia.partymanager.commands.invitesystem.Invite.invites;
import java.util.UUID;

@Data
public class PartyInvite {
    private PartyInvite invite;
    UUID invited, inviter;
    public PartyInvite(UUID invited, UUID inviter){
        this.invited = invited;
        this.inviter = inviter;
        int[] tl = {600};
        invites.add(this);
        new BukkitRunnable(){
            @Override
            public void run() {
                tl[0] = tl[0] - 1;
                if (tl[0] <= 0){
                    delete();
                    cancel();
                }
            }
        }.runTaskTimer(PartyManager.instance(), 0, 1);

    }
    public void delete() { invites.remove(this); }
    public void accept(){
        Player p = Bukkit.getPlayer(inviter);
        Player ps = Bukkit.getPlayer(invited);
        if (ps == null) return;
        if (p == null){
            ps.sendMessage(getString("invalid-error"));
            return;
        }
        Party party = PartyListener.getParty(p);
        if (party == null){
            Party pf = new Party(inviter, invited);
            pf.broadCastMessage(getString("join-message-bc", p.getName()));
            pf.broadCastMessage(getString("join-message-bc", ps.getName()));

        } else {
            if (party.size() >= 5){
                ps.sendMessage(getString("invite-party-full"));
                return;
            }
            party.add(ps);
        }
        delete();
    }
    public void deny(){
        Player ps = Bukkit.getPlayer(invited);
        ps.sendMessage(Utils.getString("deny-message"));
        delete();
    }
}
