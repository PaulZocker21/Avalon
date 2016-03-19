package de.avalon.mmo;

import de.avalon.player.Hero;

public class Clazz extends Levelable {

	private Hero hero;
	private String name;

	public Clazz(String name, Hero hero) {
		super();
		this.name = name;
		this.hero = hero;
	}

	public Clazz(String name, int level, int exp, Hero hero) {
		super(level, exp);
		this.name = name;
		this.hero = hero;
	}

	public String getName() {
		return name;
	}

	@Override
	public int calculateMaxExp(int level) {
		return level * (100 + 2 * level);
	}

	@Override
	public void reachNextLevel(int level) {
		hero.sendMessage("Herzlichen Glückwunsch, du hast " + level + " erreicht!");
	}

	@Override
	public void reachMaxLevel() {
		hero.sendMessage("Toll! Du hast das maximale Level " + getMaxLevel() + " erreicht!");
	}

	@Override
	public void eaernExp(int exp) {
		hero.sendMessage("+" + exp + "xp");
	}

	public Hero getHero() {
		return hero;
	}
}
