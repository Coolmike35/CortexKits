package me.coolmike35.cortexkits.commands;

import com.youtube.hempfest.hempcore.hempcore.HempCore;
import com.youtube.hempfest.hempcore.hempcore.formatting.component.Text;
import com.youtube.hempfest.hempcore.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.hempcore.hempcore.gui.GuiLibrary;
import me.coolmike35.cortexkits.CortexKits;
import me.coolmike35.cortexkits.api.Kit;
import me.coolmike35.cortexkits.api.KitAPI;
import me.coolmike35.cortexkits.data.Config;
import me.coolmike35.cortexkits.data.DataManager;
import me.coolmike35.cortexkits.data.FileType;
import me.coolmike35.cortexkits.gui.InventoryKits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CommandKit extends BukkitCommand {

    List<String> arguments = new ArrayList<>();

    Kit kit;

    public CommandKit() {
        super("kit");
        setAliases(Arrays.asList("kits", "hkits"));
        setPermission("kits");
        setDescription("Main global kit use command.");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (arguments.isEmpty()) {
            arguments.addAll(Arrays.asList("create", "hold", "give", "remove", "save", "timer"));
        }

        for (String saved : KitAPI.getAllKits()) {
            if (!arguments.contains(saved)) {
                arguments.add(saved);
            }
        }

        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            Player p = (Player) sender;

            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            if (!p.hasPermission(this.getPermission() + ".modify")) {
                result.remove("create");
                result.remove("remove");
                result.remove("hold");
                result.remove("save");
                result.remove("timer");
                result.removeIf(res -> !p.hasPermission(this.getPermission() + ".kit." + res));
            }
            result.removeIf(res -> !KitAPI.getAllKits().contains(res) && !res.equals("create") && !KitAPI.getAllKits().contains(res) && !res.equals("remove") && !KitAPI.getAllKits().contains(res) && !res.equals("hold") && !KitAPI.getAllKits().contains(res) && !res.equals("save") && !KitAPI.getAllKits().contains(res) && !res.equals("timer") && !KitAPI.getAllKits().contains(res) && !res.equals("give"));
            return result;
        }
        if (args.length == 2) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")) {
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                        result.add(a);
                }
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    result.clear();
                }
                result.removeIf(res -> !KitAPI.getAllKits().contains(res));
                return result;
            }
            if (args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("s")) {
                for (String a : KitAPI.getAllSavedKits()) {
                        result.remove(a);
                }
                result.removeIf(res -> !KitAPI.getAllKits().contains(res));
                for (String a : KitAPI.getAllTempKits()) {
                    result.add(a);
                }
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    result.clear();
                }
                return result;
            }
            if (args[0].equalsIgnoreCase("timer") || args[0].equalsIgnoreCase("t")) {
                result.removeIf(res -> !KitAPI.getAllKits().contains(res));
                for (String a : KitAPI.getAllSavedKits()) {
                    result.add(a);
                }
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    result.clear();
                }
                return result;
            }
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
                for (Player a : Bukkit.getOnlinePlayers()) {
                    result.add(a.getName());
                }
                result.removeIf(res -> KitAPI.getAllKits().contains(res));
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    result.clear();
                }
                return result;
            }
        }
        if (args.length == 3) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
                for (String a : KitAPI.getAllKits()) {
                    result.add(a);
                }
                result.removeIf(res -> !KitAPI.getAllKits().contains(res));
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    result.clear();
                }
                return result;
            }
        }
        return null;
    }

    private void msg(Player p, String text) {
        p.sendMessage(new ColoredString("&7[&3&lCortex&7] &r" + text, ColoredString.ColorType.HEX).toString());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        Player p = (Player) commandSender;
        int length = args.length;
        if(args.length == 0) {
            new InventoryKits(HempCore.guiManager(p)).open();
            return true;
        }
        if(length == 1) {
            String name = args[0].replaceAll("\\*", "");
            Kit kit = new Kit(p, name);
            DataManager dm = new DataManager(p.getUniqueId().toString());
            Config user = dm.getFile(FileType.USER);
            if (!p.hasPermission(this.getPermission() + ".kit." + kit.getName())) {
                msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this kit.");
                return true;
            }
            if (args[0].equalsIgnoreCase("timer")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                msg(p, "&#ed2d2dInvalid usage: &#de9012integer value required. Ex. (" + '"' + "140" + '"' + ")");
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                msg(p, "&#ed2d2dInvalid usage: &#de9012Player and kit values required. Ex. (" + '"' + "/kit give Hempfest Starter" + '"' + ")");
                return true;
            }
            if (args[0].equalsIgnoreCase("confirm")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (this.kit != null) {
                    this.kit.complete();
                    msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + this.kit.getName() + "&f" + '"' + " &#12deb8&ohas been created.");
                    this.kit = null;
                } else {
                    msg(p, "&#ed2d2d&lError &8&l» &#de9012&oA kit in selection was not found.");
                    return true;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("cancel")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (this.kit != null) {
                    msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + this.kit.getName() + "&f" + '"' + " &#12deb8&ocreation has been cancelled.");
                    this.kit = null;
                } else {
                    msg(p, "&#ed2d2d&lError &8&l» &#de9012&oA kit in selection was not found.");
                    return true;
                }
                return true;
            }
            if (!KitAPI.kitAvailable(p, kit)) {
                    long timeUsed = (System.currentTimeMillis() - user.getConfig().getLong(kit.getName() + ".Cooldown")) / 1000;
                    int timeLeft = Integer.parseInt(String.valueOf(timeUsed).replace("-", ""));
                    if (timeLeft == 0) {
                        user.getConfig().set(kit.getName(), null);
                        user.saveConfig();
                    }
                if (p.hasPermission(this.getPermission() + ".modify")) {
                    KitAPI.giveKit(kit);
                    msg(p, "&#34eb71[BYPASS] &#12deb8&oKit &f" + '"' + "&#de9012" + args[0] + "&f" + '"' + " &#12deb8&ohas been given.\n" + "&#e06c8d&oYou can theoretically use this kit again in &f: " + KitAPI.formatTime(String.valueOf(timeLeft)));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 10);
                    return true;
                }
                    msg(p, "&#fc0349&oKit &f" + '"' + "&#de9012" + args[0] + "&f" + '"' + " &#fc0349&ohas already been given.\n&#e06c8d&oYou can use this kit again in &f: " + KitAPI.formatTime(String.valueOf(timeLeft)));
            } else {
                if (!KitAPI.getAllSavedKits().contains(kit.getName()) && !KitAPI.getAllTempKits().contains(kit.getName())) {
                    msg(p, "&#ed2d2dInvalid syntax: &#de9012kit not found.");
                    return true;
                }
                if (KitAPI.getAllSavedKits().contains(kit.getName())) {
                    user.getConfig().set(kit.getName() + ".Cooldown", System.currentTimeMillis() + (kit.getCooldown() * 1000));
                }
                KitAPI.giveKit(kit);
                msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + args[0] + "&f" + '"' + " &#12deb8&ohas been given.");
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 10);
                user.saveConfig();
                return true;
            }
            return true;
        }

        if(length == 2) {
            Kit kit = new Kit(p, args[1]);
            if (args[0].equals("create")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (KitAPI.getAllSavedKits().contains(args[1]) || KitAPI.getAllTempKits().contains(args[1])) {
                    String source = "Unknown";
                    if (KitAPI.getAllTempKits().contains(args[1])) {
                        source = "Temp";
                    }
                    if (KitAPI.getAllSavedKits().contains(args[1])) {
                        source = "Saved";
                        msg(p, "&#ed2d2dWARNING: &#12deb8Confirmation will result in overwriting the existing kit.");
                    }
                    this.kit = kit;
                    msg(p, "&#de9012A kit with this name already exists in storage : " + source + "\n&#12deb8Would you like to create the kit anyway? Type /kit confirm or /kit cancel to cancel.");
                    return true;
                }
                kit.complete();
                msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + args[1] + "&f" + '"' + " &#12deb8&ohas been created.");
                return true;
            }
            if (args[0].equals("hold")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                kit.hold();
                msg(p, "&#de9012&oTemporary Kit &f" + '"' + "&#12deb8" + args[1] + "&f" + '"' + " &#de9012&ohas been cached.");
                return true;
            }
            if (args[0].equalsIgnoreCase("timer")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                msg(p, "&#ed2d2dInvalid usage: &#de9012integer value required. Ex. (" + '"' + "140" + '"' + ")");
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                msg(p, "&#ed2d2dInvalid usage: &#de9012Player and kit values required. Ex. (" + '"' + "/kit give Hempfest Starter" + '"' + ")");
                return true;
            }
            if (args[0].equals("save")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (!KitAPI.getAllTempKits().contains(kit.getName())) {
                    msg(p, "&#ed2d2d&lError &8&l» &#de9012&oTemporary Kit &f" + '"' + "&#12deb8" + args[1] + "&f" + '"' + " &#de9012&owas not found.\n&#e07b7b&o(Ensure you have already cached the kit before saving.)");
                    return true;
                }
                KitAPI.createKit(kit.getName(), KitAPI.tmpKits.get(kit.getName()));
                KitAPI.tmpKits.remove(kit.getName());
                msg(p, "&#de4f12&oTemporary Kit &f" + '"' + "&#2d80ed" + args[1] + "&f" + '"' + " &#de4f12&ohas been moved to storage.");
                return true;
            }
            if (args[0].equals("remove")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (KitAPI.getAllKits().contains(args[1])) {
                    msg(p, "&#8c7683&oKit &f" + '"' + "&#a83277" + args[1] + "&f" + '"' + " &#8c7683&ohas been removed.");
                }
                KitAPI.removeKit(args[1]);
                return true;
            }
            p.sendMessage(ChatColor.RED + "Unknown sub-command");
        }

        if (length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    msg(p, "&#ed2d2d&lError &8&l» &#de9012&oPlayer &f" + '"' + "&#12deb8" + args[1] + "&f" + '"' + " &#de9012&owas not found.\n&#e07b7b&o(Ensure you are targeting an online player.)");
                    return true;
                }
                if (!KitAPI.getAllKits().contains(args[2])) {
                    msg(p, "&#ed2d2d&lError &8&l» &#de9012&oKit &f" + '"' + "&#12deb8" + args[2] + "&f" + '"' + " &#de9012&owas not found.\n&#e07b7b&o(Ensure you are giving a valid kit type.)");
                    return true;
                }
                Kit forTarget = new Kit(target, args[2].replace("\\*", ""), 0);
                KitAPI.giveKit(forTarget);
                msg(target, "&#12deb8&oKit &f" + '"' + "&#de9012" + args[2] + "&f" + '"' + " &#12deb8&ohas been given with no cooldown effects.");
                msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + args[2] + "&f" + '"' + " &#12deb8&owas given to &f" + '"' + "&#de9012" + target.getName() + "&f" +'"');
                return true;
            }
            Kit kit = new Kit(p, args[1], Integer.parseInt(args[2]));
            if (args[0].equals("create")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (KitAPI.getAllSavedKits().contains(args[1]) || KitAPI.getAllTempKits().contains(args[1])) {
                    String source = "Unknown";
                    if (KitAPI.getAllTempKits().contains(args[1])) {
                        source = "Temp";
                    }
                    if (KitAPI.getAllSavedKits().contains(args[1])) {
                        source = "Saved";
                        msg(p, "&#ed2d2dWARNING: &#12deb8Confirmation will result in overwriting the existing kit.");
                    }
                    this.kit = kit;
                    msg(p, "&#de9012A kit with this name already exists in storage : " + source + "\n&#12deb8Would you like to create the kit anyway? Type /kit confirm or /kit cancel to cancel.");
                    return true;
                }
                kit.completeTimed();
                msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + args[1] + "&f" + '"' + " &#12deb8&ohas been created with a cooldown of: " + KitAPI.formatTime(args[2]));
                return true;
            }

            if (args[0].equals("timer")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                kit.setCooldown(Integer.parseInt(args[2]));
                msg(p, "&#12deb8&oKit &f" + '"' + "&#de9012" + args[1] + "&f" + '"' + " &#12deb8&ocooldown has been updated to : " + KitAPI.formatTime(args[2]));
                return true;
            }

            if (args[0].equals("save")) {
                if (!p.hasPermission(this.getPermission() + ".modify")) {
                    msg(p, "&#ed2d2dIncorrect permission: &#de9012You are not allowed access to this command.");
                    return true;
                }
                if (!KitAPI.getAllTempKits().contains(kit.getName())) {
                    msg(p, "&#ed2d2d&lError &8&l» &#de9012&oTemporary Kit &f" + '"' + "&#12deb8" + args[1] + "&f" + '"' + " &#de9012&owas not found.\n&#e07b7b&o(Ensure you have already cached the kit before saving.)");
                    return true;
                }
                KitAPI.createKit(kit.getName(), KitAPI.tmpKits.get(kit.getName()), Integer.parseInt(args[2]));
                KitAPI.tmpKits.remove(kit.getName());
                msg(p, "&#de4f12&oKit &f" + '"' + "&#2d80ed" + args[1] + "&f" + '"' + " &#de4f12&ohas been moved to storage with a cooldown of: " + KitAPI.formatTime(args[2]));
                return true;
            }

            return true;
        }






        return false;
    }
}
