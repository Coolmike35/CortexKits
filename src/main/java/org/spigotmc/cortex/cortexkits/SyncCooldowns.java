package org.spigotmc.cortex.cortexkits;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.cortex.cortexkits.utility.data.DataManager;

public class SyncCooldowns extends BukkitRunnable {

    DataManager dm = new DataManager();

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() > 0) {

            dm.checkCooldowns();

        }
    }
}
