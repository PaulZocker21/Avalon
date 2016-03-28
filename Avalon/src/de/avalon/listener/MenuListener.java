package de.avalon.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.avalon.menu.Menu;
import de.avalon.menu.MenuItem;

public class MenuListener implements Listener {

	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		if (e.getSlot() != e.getRawSlot() || e.getSlot() == -999)
			return;
		for (Menu menu : Menu.getMenus()) {
			if (menu.getInventory().equals(e.getInventory())) {
				menu.getClickListeners().forEach(listener -> listener.click(e));
				MenuItem item = menu.getItem(e.getSlot());
				if (item == null)
					return;
				item.getListeners().forEach(listener -> listener.click(e));
				if (!item.isMovenable()) {
					e.setCancelled(true);
				}
				if(item.isAutoClose()) {
					e.getWhoClicked().closeInventory();
				}
				return;
			}
		}
	}

}
