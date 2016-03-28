package de.avalon.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {

	private static ArrayList<Menu> menus = new ArrayList<>();

	public static Menu get(String name) {
		for (Menu menu : menus) {
			if (menu.getName().equals(name)) {
				return menu;
			}
		}
		return null;
	}

	public static ArrayList<Menu> getMenus() {
		return menus;
	}

	private String name;
	private Inventory inventory;
	private MenuItem[] items;
	private List<Player> viewers;
	private List<ClickListener> clickListeners;

	public Menu(String name, String title, int rows) {
		this.viewers = new ArrayList<>();
		this.clickListeners = new ArrayList<>();
		this.name = name;
		this.inventory = Bukkit.createInventory(null, rows * 9, title);
		this.items = new MenuItem[inventory.getSize()];

		menus.add(this);
	}

	public MenuItem addItem(ItemStack item) {
		return addItem(item, 0);
	}

	public MenuItem addItem(ItemStack item, int startIndex) {
		if (startIndex < 0 || startIndex >= items.length)
			return null;
		for (int i = startIndex; i < items.length; i++) {
			MenuItem menuItem = items[i];
			if (menuItem == null) {
				return createItem(i, item);
			}
		}
		return null;
	}

	public void addClickListener(ClickListener listener) {
		clickListeners.add(listener);
	}

	public List<ClickListener> getClickListeners() {
		return clickListeners;
	}

	public void openInventory(Player player) {
		player.openInventory(inventory);
		addViewer(player);
	}

	public void addViewer(Player player) {
		this.viewers.add(player);
	}

	public List<Player> getViewers() {
		return viewers;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public MenuItem[] getItems() {
		return items;
	}

	public MenuItem getItem(int index) {
		if (index >= items.length || index < 0)
			return null;
		return items[index];
	}

	public String getName() {
		return name;
	}

	public void set(int index, MenuItem item) {
		if (index >= items.length || index < 0)
			return;
		items[index] = item;
		inventory.setItem(index, item.getItem());
	}

	public MenuItem createItem(int slot, ItemStack item) {
		if (slot >= items.length || slot < 0)
			return null;
		MenuItem menuItem = new MenuItem(slot, item);
		set(slot, menuItem);
		return menuItem;
	}
}
