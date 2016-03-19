package de.avalon.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Hero {

	private static HashMap<UUID, Hero> heros = new HashMap<>();

	private UUID uuid;
	private String name;

	private Hero(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public static Hero create(Player player) {
		if (heros.containsKey(player.getUniqueId())) {
			return null;
		}
		return new Hero(player.getUniqueId(), player.getName());
	}

	public static Hero getHero(Player player) {
		return heros.get(player.getUniqueId());
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}