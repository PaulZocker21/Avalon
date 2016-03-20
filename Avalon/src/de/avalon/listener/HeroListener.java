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

import de.avalon.bossbar.BossBarAPI;
import de.avalon.bossbar.BossBarAPI.Color;
import de.avalon.bossbar.BossBarAPI.Style;
import de.avalon.mmo.Digging;
import de.avalon.mmo.Forest;
import de.avalon.mmo.Mining;
import de.avalon.player.Hero;
import net.md_5.bungee.api.chat.TextComponent;

public class HeroListener implements Listener {

	// Chest System
	// @EventHandler
	// public void onChestProtectAndOpen(PlayerInteractEvent e) {
	// if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	// if (e.getClickedBlock().getType().equals(Material.CHEST)) {
	// if (!(Avalon.chests.containsKey(e.getClickedBlock().getLocation()))) {
	// if ((e.getPlayer().getItemInHand().getType().equals(Material.IRON_INGOT))
	// || (e.getPlayer().getItemInHand().getType().equals(Material.GOLD_INGOT))
	// || (e.getPlayer().getItemInHand().getType().equals(Material.DIAMOND)) ||
	// (e.getPlayer().getItemInHand().getType().equals(Material.OBSIDIAN))) {
	// Chest_Avalon chest = new Chest_Avalon();
	// chest.setOwner(e.getPlayer().getUniqueId());
	// chest.setSavedBy(e.getPlayer().getItemInHand().getType());
	//
	// Avalon.chests.put(e.getClickedBlock().getLocation(), chest);
	//
	// e.getPlayer().sendMessage("§aErfolgreich Truhe mit " +
	// e.getPlayer().getItemInHand().getType() + " gesichert!");
	//
	// }
	// } else {
	// UUID uuid =
	// Avalon.chests.get(e.getClickedBlock().getLocation()).getOwner();
	// if (!(e.getPlayer().getUniqueId().equals(uuid))) {
	//
	// e.getPlayer().sendMessage("§cDiese Truhe ist leider von §6" +
	// Bukkit.getServer().getPlayer(uuid).getName() + "§c mit §6" +
	// Avalon.chests.get(e.getClickedBlock().getLocation()).getSavedBy() + "§c
	// geschützt!");
	// e.setCancelled(true);
	//
	// }
	//
	// }
	// }
	// }
	// }

	// Chest System Ende

	@EventHandler
	public void onHeroInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Hero hero = Hero.getHero(player);
		if (hero == null)
			hero = Hero.create(player);
		Material mat = e.getPlayer().getItemInHand().getType();
		if (Forest.tools.contains(mat)) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				int code = hero.getForest().use();
				if (code == 1)
					hero.sendMessage("§aDu hast die Spezialfähigkeit benutzt!\n§6Du kannst nun Bäume mit einem Schlag abfarmen.");
			}
		} else if (Digging.tools.contains(mat)) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				int code = hero.getDigging().use();
				if (code == 1)
					hero.sendMessage("§aDu hast die Spezialfähigkeit benutzt!\n§6Du hast nun den Potion Effect 'Eile'");
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
		Material tool = player.getItemInHand().getType();
		if (Mining.materials.containsKey(mat) && Mining.tools.contains(tool)) {
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
		} else if (Forest.materials.containsKey(mat) && Forest.tools.contains(tool)) {
			int exp = Forest.getExperience(mat);
			hero.getForest().addExp(exp);
			hero.getForest().runSpecial(e.getBlock());
		} else if (Digging.materials.containsKey(mat) && Digging.tools.contains(tool)) {
			int exp = Digging.getExperience(mat);
			hero.getDigging().addExp(exp);
		}
	}

	@EventHandler
	public void onHeroJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Hero hero = Hero.getHero(player);
		if (hero == null)
			hero = Hero.create(player);
		hero.sendMessage("Hallo " + hero.getName());
		BossBarAPI.addBar(player, new TextComponent("Jaaa"), Color.BLUE, Style.NOTCHED_10, 1f).setVisible(true);;
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
