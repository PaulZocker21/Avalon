package de.avalon.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.avalon.mmo.Clazz;
import de.avalon.mmo.Digging;
import de.avalon.mmo.Forest;
import de.avalon.mmo.Mining;

public class Hero {

	private static HashMap<UUID, Hero> heros = new HashMap<>();

	public static final int COMBAT_TIME = 10;

	private UUID uuid;
	private String name;

	private HashMap<String, Clazz> classes;
	private String selectetClass;
	private Mining mining;
	private Forest forest;
	private Digging digging;
	private GUI gui;

	private Hero(UUID uuid, String name) {
		this.classes = new HashMap<>();
		this.uuid = uuid;
		this.name = name;

		addClass(new Clazz("Test", this));

		this.selectetClass = "Test";
		this.mining = new Mining(this);
		this.gui = new GUI(this);
	}

	private Hero(UUID uuid, String name, String selectetClass) {
		this.classes = new HashMap<>();
		this.uuid = uuid;
		this.name = name;
		this.selectetClass = selectetClass;
		this.mining = new Mining(this);
		this.gui = new GUI(this);
	}

	public GUI getGui() {
		return gui;
	}

	public Digging getDigging() {
		return digging;
	}

	private void setDigging(Digging digging) {
		this.digging = digging;
	}

	private void setForest(Forest forest) {
		this.forest = forest;
	}

	public Forest getForest() {
		return forest;
	}

	public Mining getMining() {
		return mining;
	}

	public Clazz getSelectetClass() {
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

	private void setMining(Mining mining) {
		this.mining = mining;
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
		config.set(uuid + ".mining.level", mining.getLevel());
		config.set(uuid + ".mining.exp", mining.getExp());
		config.set(uuid + ".forest.level", forest.getLevel());
		config.set(uuid + ".forest.exp", forest.getExp());
		config.set(uuid + ".digging.level", digging.getLevel());
		config.set(uuid + ".digging.exp", digging.getExp());
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
			int miningLevel = config.getInt(uuid + ".mining.level");
			int miningExp = config.getInt(uuid + ".mining.exp");
			Mining mining = new Mining(miningLevel, miningExp, hero);
			hero.setMining(mining);
			int forestLevel = config.getInt(uuid + ".forest.level");
			int forestExp = config.getInt(uuid + ".forest.exp");
			Forest forest = new Forest(forestLevel, forestExp, hero);
			hero.setForest(forest);
			int diggingLevel = config.getInt(uuid + ".digging.level");
			int diggingExp = config.getInt(uuid + ".digging.exp");
			Digging digging = new Digging(diggingLevel, diggingExp, hero);
			hero.setDigging(digging);

			heros.put(hero.getUniqueId(), hero);

			Player player = Bukkit.getPlayer(hero.getUniqueId());
			if (player != null && player.isOnline())
				hero.getGui().init();
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