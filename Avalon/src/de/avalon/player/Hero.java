package de.avalon.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Hero {

	private static HashMap<UUID, Hero> heros = new HashMap<>();

	private UUID uuid;
	private String name;
	
	private Hero(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public void sendMessage(String message) {
		getBukkitPlayer().sendMessage(message);
	}

	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(uuid);
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

	public static Hero create(Player player) {
		if (heros.containsKey(player.getUniqueId())) {
			return null;
		}
		Hero hero = new Hero(player.getUniqueId(), player.getName());
		heros.put(hero.getUniqueId(), hero);
		return hero;
	}

	public static Hero getHero(Player player) {
		return heros.get(player.getUniqueId());
	}

	public void save(YamlConfiguration config) {
		config.set(uuid + ".name", name);
	}

	public static void loadAll(File file) {
		if (file == null)
			return;
		if (!file.getName().endsWith(".yml"))
			return;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String uuid : config.getConfigurationSection("").getKeys(false)) {
			String name = config.getString(uuid + ".name");
			Hero hero = new Hero(UUID.fromString(uuid), name);
			heros.put(hero.getUniqueId(), hero);
		}
		System.out.println("Loaded " + heros.size() + " heros");
	}

	public static void saveAll(File file) {
		if (file == null)
			return;
		if (!file.getName().endsWith(".yml"))
			return;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		heros.values().forEach(hero -> hero.save(config));
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}