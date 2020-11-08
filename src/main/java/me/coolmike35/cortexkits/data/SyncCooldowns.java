package me.coolmike35.cortexkits.data;

import org.bukkit.scheduler.BukkitRunnable;

public class SyncCooldowns extends BukkitRunnable {

    DataManager dm = new DataManager();

    @Override
    public void run() {
        dm.checkCooldowns();
    }
}
