package de.avalon.mmo;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import de.avalon.Avalon;
import de.avalon.player.Hero;

public class Forest extends Levelable {

	public static HashMap<Material, Integer> materials = new HashMap<>();

	public static int getExperience(Material mat) {
		return materials.get(mat);
	}

	static {
		materials.put(Material.WOOD, 5);
	}

	private static final long cooldown = 60 * 1000L;

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
		setLastUse(lastUse);
		setSpecial(true);
		new BukkitRunnable() {

			@Override
			public void run() {
				setSpecial(false);
			}
		}.runTaskLater(Avalon.getPlguin(), cooldown);
		return 1;
	}

	public void runSpecial(Block block) {
		if (use() == 1) {
			if (isSpecial()) {
				if (!hero.getForest().isSpecial()) {
					while ((block = block.getRelative(BlockFace.UP)).getType() == Material.WOOD) {
						block.breakNaturally();
					}
					while ((block = block.getRelative(BlockFace.DOWN)).getType() == Material.WOOD) {
						block.breakNaturally();
					}
				}
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
