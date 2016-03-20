package de.avalon.rpg.priester;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.avalon.mmo.Clazz;
import de.avalon.rpg.Skill;
import de.avalon.utils.ParticleEffectAPI;

public class Verzweifeltes_Gebet extends Skill {

	public Verzweifeltes_Gebet(String name, int minMana, int minLevel, int cooldown, String description) {
		super(name, minMana, minLevel, cooldown, description);
	}
	
	
	@Override
	public void use(Clazz clazz) {
		clazz.setLastUse(this, System.currentTimeMillis());
		clazz.setMana(0);
	}

	@Override
	public int run(Clazz clazz) {
		
		if (super.run(clazz) == 1) {
			
			Player p = clazz.getHero().getBukkitPlayer();
			
			p.setHealth(20d);
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*3, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 2));
			

			ParticleEffectAPI.HEART.display(1, 1, 1, 0, 10, p.getLocation().add(0, 1, 0) , 2);
			
			
			p.sendMessage("§aErfolgreich Skill §6Verzweifeltes Gebet §aausgeführt");
			
			use(clazz);
		}
		return super.run(clazz);
	}

}
