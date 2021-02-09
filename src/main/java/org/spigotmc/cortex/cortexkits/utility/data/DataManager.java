package org.spigotmc.cortex.cortexkits.utility.data;

import com.github.sanctum.labyrinth.formatting.string.ColoredString;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import org.spigotmc.cortex.cortexkits.CortexKits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataManager {

    private String name;
    private String directory;


    public DataManager(){}
    public DataManager(String name) {
        this.name = name;
    }
    public DataManager(String name, String directory) {
        this.name = name;
        this.directory = directory;
    }

    public enum FileType {
        KIT, USER, MISC
    }

    public Config getFile(FileType type) {
        Config result = null;
        switch (type) {
            case KIT:
                result = Config.get(name, "Kits");
                break;
            case USER:
                result = Config.get(name, "Users");
                break;
            case MISC:
                result = Config.get(name, directory);
                break;
        }
        return result;
    }

    public enum FolderType {
        KITS, USERS
    }

    private static final DataManager dm = new DataManager("Config", "Configuration");
    private static final Config main = dm.getFile(DataManager.FileType.MISC);

    public static String getPrefix() {
        return main.getConfig().getString("Prefix");
    }

    public static void msg(Player p, String text) {
        p.sendMessage(new ColoredString(getPrefix() + "&r " + text, ColoredString.ColorType.HEX).toString());
    }

    public static void copyDefault() {
        if (!main.exists()) {
            InputStream io = CortexKits.getInstance().getResource("Config.yml");
            Config.copy(io, main.getFile());
        }
    }

    public void checkCooldowns() {
        for (File player : Objects.requireNonNull(getFolder(FolderType.USERS).listFiles())) {
            String name = player.getName().replace(".yml", "");
            Config user = Config.get(name, "Users");
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
