package me.coolmike35.cortexkits;

import org.bukkit.plugin.java.JavaPlugin;

public final class CortexKits extends JavaPlugin {

    static CortexKits instance;

    public void onEnable() {

        instance = this;

    }

    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CortexKits getInstance() {
        return instance;
    }


}
