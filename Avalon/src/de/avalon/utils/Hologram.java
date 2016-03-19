package de.avalon.utils;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class Hologram {

	private String text;
	private ArmorStand armorstand;
	private static ArrayList<Hologram> holograms = new ArrayList<>();
	
	
	public Hologram(String text, Location where) {
		
		this.text = text;
		this.armorstand = (ArmorStand) where.getWorld().spawnEntity(new Location(where.getWorld(), where.getX(), where.getY(), where.getZ()), EntityType.ARMOR_STAND);
		
		armorstand.setCanPickupItems(false);
		armorstand.setCustomName(text);
		armorstand.setCustomNameVisible(true);
		armorstand.setVisible(false);
		
		holograms.add(this);
	}
	
	
	public void setVelocity(Vector v) {
		armorstand.setVelocity(v);
	}
	
	public void remove() {
		holograms.remove(this);
		armorstand.remove();
	}
	
	public void setLocation(Location loc){
		armorstand.teleport(loc);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Hologram) {
			return armorstand.equals(((Hologram)obj).armorstand);
		}
		return super.equals(obj);
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		armorstand.setCustomName(text);
	}
	
}
