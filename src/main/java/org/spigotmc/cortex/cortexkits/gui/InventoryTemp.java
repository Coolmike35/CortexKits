package org.spigotmc.cortex.cortexkits.gui;

import com.github.sanctum.labyrinth.Labyrinth;
import com.github.sanctum.labyrinth.formatting.string.ColoredString;
import com.github.sanctum.labyrinth.gui.GuiLibrary;
import com.github.sanctum.labyrinth.gui.Pagination;
import java.util.ArrayList;
import org.bukkit.ChatColor;
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

public class InventoryTemp extends Pagination {

    public InventoryTemp(GuiLibrary guiLibrary) {
        super(guiLibrary);
    }

    @Override
    public String getMenuName() {
        return new ColoredString(DataManager.getPrefix() + " &d&oTemp Kits", ColoredString.ColorType.MC).toString();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String name = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Labyrinth.getInstance(), "kit"), PersistentDataType.STRING);
        Material mat = e.getCurrentItem().getType();
        ArrayList<String> kits = new ArrayList<>(KitAPI.getAllTempKits());
        if (mat.equals(Material.PAPER)) {
            Bukkit.dispatchCommand(p, "kit " + name);
            p.closeInventory();
        }

        switch (mat) {
            case BARRIER:
                p.closeInventory();
                break;
            case DARK_OAK_BUTTON:
                if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
                    if (page == 0) {
                        p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                    } else {
                        page = page - 1;
                        super.open();
                    }
                } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
                        .equalsIgnoreCase("Right")) {
                    if (!((index + 1) >= kits.size())) {
                        page = page + 1;
                        super.open();
                    } else {
                        p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                    }
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        ArrayList<String> kits = new ArrayList<>(KitAPI.getAllTempKits());
        if (kits != null && !kits.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= kits.size())
                    break;
                if (kits.get(index) != null) {
                    String color = "&8(&a&l&oTEMP&8)&e ";
                    ItemStack item = makePersistentItem(Material.PAPER, "&b&l&oKit: " + color + kits.get(index), "kit", kits.get(index), "", "Click to obtain kit.");
                    inventory.addItem(item);
                }
            }
        }
        setFillerGlassLight();
    }
}
