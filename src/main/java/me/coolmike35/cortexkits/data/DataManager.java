package me.coolmike35.cortexkits.data;

import me.coolmike35.cortexkits.CortexKits;
import me.coolmike35.cortexkits.api.KitAPI;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class DataManager {

    String name;

    public DataManager(){};
    public DataManager(String name) {
        this.name = name;
    }


    public Config getFile(FileType type) {
        Config result;
        switch (type) {
            case KIT:
                result = new Config(name, "Kits");
                break;
            case USER:
                result = new Config(name, "Users");
                break;
            default:
            result = new Config(name, "");
                break;
        }
        return result;
    }

    public File getFolder() {
        final File dir = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File d = new File(dir.getParentFile().getPath(), CortexKits.getInstance().getDescription().getName() + "/" + "Kits" + "/");
        if(!d.exists()) {
            d.mkdirs();
        }
        return d;
    }


}
