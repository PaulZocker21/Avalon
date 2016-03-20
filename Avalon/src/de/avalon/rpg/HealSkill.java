package de.avalon.rpg;

import de.avalon.mmo.Clazz;

public class HealSkill extends Skill {

	public HealSkill(String name, int minMana, int minLevel, int cooldown) {
		super(name, minMana, minLevel, cooldown);
	}

	@Override
	public int run(Clazz clazz) {
		if (super.run(clazz) == 1) {
			clazz.getHero().getBukkitPlayer().setHealth(20d);
			use(clazz);
		}
		return super.run(clazz);
	}

}
