package me.coolmike35.cortexkits.data;

import me.coolmike35.cortexkits.CortexKits;
import me.coolmike35.cortexkits.api.KitAPI;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class DataManager {

    String name;

    String directory;

    public DataManager(){};
    public DataManager(String name, String directory) {
        this.name = name;
        this.directory = directory;
    }


    public Config getFile(FileType type) {
        Config result;
        switch (type) {
           default:
                result = new Config(name, directory);
                break;
        }
        return result;
    }

    public void loadDefaults() {
        for (File file : getFolder().listFiles()) {
            String name = file.getName().replace(".yml", "");
            DataManager dm = new DataManager(name, "Kits");
            Config kit = dm.getFile(FileType.KIT);
            ItemStack[] content = new ItemStack[27];
            for (int i = 0; i < 27; i++) {
                content[i] = kit.getConfig().getItemStack("Content." + i);
            }
            KitAPI.kits.put(name, content);
            CortexKits.getInstance().getLogger().info("- All kits have been cached. Ready for use.");
        }
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
