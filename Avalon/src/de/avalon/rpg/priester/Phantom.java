package de.avalon.rpg.priester;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import de.avalon.mmo.Clazz;
import de.avalon.rpg.Skill;
import de.avalon.utils.ParticleEffectAPI;

public class Phantom extends Skill {

	public Phantom(String name, int minMana, int minLevel, int cooldown, String description) {
		super(name, minMana, minLevel, cooldown, description);
	}
	
	
	

	@Override
	public int run(Clazz clazz) {
	
		
		if (super.run(clazz) == 1) {
			
			Player player = clazz.getHero().getBukkitPlayer();

			
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.removePotionEffect(PotionEffectType.CONFUSION);			
			player.removePotionEffect(PotionEffectType.WEAKNESS);		
			player.removePotionEffect(PotionEffectType.SLOW);			
			player.removePotionEffect(PotionEffectType.POISON);			
	          

			ParticleEffectAPI.SPELL_MOB_AMBIENT.display(1, 1, 1, 1, 50, player.getLocation().add(0, 1, 0) , 2);
			
			player.sendMessage("§aErfolgreich Skill §6Phantom §aausgeführt");
			
			use(clazz);
		}
		

		
		return super.run(clazz);
	}

}
