package me.serenia.partymanager.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import me.serenia.partymanager.player.Manager;
import org.bukkit.entity.Player;

import static me.serenia.partymanager.Utils.getString;


@CommandAlias("partychat")
public class Chat extends BaseCommand {
    @Default
    @Description("Toggles party chat")
    public static void onLeave(Player p, String[] args){
        boolean isOn = Manager.getValue(p, "partyChat");
        Manager.setValue(p, "partyChat", !isOn);
        String msg = "&cdisabled";
        if (!isOn) msg = "&aenabled";
        p.sendMessage(getString("toggling-message", msg));
    }
}
