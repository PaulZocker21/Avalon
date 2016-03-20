package de.avalon.rpg.priester;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;

import de.avalon.mmo.Clazz;
import de.avalon.rpg.Skill;
import de.avalon.utils.ParticleEffectAPI;

public class Aufopferung extends Skill {

	public Aufopferung(String name, int minMana, int minLevel, int cooldown, String description) {
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
			
			Player player = clazz.getHero().getBukkitPlayer();

			
			
			
	          EntityType snow = EntityType.WITHER_SKULL;
              Location loc = player.getLocation().add(0, 1, 0);
              
              
              WitherSkull sb = (WitherSkull) player.getWorld().spawnEntity(loc, snow);
         
   
              
              sb.getLocation().getDirection().multiply(10D).setY(0D);    		
              sb.setShooter(player);
              sb.setVelocity(player.getEyeLocation().getDirection());
			    
			

			  ParticleEffectAPI.SPELL_WITCH.display(1, 1, 1, 1, 50, clazz.getHero().getBukkitPlayer().getLocation().add(0, 1, 0) , 2);
              ParticleEffectAPI.SMOKE_LARGE.display(1, 1, 1, 0, 20, clazz.getHero().getBukkitPlayer().getLocation().add(0, 1, 0) , 2);
  			
			
			                    
			clazz.getHero().getBukkitPlayer().setHealth(0d);
			clazz.getHero().getBukkitPlayer().sendMessage("§aErfolgreich Skill §6Aufopferung §aausgeführt");
			
			use(clazz);
		}
		

		
		return super.run(clazz);
	}

}
