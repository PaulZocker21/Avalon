package de.avalon.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.avalon.player.Hero;

public class HeroListener implements Listener {

	@EventHandler
	public void onHeroJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Hero hero = Hero.getHero(player);
		if (hero == null)
			hero = Hero.create(player);
		hero.sendMessage("Hallo " + hero.getName());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Monster) {
			Player killer = ((Monster) entity).getKiller();
			Hero hero = Hero.getHero(killer);
			if (hero == null)
				hero = Hero.create(killer);
			hero.getSelectetClass().addExp(30);
		}
	}

}
