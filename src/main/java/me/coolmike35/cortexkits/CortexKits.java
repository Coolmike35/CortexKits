package me.coolmike35.cortexkits;

import me.coolmike35.cortexkits.api.KitAPI;
import me.coolmike35.cortexkits.commands.Command;
import me.coolmike35.cortexkits.data.Config;
import me.coolmike35.cortexkits.data.DataManager;
import me.coolmike35.cortexkits.data.FileType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CortexKits extends JavaPlugin {

    static CortexKits instance;

    public void onEnable() {
        instance = this;
        Command.registerAll();


            DataManager dm = new DataManager();
            for (File file : Objects.requireNonNull(dm.getFolder().listFiles())) {
                KitAPI.kits.put(file.getName().replace(".yml", ""), new ItemStack[0]);
            }



    }

    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CortexKits getInstance() {
        return instance;
    }


}
