package org.spigotmc.cortex.cortexkits.utility.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.cortex.cortexkits.utility.data.Config;
import org.spigotmc.cortex.cortexkits.utility.data.DataManager;

public class KitAPI {
    public static Map<String, ItemStack[]> tmpKits = new HashMap<>();

    private static boolean isInventoryFull(Player p) { return (p.getInventory().firstEmpty() == -1); }

    public static void createKit(String name, ItemStack[] contents, int cooldown){
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(DataManager.FileType.KIT);
        main.getConfig().set("Cooldown", cooldown);
        for (int i = 0; i < contents.length; i++) {
            main.getConfig().set("Contents." + i, contents[i]);
        }

        main.saveConfig();
    }
    public static void createKit(String name, ItemStack[] contents){
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(DataManager.FileType.KIT);
        main.getConfig().set("Cooldown", 0);
        for (int i = 0; i < contents.length; i++) {
            main.getConfig().set("Contents." + i, contents[i]);
        }

        main.saveConfig();
    }

    public static void removeKit(String name) {
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(DataManager.FileType.KIT);
        if (main.exists())
        main.delete();
        tmpKits.remove(name.replaceAll("\\*", ""));
    }

    public boolean kitExists(String name) { return getAllKits().contains(name);}

    public boolean isTempKit(String name){return tmpKits.containsKey(name) && (!(getAllSavedKits().contains(name)));}

    public static ItemStack[] getKitContents(String name){return new Kit(null, name).retrieve();}

    public static List<String> getAllKits(){
        List<String> kitList = new ArrayList<>();
        DataManager dm = new DataManager();
        for(File file : Objects.requireNonNull(dm.getFolder(DataManager.FolderType.KITS).listFiles())){
            String name = file.getName().replaceAll(".yml", "");
            kitList.add(name);
        }
        for (String kit: tmpKits.keySet()) {kitList.add("*" + kit);}
        return kitList;
    }
    public static List<String> getAllSavedKits(){
        List<String> kitList = new ArrayList<>();
        DataManager dm = new DataManager();
        for(File file : Objects.requireNonNull(dm.getFolder(DataManager.FolderType.KITS).listFiles())) {
            String name = file.getName().replaceAll(".yml", "");
            kitList.add(name);
        }
        return kitList;
    }
    public static List<String> getAllTempKits(){ return new ArrayList<>(tmpKits.keySet()); }

    public static String formatTime(String value) {
        String time;
        int day = (int) TimeUnit.SECONDS.toDays(Integer.parseInt(value));
        long hours = TimeUnit.SECONDS.toHours(Integer.parseInt(value)) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(Integer.parseInt(value)) - (TimeUnit.SECONDS.toHours(Integer.parseInt(value))* 60);
        long second = TimeUnit.SECONDS.toSeconds(Integer.parseInt(value)) - (TimeUnit.SECONDS.toMinutes(Integer.parseInt(value)) *60);
        time = "&#6cc5e0" + day + " &#2358ccDays &#6cc5e0" + hours + " &#2358ccHours &#6cc5e0" + minute + " &#2358ccMinutes &#6cc5e0" + second + " &#2358ccSeconds";
        return time;
    }

    public static boolean kitAvailable(Player p, Kit kit) {
        boolean result = false;
        DataManager dm = new DataManager(p.getUniqueId().toString());
        Config user = dm.getFile(DataManager.FileType.USER);
        boolean cooldownActive = user.getConfig().contains(kit.getName() + ".Cooldown");
        if (cooldownActive) {
            Long a = user.getConfig().getLong(kit.getName() + ".Cooldown");
            Long b = System.currentTimeMillis();
            int compareNum = a.compareTo(b);
            if (compareNum > 0){
                result = false;
            }
        } else {
            result = true;
        }
        return result;
    }

    public static void giveKit(Kit kit){
        Player player = kit.getPlayer();
        if (getAllTempKits().contains(kit.getName())) {
            ItemStack[] contents = kit.retrieveTemp();
            int amnt = contents.length;
            for (int i = 0; i < amnt; ++i) {
                ItemStack item = contents[i];
                if (item != null) {
                    if (isInventoryFull(kit.getPlayer())) {
                        kit.getPlayer().getWorld().dropItemNaturally(kit.getPlayer().getLocation(), item);
                    } else
                        kit.getPlayer().getInventory().addItem(item);
                }
            }
        } else {
            if (getAllSavedKits().contains(kit.getName())) {
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
                Config playerFile = dm.getFile(DataManager.FileType.USER);
                Date date = new Date();   // given date
                playerFile.getConfig().set(kit.getName() + ".LastTimeGiven", date.toString());
                playerFile.saveConfig();
            }
        }
    }
}
