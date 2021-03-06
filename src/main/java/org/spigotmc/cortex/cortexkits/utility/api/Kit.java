package org.spigotmc.cortex.cortexkits.utility.api;

import org.spigotmc.cortex.cortexkits.CortexKits;
import org.spigotmc.cortex.cortexkits.utility.data.Config;
import org.spigotmc.cortex.cortexkits.utility.data.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Kit {

    private Player p;

    private String name;

    private int cooldown = 10800;

    public Kit(Player player, String name) {
        this.p = player;
        this.name = name;
    }
    public Kit(Player player, String name, int cooldown) {
        this.p = player;
        this.name = name;
        this.cooldown = cooldown; //Cooldown in seconds
    }

    public Player getPlayer(){
        return this.p;
    }

    public String getName(){
        return this.name;
    }

    // cacheing results
    public void hold() {
        ItemStack[] contents = p.getInventory().getContents();
        int amnt = contents.length;
        ItemStack[] clonedContents = new ItemStack[amnt];
        for (int i = 0; i < amnt; ++i) {
            ItemStack item = contents[i];
            if (item != null) {
                clonedContents[i] = item.clone();
            }
        }
        KitAPI.tmpKits.put(name, clonedContents);
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been temporarily cached.");
    }

    public void complete() {
        KitAPI.createKit(name, p.getInventory().getContents());
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been stored.");
    }

    public void completeTimed() {
        KitAPI.createKit(name, p.getInventory().getContents(), cooldown);
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been stored.");
    }

    public int getCooldown(){
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(DataManager.FileType.KIT);
        return main.getConfig().getInt("Cooldown");
    }

    public void setCooldown(int cooldown) {
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(DataManager.FileType.KIT);
        main.getConfig().set("Cooldown", cooldown);
        main.saveConfig();
    }

    public void setCategory(String category) {
        DataManager dm = new DataManager(name);
        Config main = dm.getFile(DataManager.FileType.KIT);
        main.getConfig().set("Category", category);
        main.saveConfig();
    }

    public boolean hasCooldown() {return cooldown != 0;}

    public boolean hasUsedKit() {
        DataManager dm = new DataManager();
        Config playerConfig = dm.getFile(DataManager.FileType.USER);
        return playerConfig.getConfig().contains(name + ".LastTimeGiven");
        }

    public Date getLastUsed() throws ParseException {
        DataManager dm = new DataManager(p.getUniqueId().toString());
        Config playerConfig = dm.getFile(DataManager.FileType.USER);
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(playerConfig.getConfig().getString(name + ".LastTimeGiven"));
    }


    public ItemStack[] retrieve() {
        ItemStack[] content;
            content = new ItemStack[36];
            DataManager dm = new DataManager(name);
            Config kit = dm.getFile(DataManager.FileType.KIT);
            for (int i = 0; i < 36; i++) {
                content[i] = kit.getConfig().getItemStack("Contents." + i);
            }
        return content;
    }

    public ItemStack[] retrieveTemp() {
        return KitAPI.tmpKits.get(name);
    }
}
