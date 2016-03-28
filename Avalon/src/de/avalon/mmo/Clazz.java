package de.avalon.mmo;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.avalon.Avalon;
import de.avalon.player.Hero;
import de.avalon.rpg.Skill;
import de.avalon.utils.ParticleEffectAPI;

public class Clazz extends Levelable {

	private Hero hero;
	private HashMap<Skill, Long> skills;
	private String name;
	private int mana;
	private int maxMana;

	@Override
	public int getMaxLevel() {
		return 100;
	}

	public Clazz(String name, Hero hero) {
		super();
		this.skills = new HashMap<>();
		this.name = name;
		this.hero = hero;
		this.mana = 20;
	}

	public Clazz(String name, int level, int exp, Hero hero) {
		super(level, exp);
		this.skills = new HashMap<>();
		this.name = name;
		this.hero = hero;
		this.mana = 20 + (level * 5);
		this.maxMana = this.mana;
	}

	public HashMap<Skill, Long> getSkills() {
		return skills;
	}

	public int performSkill(String name) {
		Skill skill = Skill.getSkill(name);
		if (skill == null)
			return Skill.ERROR_UNKOWN;
		return skill.run(this);
	}

	public long getLastUss(String name) {
		Skill skill = Skill.getSkill(name);
		return getLastUss(skill);
	}

	public long getLastUss(Skill skill) {
		if (skill == null)
			return -1;
		if (skills.containsKey(skill))
			return skills.get(skill);
		return 0L;
	}

	public void setLastUse(String name, long lastUse) {
		Skill skill = Skill.getSkill(name);
		setLastUse(skill, lastUse);
	}

	public void setLastUse(Skill skill, long lastUse) {
		if (skill == null)
			return;
		skills.put(skill, lastUse);
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
		setMaxMana(20 + (level * 5));
		hero.sendMessage("Herzlichen Glückwunsch, du hast " + level + " erreicht!");
		new BukkitRunnable() {

			private double t = Math.PI / 4;
			private Location loc = hero.getBukkitPlayer().getEyeLocation();
			private final int maxRadius = 15;

			@Override
			public void run() {
				if (t > maxRadius) {
					cancel();
					return;
				}
				t += 0.1 * Math.PI;
				for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 32) {
					double x = t * Math.cos(theta);
					double y = 2 * Math.exp(-0.1 * t) * Math.sin(t);
					double z = t * Math.sin(theta);
					loc.add(x, y, z);
					ParticleEffectAPI.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, loc, hero.getBukkitPlayer());
					loc.subtract(x, y, z);

					theta = theta + Math.PI / 64;
					
					x = t * Math.cos(theta);
					y = 2 * Math.exp(-0.1 * t) * Math.sin(t);
					z = t * Math.sin(theta);
					loc.add(x, y, z);
					ParticleEffectAPI.SPELL_WITCH.display(0, 0, 0, 0, 1, loc, hero.getBukkitPlayer());
					loc.subtract(x, y, z);
				}
			}
		}.runTaskTimer(Avalon.getPlguin(), 0, 1L);

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
