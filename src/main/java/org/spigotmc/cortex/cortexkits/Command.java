package org.spigotmc.cortex.cortexkits;

import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import org.spigotmc.cortex.cortexkits.commands.CommandKit;

public class Command {


    public static void registerCommand(BukkitCommand command) {
        try {

            final Field commandMapField = CortexKits.getInstance().getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(CortexKits.getInstance().getServer());
            commandMap.register(command.getLabel(), command);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerAll() {
        registerCommand(new CommandKit());
    }

    public static void synchronizeCooldowns(CortexKits instance) {
        SyncCooldowns synchronize = new SyncCooldowns();
        synchronize.runTaskTimerAsynchronously(instance, 20, 20);
    }

}
