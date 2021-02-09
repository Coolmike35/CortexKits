package org.spigotmc.cortex.cortexkits.gui;

import com.github.sanctum.labyrinth.Labyrinth;
import com.github.sanctum.labyrinth.formatting.string.ColoredString;
import com.github.sanctum.labyrinth.gui.GuiLibrary;
import com.github.sanctum.labyrinth.gui.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCategory extends Menu {

	public InventoryCategory(GuiLibrary guiLibrary) {
		super(guiLibrary);
	}

	@Override
	public String getMenuName() {
		return new ColoredString("&8Choose a category to browse »", ColoredString.ColorType.HEX).toString();
	}

	@Override
	public int getSlots() {
		return 9;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Material mat = e.getCurrentItem().getType();
		GuiLibrary guiLibrary = Labyrinth.guiManager(p);
		switch (mat) {
			case HEART_OF_THE_SEA:
				new InventoryMisc(guiLibrary).open();
				break;

			case EMERALD:
				new InventoryRank(guiLibrary).open();
				break;

			case ICE:
				new InventoryTemp(guiLibrary).open();
				break;

		}
	}

	@Override
	public void setMenuItems() {
		ItemStack misc = makeItem(Material.HEART_OF_THE_SEA, "       &8»» &eMisc &8««", "      &f&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "       &7&oClick to view", "&7&oall non-categorized kits.");
		ItemStack ranks = makeItem(Material.EMERALD, "       &8»» &aRank &8««", "      &f&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "       &7&oClick to view", "&7&oall rank specialized kits.");
		ItemStack temp = makeItem(Material.ICE, "      &8»» &fTemp &8««", "     &f&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "      &7&oClick to view", "  &7&oall temporary kits.");
		inventory.setItem(4, ranks);
		inventory.setItem(3, misc);
		inventory.setItem(5, temp);
	}
}
