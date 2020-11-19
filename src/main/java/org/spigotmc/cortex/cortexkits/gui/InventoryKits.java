package org.spigotmc.cortex.cortexkits.gui;

import com.youtube.hempfest.hempcore.HempCore;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.hempcore.gui.Pagination;
import java.util.ArrayList;
import org.spigotmc.cortex.cortexkits.utility.api.Kit;
import org.spigotmc.cortex.cortexkits.utility.api.KitAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.spigotmc.cortex.cortexkits.utility.data.DataManager;

public class InventoryKits extends Pagination {

    public InventoryKits(GuiLibrary guiLibrary) {
        super(guiLibrary);
    }

    @Override
    public String getMenuName() {
        return new ColoredString(DataManager.getPrefix() + " &e&oAll kits", ColoredString.ColorType.MC).toString();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String name = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(HempCore.getInstance(), "kit"), PersistentDataType.STRING);
        Material mat = e.getCurrentItem().getType();
        if (mat.equals(Material.PAPER)) {
            Bukkit.dispatchCommand(p, "kit " + name);
            p.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        ArrayList<String> kits = new ArrayList<>(KitAPI.getAllKits());
        if (kits != null && !kits.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= kits.size())
                    break;
                if (kits.get(index) != null) {
                    String color = "";
                    if (kits.get(index).contains("*")) {
                        color = "&f(&a&l&oTEMP&f)&e ";
                    }
                    String useable = "";
                    if (!KitAPI.kitAvailable(guiLibrary.getViewer(), new Kit(guiLibrary.getViewer(), kits.get(index)))) {
                        useable = "&e&o&m";
                    } else {
                        useable = "&f";
                    }
                    ItemStack item = makePersistentItem(Material.PAPER, "&b&l&oKit: " + useable + color + kits.get(index), "kit", kits.get(index), "", "Click to obtain kit.");
                    inventory.addItem(item);
                }
            }
        }
        setFillerGlassLight();
    }
}
