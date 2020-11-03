package me.coolmike35.cortexkits.api;

import me.coolmike35.cortexkits.CortexKits;
import me.coolmike35.cortexkits.data.Config;
import me.coolmike35.cortexkits.data.DataManager;
import me.coolmike35.cortexkits.data.FileType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Kit {

    private Player p;

    private String name;

    public Kit(Player player, String name) {
        this.p = player;
        this.name = name;
    }

    // cacheing results
    public void initialize() {
       KitAPI.kits.put(name, p.getInventory().getContents());
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been cached.");
    }

    public void complete() {
        KitAPI.kits.put(name, p.getInventory().getContents());
        KitAPI.createKit(name, p.getInventory().getContents());
        CortexKits.getInstance().getLogger().info("- Kit " + '"' + name + '"' + " has been stored.");
    }


    public ItemStack[] retrieve() {
            ItemStack[] content = new ItemStack[27];
            DataManager dm = new DataManager(name, "Kits");
            Config kit = dm.getFile(FileType.KIT);
            for (int i = 0; i < 27; i++) {
                content[i] = kit.getConfig().getItemStack("Contents." + i);
            }
            return content;
    }


}
