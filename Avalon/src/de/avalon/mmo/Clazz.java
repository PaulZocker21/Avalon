package de.avalon.mmo;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.avalon.Avalon;
import de.avalon.player.Hero;

public class Clazz extends Levelable {

	private Hero hero;
	private String name;
	private int mana;
	private int maxMana;

	public Clazz(String name, Hero hero) {
		super();
		this.name = name;
		this.hero = hero;
		this.mana = 20;
	}

	public Clazz(String name, int level, int exp, Hero hero) {
		super(level, exp);
		this.name = name;
		this.hero = hero;
		this.mana = 20 + (level * 5);
		this.maxMana = this.mana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public int getMana() {
		return mana;
	}

	private BukkitTask task;

	public void setMana(int mana) {
		if (mana > maxMana)
			mana = maxMana;
		if (mana < maxMana) {
			if (task == null) {
				task = new BukkitRunnable() {

					@Override
					public void run() {
						if (Clazz.this.mana == maxMana) {
							task = null;
							cancel();
						}
						System.out.println(Clazz.this.mana);
						Clazz.this.mana++;
					}
				}.runTaskTimer(Avalon.getPlguin(), 0L, 20L);
			}
		}
		this.mana = mana;

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
