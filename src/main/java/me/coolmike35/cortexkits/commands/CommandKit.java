package me.coolmike35.cortexkits.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

public class CommandKit extends BukkitCommand {
    protected CommandKit() {
        super("kit");
        setAliases(Arrays.asList("", ""));
        setPermission("");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
