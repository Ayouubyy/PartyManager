package me.serenia.partymanager.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.gui.GuiManager;
import me.serenia.partymanager.party.Party;
import me.serenia.partymanager.party.PartyListener;
import me.serenia.partymanager.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("atlas")
public class Atlas extends BaseCommand {
    @Default
    @CommandCompletion("party")
    @Description("opens party gui")
    public static void onAtlas(Player p, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("party")) {
            GuiManager.createPartyGui(p);
            return;
        }
        p.sendMessage(Utils.getString("atlas-wrong-usage"));
    }

    @Subcommand("partychat")
    @Default
    @Description("Toggles party chat")
    public static void onChat(Player p, String[] args) {
        boolean isOn = Manager.getValue(p, "partyChat");
        Manager.setValue(p, "partyChat", !isOn);
        String msg = "&cdisabled";
        if (!isOn) msg = "&aenabled";
        p.sendMessage(Utils.getString("toggling-message", msg));
    }

    @Subcommand("kick")
    @Default
    @CommandCompletion("@players")
    @Description("kick a player from party")
    public static void onKick(Player p, String[] args) {
        Party party = PartyListener.getParty(p);
        if (args.length != 1) {
            p.sendMessage(Utils.getString("kick-wrong-usage"));
            return;
        }
        Player ps = Bukkit.getPlayer(args[0]);
        if (!checkArguments(p, ps, party)) return;
        assert ps != null;
        assert party != null;
        if (!party.hasPlayer(ps)) {
            p.sendMessage(Utils.getString("player-not-in-party"));
            return;
        }
        ps.sendMessage(Utils.getString("kick-player-kick", ps.getName()));
        party.kick(ps);
        party.broadCastMessage(Utils.getString("kick-player-kicked", ps.getName(), p.getName()));
    }

    public static boolean checkArguments(Player p, Player ps, Party party) {
        if (party == null) {
            p.sendMessage(Utils.getString("not-in-party"));
            return false;
        }
        if (party.getPartyLeader() != p.getUniqueId()) {
            p.sendMessage(Utils.getString("not-party-leader"));
            return false;
        }
        if (ps == null) {
            p.sendMessage(Utils.getString("player-invalid"));
            return false;
        }
        if (ps == p) {
            p.sendMessage(Utils.getString("player-is-you"));
            return false;
        }
        return true;
    }

    @Subcommand("leave")
    @Default
    @Description("kick a player from party")
    public static void onLeave(Player p, String[] args) {
        Party party = PartyListener.getParty(p);
        if (party == null) {
            p.sendMessage(Utils.getString("not-in-party"));
            return;
        }
        party.kick(p);
        party.broadCastMessage(Utils.getString("leave-message", p.getName()));
        p.sendMessage(Utils.getString("leave-player-message"));
    }

    @Subcommand("promote")
    @Default
    @CommandCompletion("@players")
    @Description("promote a player in a party")
    public static void onPromote(Player p, String[] args) {
        Party party = PartyListener.getParty(p);
        if (args.length != 1) {
            p.sendMessage(Utils.getString("promote-wrong-usage"));
            return;
        }
        Player ps = Bukkit.getPlayer(args[0]);
        if (!checkArguments(p, ps, party)) return;
        assert ps != null;
        assert party != null;
        if (!party.hasPlayer(ps)) {
            p.sendMessage(Utils.getString("player-not-in-party"));
            return;
        }
        party.setPartyLeader(ps.getUniqueId());
        party.broadCastMessage(Utils.getString("promote-message", ps.getName()));
    }

}

