package de.avalon.rpg;

import java.util.HashMap;

import de.avalon.mmo.Clazz;
import de.avalon.rpg.priester.Aufopferung;
import de.avalon.rpg.priester.Phantom;
import de.avalon.rpg.priester.Verzweifeltes_Gebet;

public abstract class Skill implements Cloneable {

	public static final int ERROR_MIN_LEVEL = -1;
	public static final int ERROR_COOLDOWN = -2;
	public static final int ERROR_MANA = -3;
	public static final int ERROR_UNKOWN = -4;
	public static final int ERROR_NOT_RIGHT_CLASS = -5;
	
	private static HashMap<String, Skill> skills = new HashMap<>();

	// Name, Mana, Level, Cooldown, Description
	public static HealSkill skill_heal = new HealSkill("Heal", 20, 1, 5, "Heile dich leicht");

	// Priester

	/*
	 * Aufopferung to-do : WitherHead soll kein Damage mehr machen WitherHead
	 * soll nicht mehr explodieren (Kein Block-Damage) Player der vom WitherHead
	 * getroffen wird, wird auf 100% Leben geheilt und bekommt strange boost
	 */

	public static Verzweifeltes_Gebet skill_verzweifeltes_gebet = new Verzweifeltes_Gebet("Verzweifeltes Gebet", 0, 15, 10, "In deiner Verzweiflung gibt dir das Beten neue Lebensenergie"); // Zieht
	public static Aufopferung skill_aufopferung = new Aufopferung("Aufopferung", 0, 10, 30, "Du opferst dich für einen Freund!"); // Zieht
	public static Phantom skill_phantom = new Phantom("Phantom", 80, 20, 20, "Entferne alle Bewegungseinschränkende Effekte!"); // level

	public static Skill getSkill(String name) {
		return skills.get(name);
	}

	public static HashMap<String, Skill> getSkills() {
		return skills;
	}

	private String name;
	private int minMana;
	private int minLevel;
	private long cooldown;
	private String description;

	protected Skill(String name, int minMana, int minLevel, int cooldown, String description) {
		this.name = name;
		this.minMana = minMana;
		this.minLevel = minLevel;
		this.cooldown = cooldown;
		this.description = description;

		skills.put(name, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Skill) {
			Skill skill = (Skill) obj;
			if (skill.getName().equals(getName()))
				return true;
		}
		return super.equals(obj);
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public int getMinMana() {
		return minMana;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public int run(Clazz clazz) {
		if (clazz.getLevel() < minLevel)
			return ERROR_MIN_LEVEL;
		if (clazz.getLastUss(this) + cooldown > System.currentTimeMillis())
			return ERROR_COOLDOWN;
		if (minMana > clazz.getMana())
			return ERROR_MANA;
		return 1;
	}

	public void use(Clazz clazz) {
		clazz.setLastUse(this, System.currentTimeMillis());
		clazz.setMana(clazz.getMana() - minMana);
	}

}
