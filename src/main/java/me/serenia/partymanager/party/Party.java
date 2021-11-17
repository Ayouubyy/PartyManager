package me.serenia.partymanager.party;



import lombok.Data;
import me.serenia.partymanager.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;
import static me.serenia.partymanager.PartyManager.*;
@Data
public class Party {
     UUID partyLeader;
     LinkedList<UUID> members;
     boolean roundRobin;
     boolean ffa;
     boolean lastHit;
    public Party(UUID partyLeader, LinkedList<UUID> members, boolean roundRobin, boolean ffa, boolean lastHit){
        this.partyLeader = partyLeader;
        this.members = members;
        this.roundRobin = roundRobin;
        this.ffa = ffa;
        this.lastHit = lastHit;
    }
    int size(){
        return members.size();
    }

    void kick(Player p){
        UUID id = p.getUniqueId();
        if (id == partyLeader){

        }
        members.remove(p.getUniqueId());

    }
    void cleanUp(){
        members.removeIf(uuid -> (Bukkit.getPlayer(uuid) == null));
        if (size() > 2){
           if (!parties.contains(this)) return;
           parties.remove(this);
        } else {
            partyLeader = members.get(0);
        }
    }
    void broadCastMessage(String s){
        for (Player p : getPlayers()){
            p.sendMessage(Utils.chat(s));
        }
    }
    LinkedList<Player> getPlayers(){
        LinkedList<Player> players = new LinkedList<>();
        for (UUID id : members){
            Player p = Bukkit.getPlayer(id);
            if (p != null) players.add(p); else cleanUp();
        }
        return players;
    }
    boolean hasPlayer(Player p){
        return members.contains(p.getUniqueId());
    }
}
