package de.avalon.listener;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkillListener implements Listener {

	
	//Für Skill Aufopferung
	
	public static ArrayList<WitherSkull> aws = new ArrayList<WitherSkull>();
	
	@EventHandler
	public void onBlockBreak(EntityDamageByEntityEvent e) {
		
		if(aws.size() > 0)
			if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)){
				if(e.getDamager() instanceof WitherSkull){
					
					for(WitherSkull ws : aws){
						if(ws.equals(e.getDamager())){
							Player p = (Player) ws.getShooter();
							
							//Wenn er einen anderen Spieler trifft
							if((!e.getEntity().getUniqueId().equals(p.getUniqueId())) && (e.getEntity() instanceof Player)){
								Player victim = (Player) e.getEntity();
								victim.setHealth(20d);
								victim.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 2)); //20 Sekunden lang, 2. Stärke
								
							}
							
							
						}
					}
					
					
				}
			}
		
	}
	
	
	@EventHandler
	public void onBlockBreak(EntityExplodeEvent e) {
		
		if(aws.size() > 0)
			for(WitherSkull ws : aws){
				if(ws.equals(e.getEntity())){
					e.setCancelled(true);
				}
			}
			
		
	}
	
	
	
}
