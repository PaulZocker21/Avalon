package de.avalon.mmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

import de.avalon.player.Hero;

public class Mining extends Levelable {

	public static HashMap<Material, Integer> materials = new HashMap<>();
	public static List<Material>tools = new ArrayList<>();
	
	public static int getExperience(Material mat) {
		return materials.get(mat);
	}

	static {
		materials.put(Material.STONE, 1);
		materials.put(Material.COAL_ORE, 3);
		materials.put(Material.IRON_ORE, 5);
		materials.put(Material.LAPIS_ORE, 7);
		materials.put(Material.REDSTONE_ORE, 10);
		materials.put(Material.GOLD_ORE, 15);
		materials.put(Material.EMERALD_ORE, 20);
		materials.put(Material.DIAMOND_ORE, 25);
	
		tools.add(Material.WOOD_PICKAXE);
		tools.add(Material.STONE_PICKAXE);
		tools.add(Material.IRON_PICKAXE);
		tools.add(Material.GOLD_PICKAXE);
		tools.add(Material.DIAMOND_PICKAXE);
	}

	private Hero hero;

	public Mining(Hero hero) {
		super();
		this.hero = hero;
	}

	public Mining(int level, int exp, Hero hero) {
		super(level, exp);
		this.hero = hero;
	}

	public int getLuckPercent() {
		return getLevel();
	}

	public Hero getHero() {
		return hero;
	}

	@Override
	public void eaernExp(int exp) {

	}

	@Override
	public int calculateMaxExp(int level) {
		return level * 25;
	}

	@Override
	public void reachNextLevel(int level) {
		getHero().sendMessage("Mining Level " + level);
	}

	@Override
	public void reachMaxLevel() {
		getHero().sendMessage("Herzlichen Glückwunsch mining ist auf maximalem Level " + getMaxLevel());
	}

}
