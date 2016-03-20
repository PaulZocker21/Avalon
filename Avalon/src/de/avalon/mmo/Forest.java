package de.avalon.mmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import de.avalon.Avalon;
import de.avalon.player.Hero;

public class Forest extends Levelable {

	public static HashMap<Material, Integer> materials = new HashMap<>();
	public static List<Material>tools = new ArrayList<>();
	
	public static int getExperience(Material mat) {
		return materials.get(mat);
	}

	static {
		materials.put(Material.LOG, 5);
		materials.put(Material.LOG_2, 5);

		tools.add(Material.WOOD_AXE);
		tools.add(Material.STONE_AXE);
		tools.add(Material.IRON_AXE);
		tools.add(Material.GOLD_AXE);
		tools.add(Material.DIAMOND_AXE);
	}

	private static final long cooldown = 5 * 1000L;

	private Hero hero;
	private boolean special;
	private long lastUse;

	public Forest(Hero hero) {
		super();
		this.hero = hero;
	}

	public Forest(int level, int exp, Hero hero) {
		super(level, exp);
		this.hero = hero;
	}

	public int use() {
		if (lastUse + cooldown > System.currentTimeMillis())
			return -1;
		if (special)
			return -2;
		setLastUse(System.currentTimeMillis());
		setSpecial(true);
		new BukkitRunnable() {

			@Override
			public void run() {
				setSpecial(false);
			}
		}.runTaskLater(Avalon.getPlguin(), calculateUsing() * 20L);
		return 1;
	}

	public long calculateUsing() {
		return 5 + getLevel();
	}

	public void runSpecial(Block b) {
		if (isSpecial()) {
			Block block = b;
			while ((block = block.getRelative(BlockFace.UP)).getType() == Material.LOG || block.getType() == Material.LOG_2) {
				block.breakNaturally();
				addExp(1);
			}
			while ((b = b.getRelative(BlockFace.DOWN)).getType() == Material.LOG || b.getType() == Material.LOG_2) {
				b.breakNaturally();
				addExp(1);
			}
		}
	}

	public void setLastUse(long lastUse) {
		this.lastUse = lastUse;
	}

	public long getLastUse() {
		return lastUse;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public boolean isSpecial() {
		return special;
	}

	public Hero getHero() {
		return hero;
	}

	@Override
	public void eaernExp(int exp) {

	}

	@Override
	public int calculateMaxExp(int level) {
		return level * 15;
	}

	@Override
	public void reachNextLevel(int level) {
		getHero().sendMessage("Forest Level " + level);
	}

	@Override
	public void reachMaxLevel() {
		getHero().sendMessage("Herzlichen Glückwunsch Abholzung ist auf maximalem Level " + getMaxLevel());
	}

}
