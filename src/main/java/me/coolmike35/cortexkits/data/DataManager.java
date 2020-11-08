package me.coolmike35.cortexkits.data;

import com.youtube.hempfest.hempcore.hempcore.formatting.string.ColoredString;
import me.coolmike35.cortexkits.CortexKits;
import me.coolmike35.cortexkits.api.KitAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

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

    public enum FolderType {
        KITS, USERS
    }

    private void msg(Player p, String text) {
        p.sendMessage(new ColoredString("&7[&3&lCortex&7] &r" + text, ColoredString.ColorType.HEX).toString());
    }

    public void checkCooldowns() {
        for (File player : Objects.requireNonNull(getFolder(FolderType.USERS).listFiles())) {
            String name = player.getName().replace(".yml", "");
            Config user = new Config(name, "Users");
            for (String kit : user.getConfig().getKeys(false)) {
                if (user.getConfig().getLong(kit + ".Cooldown") < System.currentTimeMillis()) {
                    user.getConfig().set(kit, null);
                    user.saveConfig();
                    CortexKits.getInstance().getLogger().info("- Cooldown for kit " + '"' + kit + '"' + " on player " + '"' + Bukkit.getOfflinePlayer(UUID.fromString(name)).getName() + '"' + " has expired.");
                    if (Bukkit.getOfflinePlayer(UUID.fromString(name)).isOnline()) {
                        msg(Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(name))), "&#8da832&oCooldown for kit " + '"' + kit + '"' + " has expired.");
                    }
                    }
            }
        }
    }

    public File getFolder(FolderType type) {
        final File dir = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File d;
        File result = null;
        switch (type) {
            case KITS:
                d = new File(dir.getParentFile().getPath(), CortexKits.getInstance().getDescription().getName() + "/" + "Kits" + "/");
                if(!d.exists()) {
                    d.mkdirs();
                }
                result = d;
                break;
            case USERS:
                d = new File(dir.getParentFile().getPath(), CortexKits.getInstance().getDescription().getName() + "/" + "Users" + "/");
                if(!d.exists()) {
                    d.mkdirs();
                }
                result = d;
                break;
        }
        return result;
    }


}
