package de.avalon.rpg;

import java.util.HashMap;

import de.avalon.mmo.Clazz;

public abstract class Skill implements Cloneable {

	public static final int ERROR_MIN_LEVEL = -1;
	public static final int ERROR_COOLDOWN = -2;
	public static final int ERROR_MANA = -3;
	public static final int ERROR_UNKOWN = -4;

	private static HashMap<String, Skill> skills = new HashMap<>();

	public static HealSkill skill_heal = new HealSkill("Heal", 20, 1, 5);

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

	protected Skill(String name, int minMana, int minLevel, int cooldown) {
		this.name = name;
		this.minMana = minMana;
		this.minLevel = minLevel;
		this.cooldown = cooldown;

		skills.put(name, this);
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
