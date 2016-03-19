package de.avalon.mmo;

public abstract class Levelable {

	private static int maxLevel = 10;

	private int exp;
	private int level;
	private int maxExp;

	public int getExp() {
		return exp;
	}

	public int getLevel() {
		return level;
	}

	public void setExp(int exp) {
		if (exp >= maxExp) {
			setExp(maxExp % exp);
			setLevel(level + 1);
			reachNextLevel();
			maxExp = calculateMaxExp(level);
		}
		this.exp = exp;
	}

	public abstract int calculateMaxExp(int level);

	public abstract void reachNextLevel();

	public abstract void reachMaxLevel();

	public void setLevel(int level) {
		if (level > maxLevel)
			level = maxLevel;
		if (level == maxLevel) {
			this.level = level;
			reachMaxLevel();
			return;
		}
		this.level = level;
	}
}
