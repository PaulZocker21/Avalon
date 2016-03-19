package de.avalon.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.avalon.mmo.Clazz;

public class Hero {

	private static HashMap<UUID, Hero> heros = new HashMap<>();

	private UUID uuid;
	private String name;
	private HashMap<String, Clazz> classes;
	private String selectetClass;

	private Hero(UUID uuid, String name) {
		this.classes = new HashMap<>();
		this.uuid = uuid;
		this.name = name;

		addClass(new Clazz("Test", this));

		this.selectetClass = "Test";
	}

	private Hero(UUID uuid, String name, String selectetClass) {
		this.classes = new HashMap<>();
		this.uuid = uuid;
		this.name = name;
		this.selectetClass = selectetClass;
	}

	public Clazz getSelectetClass() {
		classes.keySet().forEach(s -> System.out.println(s));
		return classes.get(selectetClass);
	}

	public void setSelectetClass(String selectetClass) {
		this.selectetClass = selectetClass;
	}

	public HashMap<String, Clazz> getClasses() {
		return classes;
	}

	public void addClass(Clazz clazz) {
		classes.put(clazz.getName(), clazz);
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
		config.set(uuid + ".selectetClass", selectetClass);
		String clazzPath = uuid + ".classes.";
		classes.values().forEach(clazz -> {
			config.set(clazzPath + clazz.getName() + "." + ".level", clazz.getLevel());
			config.set(clazzPath + clazz.getName() + "." + ".exp", clazz.getExp());
		});
	}

	public static void loadAll(File file) {
		if (file == null)
			return;
		if (!file.getName().endsWith(".yml"))
			return;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String uuid : config.getConfigurationSection("").getKeys(false)) {
			String name = config.getString(uuid + ".name");
			String selectetClass = config.getString(uuid + ".selectetClass");
			Hero hero = new Hero(UUID.fromString(uuid), name, selectetClass);
			for (String clazzName : config.getConfigurationSection(uuid + ".classes").getKeys(false)) {
				int level = config.getInt(uuid + ".classes." + clazzName + ".level");
				int exp = config.getInt(uuid + ".classes." + clazzName + ".exp");
				Clazz clazz = new Clazz(clazzName, level, exp, hero);
				hero.addClass(clazz);
			}
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