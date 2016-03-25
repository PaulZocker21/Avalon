package de.avalon.mmo;

import java.util.ArrayList;

import de.avalon.player.Hero;
import de.avalon.rpg.Skill;

public class PriestClazz extends Clazz {

	public static ArrayList<Skill> skills = new ArrayList<>();

	public PriestClazz(Hero hero) {
		super("Priester", hero);
	}

	public PriestClazz(int level, int exp, Hero hero) {
		super("Priester", level, exp, hero);
	}

	@Override
	public int performSkill(String name) {
		Skill skill = Skill.getSkill(name);
		if (skill == null) {
			return Skill.ERROR_UNKOWN;
		}
		if (!skills.contains(skill)) {
			return Skill.ERROR_NOT_RIGHT_CLASS;
		}
		return super.performSkill(name);
	}

	public ArrayList<Skill> getSkills(int level) {
		ArrayList<Skill> skills = new ArrayList<>();
		skills.forEach(skill -> {
			if (skill.getMinLevel() == level) {
				skills.add(skill);
			}
		});
		return skills;
	}

}
