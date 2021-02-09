package org.spigotmc.cortex.cortexkits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.cortex.cortexkits.utility.data.DataManager;

public final class CortexKits extends JavaPlugin {

    static CortexKits instance;

    public void onEnable() {
        instance = this;
        getLogger().info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        getLogger().info("- Your simple solution to kits has arrived.");
        getLogger().info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        for (String ch : logo()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getLogger().info("- " + ch);
        }
        getLogger().info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        Command.registerAll();
        Command.synchronizeCooldowns(this);
        DataManager.copyDefault();
    }

    private List<String> logo() {
       return new ArrayList<>(Arrays.asList("   ▄▄·       ▄▄▄  ▄▄▄▄▄▄▄▄ .▐▄• ▄ ", "  ▐█ ▌▪▪     ▀▄ █·•██  ▀▄.▀· █▌█▌▪", "  ██ ▄▄ ▄█▀▄ ▐▀▀▄  ▐█.▪▐▀▀▪▄ ·██· ", "  ▐███▌▐█▌.▐▌▐█•█▌ ▐█▌·▐█▄▄▌▪▐█·█▌", "  ·▀▀▀  ▀█▄▀▪.▀  ▀ ▀▀▀  ▀▀▀ •▀▀ ▀▀"));
    }

    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CortexKits getInstance() {
        return instance;
    }


}
