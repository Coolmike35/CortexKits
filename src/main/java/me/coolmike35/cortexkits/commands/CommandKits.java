package me.coolmike35.cortexkits.commands;

import me.coolmike35.cortexkits.api.KitAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandKits extends BukkitCommand {
    protected CommandKits() {
        super("kits");
        setAliases(Arrays.asList("", ""));
        setPermission("");
        setName("kits");
        setDescription("Bruhs");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player p = (Player) sender;
        if(args.length == 0){
            p.sendMessage(KitAPI.getAllKits().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
       return true;
        }
        p.sendMessage(ChatColor.RED + "Unknown sub-command");
        return false;
    }
}
