package de.avalon.mmo;

public abstract class Levelable {

	private final int maxLevel = 10;

	private int exp;
	private int level;
	private int maxExp;

	public Levelable() {
		this.exp = 0;
		this.level = 1;
		this.maxExp = calculateMaxExp(2);
	}

	public Levelable(int level, int exp) {
		this.level = level;
		this.exp = exp;

		this.maxExp = calculateMaxExp(level + 1);
	}

	public void addExp(int exp) {
		setExp(this.exp + exp);
	}

	public int getExp() {
		return exp;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setExp(int exp) {
		if (this.level == maxLevel)
			return;
		if (exp >= maxExp) {
			this.exp = exp - maxExp;
			setLevel(level + 1);
			reachNextLevel(level);
			maxExp = calculateMaxExp(level + 1);
			return;
		}
		if (exp > this.exp) {
			eaernExp(exp - this.exp);
		}
		this.exp = exp;
	}

	public abstract void eaernExp(int exp);

	public abstract int calculateMaxExp(int level);

	public abstract void reachNextLevel(int level);

	public abstract void reachMaxLevel();

	public void setLevel(int level) {
		if (this.level == maxLevel)
			return;
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
