package me.coolmike35.cortexkits.commands;

import me.coolmike35.cortexkits.api.Kit;
import me.coolmike35.cortexkits.api.KitAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CommandKit extends BukkitCommand {
    protected CommandKit() {
        super("kit");
        setAliases(Arrays.asList("", ""));
        setPermission("");
        setName("kit");
        setDescription("Bruh");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        Player p = (Player) commandSender;
        int length = args.length;

        if(length == 1){
            Kit kit = new Kit(p, args[0]);
            ItemStack[] kitStack = kit.retrieve();
            p.getInventory().setContents(kitStack);
            return true;
        }

        if(length == 2) {
            Kit kit = new Kit(p, args[1]);
            if (args[0].equals("create")) {
                kit.complete();
                p.sendMessage(ChatColor.GREEN + "Created Kit " + args[1]);
                return true;
            }
            if (args[0].equals("remove")) {
                KitAPI.removeKit(args[1]);
                p.sendMessage(ChatColor.GRAY + "Removed Kit " + args[1]);
                return true;
            }
            p.sendMessage(ChatColor.RED + "Unknown sub-command");
        }






        return false;
    }
}
