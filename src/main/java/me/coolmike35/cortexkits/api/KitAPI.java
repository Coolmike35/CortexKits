package me.coolmike35.cortexkits.api;

import me.coolmike35.cortexkits.data.Config;
import me.coolmike35.cortexkits.data.DataManager;
import me.coolmike35.cortexkits.data.FileType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class KitAPI {
    public static HashMap<String, ItemStack[]> tmpKits = new HashMap<>();

    private static boolean isInventoryFull(Player p) { return (p.getInventory().firstEmpty() == -1); }

    public static void createKit(String name, ItemStack[] contents, int cooldown){
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(FileType.KIT);
        main.getConfig().set("Cooldown", cooldown);
        for (int i = 0; i < contents.length; i++) {
            main.getConfig().set("Contents." + i, contents[i]);
        }

        main.saveConfig();
    }
    public static void createKit(String name, ItemStack[] contents){
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(FileType.KIT);
        main.getConfig().set("Cooldown", 0);
        for (int i = 0; i < contents.length; i++) {
            main.getConfig().set("Contents." + i, contents[i]);
        }

        main.saveConfig();
    }


    public static void removeKit(String name) {
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(FileType.KIT);
        main.delete();
        tmpKits.remove(name);
    }

    public boolean kitExists(String name) { return getAllKits().contains(name);}

    public boolean isTempKit(String name){return tmpKits.containsKey(name) && (!(getAllSavedKits().contains(name)));}

    public static ItemStack[] getKitContents(String name){return new Kit(null, name).retrieve();}

    public static List<String> getAllKits(){
        List<String> kitList = new ArrayList<>();
        DataManager dm = new DataManager();
        for(File file : Objects.requireNonNull(dm.getFolder().listFiles())){
            String name = file.getName().replaceAll(".yml", "");
            kitList.add(name);
        }
        for (String kit: tmpKits.keySet()) {kitList.add("*" + kit);}
        return kitList;
    }
    public static List<String> getAllSavedKits(){
        List<String> kitList = new ArrayList<>();
        DataManager dm = new DataManager();
        for(File file : Objects.requireNonNull(dm.getFolder().listFiles())){
            String name = file.getName().replaceAll(".yml", "");
            kitList.add(name);
        }
        return kitList;
    }
    public static List<String> getAllTempKits(){ return new ArrayList<>(tmpKits.keySet()); }


    public static void giveKit(Kit kit){
        Player player = kit.getPlayer();
        ItemStack[] kitStack = kit.retrieve();
        for (ItemStack item : kitStack) {
            if (item != null) {
                if (isInventoryFull(player)) {
                    player.getWorld().dropItem(player.getLocation(), item);
                } else player.getInventory().addItem(item);
            }
        }
        //Logs date that kit was given to the player
        DataManager dm = new DataManager(player.getUniqueId().toString());
        Config playerFile = dm.getFile(FileType.USER);
        playerFile.getConfig().set(kit.getName() + ".LastTimeGiven", new Date().toString());
    }
}
