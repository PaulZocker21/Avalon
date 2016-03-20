package de.avalon.mmo;

import java.util.UUID;

import org.bukkit.Material;

public class Chest_Avalon {

	private UUID owner;
	private Material savedBy;
	
	
	public UUID getOwner() {
		return owner;
	}
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	public Material getSavedBy() {
		return savedBy;
	}
	public void setSavedBy(Material savedBy) {
		this.savedBy = savedBy;
	}
	
}
