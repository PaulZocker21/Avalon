package de.avalon.listener;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.avalon.mmo.Forest;
import de.avalon.mmo.Mining;
import de.avalon.player.Hero;

public class HeroListener implements Listener {

	@EventHandler
	public void onHeroInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Hero hero = Hero.getHero(player);
		if (hero == null)
			hero = Hero.create(player);
		Material mat = e.getPlayer().getItemInHand().getType();
		if (mat.equals(Material.WOOD_AXE) || mat.equals(Material.STONE_AXE) || mat.equals(Material.IRON_AXE) || mat.equals(Material.GOLD_AXE) || mat.equals(Material.DIAMOND_AXE)) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				int code = hero.getForest().use();
				if (code == 1)
					hero.sendMessage("Du hast die Spezialfähigkeit benutzt!");
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Hero hero = Hero.getHero(player);
		if (hero == null)
			hero = Hero.create(player);
		Material mat = e.getBlock().getType();
		if (Mining.materials.containsKey(mat)) {
			int exp = Mining.getExperience(mat);
			hero.getMining().addExp(exp);
			if (mat == Material.DIAMOND_ORE || mat == Material.REDSTONE_ORE || mat == Material.LAPIS_ORE || mat == Material.EMERALD_ORE) {
				Random random = new Random();
				int value = random.nextInt(101);
				if (value >= 0 && value <= hero.getMining().getLuckPercent()) {
					e.getBlock().getDrops().forEach(item -> {
						e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), item.clone());
					});
				}
			}
		} else if (Forest.materials.containsKey(mat)) {
			int exp = Forest.getExperience(mat);
			hero.getForest().addExp(exp);
			hero.getForest().runSpecial(e.getBlock());
		}
	}

	@EventHandler
	public void onHeroJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Hero hero = Hero.getHero(player);
		if (hero == null)
			hero = Hero.create(player);
		hero.sendMessage("Hallo " + hero.getName());
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Hero hero = Hero.getHero(e.getPlayer());
		if (hero == null)
			hero = Hero.create(e.getPlayer());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Monster) {
			if (((Monster) entity).getKiller() instanceof Player) {
				Player killer = ((Monster) entity).getKiller();
				Hero hero = Hero.getHero(killer);
				if (hero == null)
					hero = Hero.create(killer);
				hero.getSelectetClass().addExp(30);
				hero.getSelectetClass().setMana(0);
			}
		}
	}

}
