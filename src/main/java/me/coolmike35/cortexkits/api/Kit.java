package me.coolmike35.cortexkits.api;

import me.coolmike35.cortexkits.CortexKits;
import me.coolmike35.cortexkits.data.Config;
import me.coolmike35.cortexkits.data.DataManager;
import me.coolmike35.cortexkits.data.FileType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Kit {

    private Player p;

    private String name;

    private int cooldown;

    public Kit(Player player, String name) {
        this.p = player;
        this.name = name;
        this.cooldown = 0;
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
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
        KitAPI.tmpKits.put(name, items);
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been temporarily cached.");
    }

    public void complete() {
        KitAPI.createKit(name, p.getInventory().getContents(), cooldown);
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been stored.");
    }

    public int getCooldown(){return cooldown;}

    public boolean hasCooldown() {return cooldown != 0;}

    public boolean hasUsedKit() throws Exception{
        DataManager dm = new DataManager();
        Config playerConfig = dm.getFile(FileType.USER);
        return playerConfig.getConfig().contains(name + ".LastTimeGiven");
        }

    public Date getLastUsed() throws ParseException {
        DataManager dm = new DataManager(p.getUniqueId().toString());
        Config playerConfig = dm.getFile(FileType.USER);
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(playerConfig.getConfig().getString(name + ".LastTimeGiven"));
    }


    public ItemStack[] retrieve() {
        ItemStack[] content;
        if (!KitAPI.tmpKits.containsKey(name)) {
            content = new ItemStack[27];
            DataManager dm = new DataManager(name);
            Config kit = dm.getFile(FileType.KIT);
            for (int i = 0; i < 27; i++) {
                content[i] = kit.getConfig().getItemStack("Contents." + i);
            }
        }else{
            content = new ItemStack[];
            for (int i = 0; i < KitAPI.tmpKits.get(name).size()/64 - 1; i++) {
                content[i] = KitAPI.tmpKits.get(name).get(i);
            }
        }
        return content;
    }
}
