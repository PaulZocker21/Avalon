package de.avalon.rpg;

import de.avalon.mmo.Clazz;

public class HealSkill extends Skill {

	public HealSkill(String name, int minMana, int minLevel, int cooldown, String description) {
		super(name, minMana, minLevel, cooldown, description);
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
