package me.coolmike35.cortexkits;

import me.coolmike35.cortexkits.commands.Command;
import org.bukkit.plugin.java.JavaPlugin;

public final class CortexKits extends JavaPlugin {

    static CortexKits instance;

    public void onEnable() {
        instance = this;
        Command.registerAll();
    }

    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CortexKits getInstance() {
        return instance;
    }


}
