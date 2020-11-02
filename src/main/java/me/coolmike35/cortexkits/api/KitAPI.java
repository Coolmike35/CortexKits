package me.coolmike35.cortexkits.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KitAPI {
    private static HashMap<String, ItemStack[]> kits = new HashMap<>();


    public static void createKit(){

    }


    public static void removeKit() {

    }


    public ItemStack[] getKitContents(String name){
        return kits.get(name);
    }

    public static List<String> getAllKits(){
        return new ArrayList<>(kits.keySet());
    }
}
