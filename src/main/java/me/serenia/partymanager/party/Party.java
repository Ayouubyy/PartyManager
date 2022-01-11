package me.serenia.partymanager.party;


import lombok.Data;
import me.serenia.partymanager.PartyManager;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.gui.GuiManager;
import me.serenia.partymanager.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.glow.GlowAPI;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

import static me.serenia.partymanager.PartyManager.parties;
import static me.serenia.partymanager.Utils.getString;

@Data
public class Party {
    UUID partyLeader;
    LinkedList<UUID> members;
    boolean roundRobin;
    boolean ffa;
    boolean lastHit;

    public Party(UUID partyLeader, UUID... mms) {
        this.partyLeader = partyLeader;
        LinkedList<UUID> members = new LinkedList<>();
        members.add(partyLeader);
        Collections.addAll(members, mms);
        this.members = members;
        Player p = Bukkit.getPlayer(partyLeader);
        this.roundRobin = Manager.getValue(p, "roundRobin");
        this.ffa = Manager.getValue(p, "ffa");
        this.lastHit = Manager.getValue(p, "lastHit");
        parties.add(this);
        glowMembers();
    }

    public int size() {
        return members.size();
    }

    public void kick(Player p) {
        members.remove(p.getUniqueId());
        cleanUp();
        if (p.hasMetadata("partyMD")) p.closeInventory();
        reloadGUI();
        for (Player ps : getPlayers()) {
            if (GlowAPI.isGlowing(p, ps)) GlowAPI.setGlowing(p, false, ps);
            if (GlowAPI.isGlowing(ps, p)) GlowAPI.setGlowing(ps, false, p);
        }
    }

    public void reloadGUI() {
        for (Player ps : getPlayers()) if (ps.hasMetadata("partyMD")) GuiManager.createPartyGui(ps);
    }

    public void add(Player p) {
        members.add(p.getUniqueId());
        broadCastMessage(getString("join-message-bc", p.getName()));
        glowMembers();
        reloadGUI();
    }

    public void cleanUp() {
        members.removeIf(uuid -> (Bukkit.getPlayer(uuid) == null));
        if (size() < 2) {
            if (!parties.contains(this)) return;
            parties.remove(this);
            broadCastMessage(getString("leave-disbanded"));
        } else {
            if (partyLeader != members.get(0)) {
                partyLeader = members.get(0);
                broadCastMessage(getString("promote-message", Bukkit.getPlayer(partyLeader).getName()));
            }
        }
    }

    public void broadCastMessage(String s) {
        for (Player p : getPlayers()) {
            p.sendMessage(Utils.chat(s));
        }
    }

    public void setPartyLeader(UUID partyLeader) {
        this.partyLeader = partyLeader;
        Collections.swap(members, 0, members.indexOf(partyLeader));
        reloadGUI();
    }

    public LinkedList<Player> getPlayers() {
        LinkedList<Player> players = new LinkedList<>();
        for (UUID id : members) {
            Player p = Bukkit.getPlayer(id);
            if (p != null) players.add(p);
            else cleanUp();
        }
        return players;
    }

    public boolean hasPlayer(Player p) {
        return members.contains(p.getUniqueId());
    }

    public void toggleLootSharingOption(String path, String displayname) {
        boolean value = false;
        Player p = Bukkit.getPlayer(partyLeader);
        if (p == null) {
            cleanUp();
            return;
        }
        if (Objects.equals(path, "ffa")) {
            this.ffa = !ffa;
            value = ffa;
            Manager.setValue(p, "ffa", value);
        }
        if (Objects.equals(path, "lastHit")) {
            this.lastHit = !lastHit;
            value = lastHit;
            Manager.setValue(p, "lastHit", value);
        }
        if (Objects.equals(path, "roundRobin")) {
            this.roundRobin = !roundRobin;
            value = roundRobin;
            Manager.setValue(p, "roundRobin", value);
        }
        String v = "&coff";
        if (value) v = "&aon";
        broadCastMessage(Utils.getToggleString(v, displayname));
    }

    public void glowMembers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : getPlayers()) {
                    for (Player ps : getPlayers())
                        if (hasPartyGlowing(p)) {
                            if (p != ps) GlowAPI.setGlowing(ps, GlowAPI.Color.AQUA, p);
                        } else {
                            if (p == ps) continue;
                            if (GlowAPI.isGlowing(ps, p)) GlowAPI.setGlowing(ps, false, p);
                        }
                }
            }
        }.runTaskLater(PartyManager.instance(), 20);
    }

    boolean hasPartyGlowing(Player p) {
        return Manager.getValue(p, "partyGlow");
    }
}
