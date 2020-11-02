package me.coolmike35.cortexkits.api;

import me.coolmike35.cortexkits.data.Config;
import me.coolmike35.cortexkits.data.DataManager;
import me.coolmike35.cortexkits.data.FileType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.*;

public class KitAPI {
    public static HashMap<String, ItemStack[]> kits = new HashMap<>();

    public static HashMap<UUID, ItemStack[]> player = new HashMap<>();


    public static void createKit(String name, ItemStack[] contents){
        DataManager dm = new DataManager(name, "Kits");
        Config main = dm.getFile(FileType.KIT);
        main.getConfig().set("Contents", contents);
        main.saveConfig();
    }


    public static void removeKit(String name) {
        DataManager dm = new DataManager(name, "Kits");
        Config main = dm.getFile(FileType.KIT);
        main.delete();
    }

    public boolean kitExists(String name) {
        return getAllKits().contains(name);
    }


    public static ItemStack[] getKitContents(String name){
        return kits.get(name);
    }

    public static List<String> getAllKits(){
        return new ArrayList<>(kits.keySet());
    }
}
