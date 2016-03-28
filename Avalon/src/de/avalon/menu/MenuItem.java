package de.avalon.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MenuItem {

	private ItemStack item;
	private int slot;
	private List<ClickListener> listeners;
	private boolean moveable = true;
	private boolean autoClose = false;

	MenuItem(int slot, ItemStack item) {
		this.listeners = new ArrayList<>();
		this.slot = slot;
		this.item = item;
	}

	public boolean isAutoClose() {
		return autoClose;
	}

	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public boolean isMovenable() {
		return moveable;
	}

	public MenuItem setTitle(String title) {
		item.getItemMeta().setDisplayName(title);
		return this;
	}

	public MenuItem setMaterial(Material mat) {
		item.setType(mat);
		return this;
	}

	public MenuItem setItem(ItemStack item) {
		this.item = item;
		return this;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public void addClickListener(ClickListener listener) {
		listeners.add(listener);
	}

	public List<ClickListener> getListeners() {
		return listeners;
	}

}
