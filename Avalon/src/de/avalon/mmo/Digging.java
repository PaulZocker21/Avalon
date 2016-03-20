package de.avalon.mmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.avalon.player.Hero;

public class Digging extends Levelable {

	public static HashMap<Material, Integer> materials = new HashMap<>();
	public static List<Material> tools = new ArrayList<>();

	public static int getExperience(Material mat) {
		return materials.get(mat);
	}

	static {
		materials.put(Material.DIRT, 1);
		materials.put(Material.GRASS, 2);

		tools.add(Material.DIAMOND_SPADE);
		tools.add(Material.STONE_SPADE);
		tools.add(Material.IRON_SPADE);
		tools.add(Material.GOLD_SPADE);
		tools.add(Material.DIAMOND_SPADE);
	}

	private static final long cooldown = 60 * 1000L;

	private Hero hero;
	private boolean special;
	private long lastUse;

	public Digging(Hero hero) {
		super();
		this.hero = hero;
	}

	public Digging(int level, int exp, Hero hero) {
		super(level, exp);
		this.hero = hero;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public void setLastUse(long lastUse) {
		this.lastUse = lastUse;
	}

	public boolean isSpecial() {
		return special;
	}

	public long getLastUse() {
		return lastUse;
	}

	public int use() {
		if (lastUse + cooldown > System.currentTimeMillis())
			return -1;
		if (special)
			return -2;
		setLastUse(System.currentTimeMillis());
		setSpecial(true);
		PotionEffect effect = new PotionEffect(PotionEffectType.FAST_DIGGING, calculateUsing() * 20, 7);
		hero.getBukkitPlayer().addPotionEffect(effect);
		return 1;
	}

	private int calculateUsing() {
		return 5 + getLevel();
	}

	public Hero getHero() {
		return hero;
	}

	@Override
	public void eaernExp(int exp) {

	}

	@Override
	public int calculateMaxExp(int level) {
		return level * 20;
	}

	@Override
	public void reachNextLevel(int level) {
		getHero().sendMessage("Digging Level " + level);
	}

	@Override
	public void reachMaxLevel() {
		getHero().sendMessage("Herzlichen Glückwunsch digging ist auf maximalem Level " + getMaxLevel());
	}

}
